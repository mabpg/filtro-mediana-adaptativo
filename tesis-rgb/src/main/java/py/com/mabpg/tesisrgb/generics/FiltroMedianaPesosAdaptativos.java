/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.mabpg.tesisrgb.generics;

import ij.process.ByteProcessor;
import java.util.ArrayList;
import java.util.List;
import py.com.mabpg.tesisrgb.models.FormulaPeso;
import py.com.mabpg.tesisrgb.models.FormulaPrevia;
import py.com.mabpg.tesisrgb.models.Pixel;
import py.com.mabpg.tesisrgb.models.PixelWeight2;

/**
 *
 * @author belis
 */
public class FiltroMedianaPesosAdaptativos {
    
    public Pixel[] se;
    public int width;
    public int height;    
    public FormulaPrevia formulaPrevia;     
    public FormulaPeso formulaPeso;
    public String [] components = {"R", "G", "B"};
    public ByteProcessor[] channels = new ByteProcessor[components.length];
    
    
    public FiltroMedianaPesosAdaptativos(Pixel[] se, int height, int width, ByteProcessor[] channels) {
      
        this.se = se;
        this.height = height;
        this.width = width;
        this.formulaPrevia = new FormulaPrevia();
        this.formulaPeso = new FormulaPeso();
        this.channels = channels;
    }
    
    /*Se hallan todas las distancias desde el elemento central de la máscara
    o elemento estructurante a cada elemento, en las posiciones posibles*/
    public void hallarDistancias(int posXcentral, int posYcentral) {                
        formulaPrevia.setPosXelemCentral(posXcentral);
        formulaPrevia.setPosYelemCentral(posYcentral);
        int x, y;
        double x1,y1;
        List<Double> distancias = new ArrayList<>();
        for (Pixel sePixel : se) {
            
            x = posXcentral + sePixel.getX();
            y = posYcentral + sePixel.getY();
            
            if (x > -1 && x < width && y > -1 && y < height) {
                x1 = Math.pow(((double)posXcentral) - (double)(x),2.0);   //OJO VOLVER A MIRAR ESTA PARTE
                y1 = Math.pow(((double)posYcentral) - (double)(y),2.0); 
                distancias.add(Math.sqrt(x1 + y1));
            }
        }
        formulaPrevia.setDistancia(distancias);
    }
    
    
    /*Hallamos cada uno de los elementos (en sus tres componentes, RGB)dentro de la mascara
    además de la media, la desviacion estandar y seteamos la cte de escalamiento
    que nos servira para hallar las formulas de los pesos */
    public List<PixelWeight2> preOrder(Pixel p) {
        int cLength = channels.length;
        int x, y;
        double t = 0;
        int[] rgbColor;
        List<PixelWeight2> orderPixelWeight = new ArrayList<>();
        PixelWeight2 pixelWeight;
        double media = 0;
        int cantElementos = 0;

        for (Pixel sePixel : se) {
            x = p.getX() + sePixel.getX();
            y = p.getY() + sePixel.getY();
            //verificamos si está en la ventana del elemento estructurante
            if (x > -1 && x < width && y > -1 && y < height) {
                rgbColor = new int[cLength];
                cantElementos = cantElementos + 1;    //para hallar la media dentro de la mascara
                for (int channel = 0; channel < cLength; channel++) {
                    rgbColor[channel] = channels[channel].get(x, y);
                    //t = t + weight[channel] * rgbColor[channel];
                    t = t + rgbColor[channel];
                }

                pixelWeight = new PixelWeight2(rgbColor, (int)Math.ceil(t/3), 0,x,y);
                orderPixelWeight.add(pixelWeight);
                media = media + pixelWeight.getElemento();
                t = 0;
            }
        }
        //logger.debug("orderPixelWeight={}", orderPixelWeight.toString());
        if (cantElementos > 0) {
            formulaPrevia.setMedia((int) Math.ceil((media)/cantElementos));
        }
         
        /*Hallamos la desviacion estandar */
        double diferencia = 0;
        double varianza = 0;
        for (PixelWeight2 elem : orderPixelWeight) {
            diferencia = elem.getElemento() - formulaPrevia.getMedia();
            varianza = varianza + Math.pow(diferencia,2);
        }
        varianza = varianza / orderPixelWeight.size();
        
        formulaPrevia.setVarianza((int)Math.ceil(varianza));
        
        formulaPrevia.setCteEscalamiento(0.3);
        
        return orderPixelWeight;
    }
    
     public void hallarPesos (List<PixelWeight2> orderPixelWeight) {
        List<Double> listPesos = new ArrayList<>();
        List<Double> distancias = formulaPrevia.getDistancia();
        double maximo = -1.0;
        int indiceMax = -1;
        double formula = 0.0;
        
        /*armamos una parte de la formula para hallar cada peso, y hallamos
        el máximo elemento para usar como peso del elemento central*/
        for (int indice = 0; indice < distancias.size(); indice++) {
        
            formula = formulaPrevia.getCteEscalamiento() * distancias.get(indice)
            * formulaPrevia.getDsvStandar() / formulaPrevia.getMedia();
            listPesos.add(formula);
            
            if(formula > maximo){
                maximo = formula;
                indiceMax = indice;
            }
            if(indice == distancias.size() - 1) { //el peso del elemento central va a ser el valor maximo + 2
                maximo = maximo + 2;
            }
        } 
        
        /*Hallamos la formula completa de cada peso*/
        for (int i = 0; i < listPesos.size(); i++) {
            if (maximo <= 0) {          //PARA QUE NO HAYA CICLO INFINITO
                formulaPeso.setBanderaPesosCero(true);
                //System.out.println("Igual pesos");
                break;
            }
            //if (i != indiceMax) {
                formula = maximo - listPesos.get(i);
                orderPixelWeight.get(i).setWeight((int)Math.ceil(formula));
            //} else { //la formula 
                //orderPixelWeight.get(i).setWeight((int)Math.ceil(maximo));
            //}
        }
        
        formulaPeso.setOrderPixelWeight(orderPixelWeight);
       
    }
}
