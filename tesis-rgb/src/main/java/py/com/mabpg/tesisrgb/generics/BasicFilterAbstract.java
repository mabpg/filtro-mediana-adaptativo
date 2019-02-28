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
import py.com.mabpg.tesisrgb.models.PixelHCL;
import py.com.mabpg.tesisrgb.models.PixelWeight2;
import py.com.mabpg.tesisrgb.models.TesisComparator;
import py.com.mabpg.tesisrgb.models.TesisComparatorSymmetricMatrix;

/**
 *
 * @author Derlis Argüello
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
    
    public FiltroMedianaPesosAdaptativos adaptative;
    
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
        this.adaptative = new FiltroMedianaPesosAdaptativos(this.se, this.height, this.width, this.channels);
        rgbImageJpaController = new RgbImageJpaController();
    }

    public abstract void setWindowsList() throws Exception;
    
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

    
    /*NUEVO ORDER A PEDIDO DEL PROFE ORDER LOEWENER*/
    
    public int[] orderNew(Pixel p) {
        int cLength = channels.length;
        int x, y;
        double t = 0.0;
        int[] rgbColor;
        List<PixelHCL> orderPixelLoewner = new ArrayList<>();
        PixelHCL pixelHCL;
        ConverterLoewner converterL = new ConverterLoewner();
        float[] xyz = new float[3];
        float[][] hclColorMatrizSimetrica = new float[2][2];
        
        int[] filterP;

        for (Pixel sePixel : se) {
            x = p.getX() + sePixel.getX();
            y = p.getY() + sePixel.getY();
            //verificamos si esta en la ventana del elemento estructurante
            if (x > -1 && x < width && y > -1 && y < height) {
                rgbColor = new int[cLength];
                for (int channel = 0; channel < cLength; channel++) {
                    rgbColor[channel] = channels[channel].get(x, y); 
                }

                /**Convertir a hcl el color rgb y luego hcl a xyz**/	
                xyz = converterL.hclToXyz(converterL.rgbToHcl(rgbColor));
                
                /**Obtener la matriz simetrica 2x2 correspondiente del xyz **/
                hclColorMatrizSimetrica = converterL.xyzToSymmetricMatrix(xyz);
                
                
                /**Guardar el nuevo pixel RGB, su correspondencia XYZ, matriz simetrica**/
                pixelHCL = new PixelHCL(rgbColor, xyz, hclColorMatrizSimetrica, x, y);
                
                orderPixelLoewner.add(pixelHCL);
            }
        }

        TesisComparatorSymmetricMatrix comparatorSymmetricMatrix = new TesisComparatorSymmetricMatrix(cLength);
        //ordenamos las matrices simetricas de acuerdo al compare del comparatorSymmetricMatrix
        orderPixelLoewner = comparatorSymmetricMatrix.ordenar(orderPixelLoewner);

        //obtenemos el filtro
        filterP = getFilterNew(orderPixelLoewner);

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

    //implementaciones Nuevas para el orden loewner
    public int[] minNew(List<PixelHCL> orderPixelLoewner) {
        int element = 0;
        return orderPixelLoewner.get(element).getPixelRGB();
    }

    public int[] maxNew(List<PixelHCL> orderPixelLoewner) {
        int element = orderPixelLoewner.size() -  1;
        return orderPixelLoewner.get(element).getPixelRGB();
    }

    public int[] medianNew(List<PixelHCL> orderPixelLoewner) {
        int element = (int) Math.ceil(orderPixelLoewner.size() / 2);
        return orderPixelLoewner.get(element).getPixelRGB();
    }
    
    public int[] getFilterNew(List<PixelHCL> orderPixelLoewner){
        switch(filter){
            case "Min":
                return minNew(orderPixelLoewner);
            case "Max":
                return maxNew(orderPixelLoewner);
            case "Median":
                return medianNew(orderPixelLoewner);
            default:
                return null;
        }
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
                
               //System.out.println("Pixel X: " + x + "Y: " + y);
                pixel = new Pixel(x, y);
                //realWeight = getRealWeight(pixel);
                adaptative.hallarDistancias(x,y);
                List<PixelWeight2> prueba = adaptative.preOrder(pixel);
                adaptative.hallarPesos(prueba);
                //EN CASO DE QUE TODOS LOS PESOS SEAN CERO
                if(formulaPeso.getBanderaPesosCero()) {
                    elementP = devolver(x,y);
                } else {
                    //elementP = order(prueba);
                    
                    elementP = orderNew(pixel); //NUEVO ORDER
                    
                }
                restoredColProcessor.putPixel(x, y, elementP);
                /*if (x == width - 1 && y == height - 1) {
                    System.out.println("Pixel x: " + x + "y: " + y);
                }*/
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
    
    //metodo que devuelve el elemento dada la posicion x e y dentro de la imagen
    public int [] devolver (int posX, int posY) {
        
        for (PixelWeight2 elemento: formulaPeso.getOrderPixelWeight()) {
            if (elemento.getPosX() == posX && elemento.getPosY() == posY ) {
                return elemento.getPixel();
            }
        }
        
        return null;
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