/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.mabpg.tesisrgb.generics;

import py.com.mabpg.tesisrgb.models.FormulaPrevia;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import py.com.mabpg.imagestorage.models.RgbImage;
import py.com.mabpg.imagestorage.utils.RgbImageJpaController;
import py.com.mabpg.tesisrgb.models.FormulaPeso;
import py.com.mabpg.tesisrgb.models.Pixel;
import py.com.mabpg.tesisrgb.models.PixelWeight;
import py.com.mabpg.tesisrgb.models.PixelWeight2;
import py.com.mabpg.tesisrgb.models.TesisComparator;

/**
 *
 * @author Derlis Arg√ºello
 */
public abstract class BasicFilterAbstract {
    //jpa components
    public RgbImageJpaController rgbImageJpaController;
    public RgbImage rgbImage;
    
    public int width;
    public int height;
    public ColorProcessor noisyColProcessor;
    public ColorProcessor restoredColProcessor;
    public String filterName;
    public String filter;
    
    public Pixel[] se;
    
    public FormulaPrevia formulaPrevia;
    
    public FormulaPeso formulaPeso;
    
    public String [] components = {"R", "G", "B"};
    //defaultOrder
    public int[] componentsOrder = {0, 1, 2};
    public ByteProcessor[] channels = new ByteProcessor[components.length];

    public double[] decisionByComp = new double[components.length];
    public long [] decisionByCompCounter = new long[components.length];
    
    public double reducedValue = 0;
    public long reducedValueCounter = 0;

    public Weight weight;

    public BasicFilterAbstract(String filter, RgbImage rgbImage, Pixel[] se) {
        //set noisyColProcessor data
        rgbImage.setChannelData();
        
        this.filter = filter;
        this.se = se;
        this.formulaPrevia = new FormulaPrevia();
        this.formulaPeso = new FormulaPeso();
        this.rgbImage = rgbImage;
        this.restoredColProcessor = new ColorProcessor(rgbImage.getWidth(), rgbImage.getHeight());
        this.noisyColProcessor = rgbImage.getColorProcessor();
        this.height = noisyColProcessor.getHeight();
        this.width = noisyColProcessor.getWidth();
        this.channels = rgbImage.getChannels();
        rgbImageJpaController = new RgbImageJpaController();
    }

    public abstract void setWindowsList() throws Exception;
    
    /*Se hallan todas las distancias desde el elemento central de la m·scara
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
    adem·s de la media, la desviacion estandar y seteamos la cte de escalamiento
    que nos servira para hallar las formulas de los pesos */
    public List<PixelWeight2> preOrder(Pixel p) {
        int cLength = channels.length;
        int x, y;
        double t = 0.0;
        int[] rgbColor;
        List<PixelWeight2> orderPixelWeight = new ArrayList<>();
        PixelWeight2 pixelWeight;
        double media = 0;
        int cantElementos = 0;

        for (Pixel sePixel : se) {
            x = p.getX() + sePixel.getX();
            y = p.getY() + sePixel.getY();
            //verificamos si esta en la ventana del elemento estructurante
            if (x > -1 && x < width && y > -1 && y < height) {
                rgbColor = new int[cLength];
                cantElementos = cantElementos + 1;    //para hallar la media dentro de la mascara
                for (int channel = 0; channel < cLength; channel++) {
                    rgbColor[channel] = channels[channel].get(x, y);
                    //t = t + weight[channel] * rgbColor[channel];
                    t = t + rgbColor[channel];
                }

                pixelWeight = new PixelWeight2(rgbColor, t/3, 0,x,y);
                orderPixelWeight.add(pixelWeight);
                media = media + pixelWeight.getElemento();
                t = 0.0;
            }
        }
        //logger.debug("orderPixelWeight={}", orderPixelWeight.toString());
        if (cantElementos > 0) {
            formulaPrevia.setMedia(media/cantElementos);
        }
         
        /*Hallamos la desviacion estandar */
        double diferencia = 0;
        double desvSt = 0;
        for (PixelWeight2 elem : orderPixelWeight) {
            diferencia = elem.getElemento() - formulaPrevia.getMedia();
            desvSt = desvSt + Math.pow(diferencia,2);
        }
        desvSt = desvSt / orderPixelWeight.size();
        
        formulaPrevia.setDsvStandar(desvSt);
        
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
        el m·ximo elemento para usar como peso del elemento central*/
        for (int indice = 0; indice < distancias.size(); indice++) {
        
            formula = formulaPrevia.getCteEscalamiento() * distancias.get(indice)
            * formulaPrevia.getDsvStandar() / formulaPrevia.getMedia();
            listPesos.add(formula);
            
            if(formula > maximo){
                maximo = formula;
                indiceMax = indice;
            }
        } 
        
        /*Hallamos la formula completa de cada peso*/
        for (int i = 0; i < listPesos.size(); i++) {
            if (maximo <= 0) {          //PARA QUE NO HAYA CICLO INFINITO
                formulaPeso.setBanderaPesosCero(true);
                break;
            }
            if (i != indiceMax) {
                formula = Math.ceil(maximo - listPesos.get(i));
                orderPixelWeight.get(i).setWeight(formula);
            }
        }
        
        formulaPeso.setOrderPixelWeight(orderPixelWeight);
       
    }
    
    public int[] order(List<PixelWeight2> orderPixelWeight) {
        int cLength = channels.length;
      
        int[] filterP;

        TesisComparator comparator = new TesisComparator(cLength);
        //ordenamos por peso
        Collections.sort(orderPixelWeight, comparator);

        //logger.debug("orderPixelWeight={}", orderPixelWeight.toString());

        for (int i = 0; i < cLength; i++) {
            decisionByCompCounter[i] += comparator.chooseChannel[i];
        }
        //los valores reducidos
        reducedValueCounter += comparator.valorReducido;
        //obtenemos el filtro
        filterP = getFilter(orderPixelWeight);

        //logger.debug("filterP={}", Arrays.toString(filterP));

        return filterP;
    }

    //implementaciones de los filtros
    public int[] min(List<PixelWeight2> orderPixelWeight) {
        int element = 0;
        return orderPixelWeight.get(element).getPixel();
    }

    public int[] max(List<PixelWeight2> orderPixelWeight) {
        int element = orderPixelWeight.size() -  1;
        return orderPixelWeight.get(element).getPixel();
    }

    public int[] median(List<PixelWeight2> orderPixelWeight) {
        
        int cantElementos = 0;
        for (PixelWeight2 elem : orderPixelWeight) {
            cantElementos = cantElementos + (int)elem.getWeight();
        }
        int elementoMediana = (int) Math.ceil(cantElementos / 2); //posicion mediana
        
        int indice = 0;
        
        //retornamos la mediana, tomando en cuenta el peso de cada elemento
        for (PixelWeight2 elem : orderPixelWeight) {
            
            for (int i = 0;i < elem.getWeight();i++) {
                indice++;
                
                if(indice == elementoMediana) {
                    return elem.getPixel();
                }
            }            
        }
        return null;
    }

    //solicitar tipo de filtro
    public int[] getFilter(List<PixelWeight2> orderPixelWeight){
        switch(filter){
            case "Min":
                return min(orderPixelWeight);
            case "Max":
                return max(orderPixelWeight);
            case "Median":
                return median(orderPixelWeight);
            default:
                return null;
        }
    }

    public double[] calculateWeight(int[][] channelHistogram, int numPixels){
        return weight.calculateWeight(channelHistogram, numPixels);
    }

    public abstract double[] getRealWeight(Pixel p);

    public abstract Weight getWeight();

    public ColorProcessor run() throws Exception {
        int[] elementP;
        Pixel pixel;
        long totalDecisiones = 0;
        setWindowsList();
        weight = getWeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                
                System.out.println("Pixel x: " + x + "y: " + y);
                pixel = new Pixel(x, y);
                //realWeight = getRealWeight(pixel);
                hallarDistancias(x,y);
                List<PixelWeight2> prueba = preOrder(pixel);
                hallarPesos(prueba);
                //EN CASO DE QUE TODOS LOS PESOS SEAN CERO
                /*if(formulaPeso.getBanderaPesosCero()) {
                    int [] elementPixel = new int [2];
                    
                    formulaPeso.getOrderPixelWeight();
                    elementPixel[0] = x;
                    elementPixel[1] = y;
                    restoredColProcessor.putPixel(x, y, elementPixel);
                } else {*/
                    elementP = order(prueba);
                    restoredColProcessor.putPixel(x, y, elementP);
                //}
                
            }
        }

        show();

        for (int channel = 0; channel < channels.length; channel++) {
            totalDecisiones = decisionByCompCounter[channel];
        }

        totalDecisiones += reducedValueCounter;

        for (int i = 0; i < channels.length; i++) {
            decisionByComp[i] = (double)decisionByCompCounter[i]/(double)totalDecisiones;
        }

        reducedValue = (double)reducedValueCounter/(double)totalDecisiones;
        return restoredColProcessor;
    }
    
    /**
     * debug
     */
    public void print(){
        ImagePlus imgPlus = new ImagePlus(filterName + filter, restoredColProcessor);
        imgPlus.show();
    }
    
    /**
     * for save restored image in disk
     */
    public void save(){
        String imgExtension = rgbImage.getExtension();
        ImagePlus imgPlus = new ImagePlus(filterName, restoredColProcessor);
        if (imgExtension.equalsIgnoreCase("png")) {
            new FileSaver(imgPlus).saveAsPng(filterName + "." + imgExtension);
        }else if (imgExtension.equalsIgnoreCase("jpg")){
            new FileSaver(imgPlus).saveAsJpeg(filterName + "." +  imgExtension);
        }
    }

    public void show(){
       //save();
    };

    @Override
    public String toString() {
        return "BasicFilterAbstract{" + "rgbImage=" + rgbImage.toString() + ", width=" + width + ", height=" + height + ", filterName=" + filterName + ", filter=" + filter + ", decisionByComp=" + Arrays.toString(decisionByComp) + ", reducedValue=" + reducedValue + '}';
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName + filter;
    }
}