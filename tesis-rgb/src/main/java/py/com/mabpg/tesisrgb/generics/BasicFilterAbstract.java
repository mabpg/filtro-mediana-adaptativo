/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.mabpg.tesisrgb.generics;

import py.com.mabpg.tesisrgb.models.FormulaPesos2;
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
import py.com.mabpg.tesisrgb.models.Pixel;
import py.com.mabpg.tesisrgb.models.PixelWeight;
import py.com.mabpg.tesisrgb.models.TesisComparator;

/**
 *
 * @author Derlis ArgÃ¼ello
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
    
    public FormulaPesos2 dist;
    
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
        this.dist = new FormulaPesos2();
        this.rgbImage = rgbImage;
        this.restoredColProcessor = new ColorProcessor(rgbImage.getWidth(), rgbImage.getHeight());
        this.noisyColProcessor = rgbImage.getColorProcessor();
        this.height = noisyColProcessor.getHeight();
        this.width = noisyColProcessor.getWidth();
        this.channels = rgbImage.getChannels();
        rgbImageJpaController = new RgbImageJpaController();
    }

    public abstract void setWindowsList() throws Exception;
    
    /*Se hallan todas las distancias desde el elemento central de la máscara
    o elemento estructurante a cada elemento, en las posiciones posibles dentro de*/
    public void hallarDistancias(int posXcentral, int posYcentral) {                
        dist.setPosXelemCentral(posXcentral);
        dist.setPosYelemCentral(posYcentral);
        int x, y;
        double x1,y1;
        int indice = 0;
        int [] distancias = null;
        for (Pixel sePixel : se) {
            
            x = posXcentral + sePixel.getX();
            y = posYcentral + sePixel.getY();
            
            if (x > -1 && x < width && y > -1 && y < height) {
                x1 = Math.pow(((double)posXcentral) - (double)((posXcentral - sePixel.getX())),2.0);
                y1 = Math.pow(((double)posYcentral) - (double)((posXcentral - sePixel.getY())),2.0); 
                distancias[indice] = (int)Math.sqrt(x1 + y1);
            
                dist.setDistancia(distancias);
            }
            
        }
    }
    
    
    public List<PixelWeight> preOrder(Pixel p) {
        int cLength = channels.length;
        int x, y;
        double t = 0.0;
        int[] rgbColor;
        List<PixelWeight> orderPixelWeight = new ArrayList<>();
        PixelWeight pixelWeight;
        int media = 0;

        for (Pixel sePixel : se) {
            x = p.getX() + sePixel.getX();
            y = p.getY() + sePixel.getY();
            //verificamos si esta en la ventana del elemento estructurante
            if (x > -1 && x < width && y > -1 && y < height) {
                rgbColor = new int[cLength];
                for (int channel = 0; channel < cLength; channel++) {
                    rgbColor[channel] = channels[channel].get(x, y);
                    //t = t + weight[channel] * rgbColor[channel];
                    t = t + rgbColor[channel];
                }

                pixelWeight = new PixelWeight(rgbColor, t/3);
                orderPixelWeight.add(pixelWeight);
                media = media + (int) pixelWeight.getWeight();
                t = 0.0;
            }
        }
        //logger.debug("orderPixelWeight={}", orderPixelWeight.toString());
        
        dist.setMedia(media);
        
      return orderPixelWeight;
    }

    public int[] order(double[] weight, Pixel p) {
        int cLength = channels.length;
        int x, y;
        double t = 0.0;
        int[] rgbColor;
        List<PixelWeight> orderPixelWeight = new ArrayList<>();
        PixelWeight pixelWeight;
        int[] filterP;

        for (Pixel sePixel : se) {
            x = p.getX() + sePixel.getX();
            y = p.getY() + sePixel.getY();
            //verificamos si esta en la ventana del elemento estructurante
            if (x > -1 && x < width && y > -1 && y < height) {
                rgbColor = new int[cLength];
                for (int channel = 0; channel < cLength; channel++) {
                    rgbColor[channel] = channels[channel].get(x, y);
                    //t = t + weight[channel] * rgbColor[channel];
                    t = t + rgbColor[channel];
                }

                pixelWeight = new PixelWeight(rgbColor, t/3);
                orderPixelWeight.add(pixelWeight);
                t = 0.0;
            }
        }

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
    public int[] min(List<PixelWeight> orderPixelWeight) {
        int element = 0;
        return orderPixelWeight.get(element).getPixel();
    }

    public int[] max(List<PixelWeight> orderPixelWeight) {
        int element = orderPixelWeight.size() -  1;
        return orderPixelWeight.get(element).getPixel();
    }

    public int[] median(List<PixelWeight> orderPixelWeight) {
        int element = (int) Math.ceil(orderPixelWeight.size() / 2);
        return orderPixelWeight.get(element).getPixel();
    }

    //solicitar tipo de filtro
    public int[] getFilter(List<PixelWeight> orderPixelWeight){
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
        double[] realWeight;
        Pixel pixel;
        long totalDecisiones = 0;
        setWindowsList();
        weight = getWeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixel = new Pixel(x, y);
                realWeight = getRealWeight(pixel);
                hallarDistancias(x,y);
                List<PixelWeight> prueba = preOrder(pixel);
                elementP = order(realWeight, pixel);
                restoredColProcessor.putPixel(x, y, elementP);
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