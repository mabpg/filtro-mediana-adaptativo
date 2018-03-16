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
    private int posX;
    private int posY;

    public PixelWeight2(int[] pixel, double elemento, double weight, int x, int y) {
        this.pixel = pixel;
        this.elemento = elemento;
        this.weight = weight;
        this.posX = x;
        this.posY = y;
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

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    @Override
    public String toString() {
        return "PixelWeight{" + "pixel=" + Arrays.toString(pixel) + ", weight=" + weight + '}';
    }

}