/*
 * Tesis Arguello Balbuena
 * Derechos Reservados 2015
 */
package py.com.mabpg.tesisrgb.models;

import java.util.Arrays;

/**
 *
 * @author daasalbion
 */
public class PixelWeight2 {
    private int[] pixel;
    private double elemento;
    private double weight;    

    public PixelWeight2(int[] pixel, double elemento, double weight) {
        this.pixel = pixel;
        this.elemento = elemento;
        this.weight = weight;        
    }

    public int[] getPixel() {
        return pixel;
    }

    public void setPixel(int[] pixel) {
        this.pixel = pixel;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getElemento() {
        return elemento;
    }

    public void setElemento(double elemento) {
        this.elemento = elemento;
    }

    @Override
    public String toString() {
        return "PixelWeight{" + "pixel=" + Arrays.toString(pixel) + ", weight=" + weight + '}';
    }

}