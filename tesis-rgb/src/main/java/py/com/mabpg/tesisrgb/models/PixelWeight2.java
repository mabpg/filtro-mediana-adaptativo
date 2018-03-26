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
    private int elemento;
    private int weight;
    private int posX;
    private int posY;

    public PixelWeight2(int[] pixel, int elemento, int weight, int x, int y) {
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

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getElemento() {
        return elemento;
    }

    public void setElemento(int elemento) {
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