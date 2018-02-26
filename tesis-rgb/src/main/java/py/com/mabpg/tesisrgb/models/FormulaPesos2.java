/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.mabpg.tesisrgb.models;

/**
 *
 * @author belis
 */
public class FormulaPesos2 {
    
    private int[] distancia;
    
    private int posXelemCentral;
    
    private int posYelemCentral;
    
    private int media;
    
    private int dsvStandar;

    public FormulaPesos2() {
    }
    

    public FormulaPesos2(int[] distancia) {
        this.distancia = distancia;
    }    

    public int[] getDistancia() {
        return distancia;
    }

    public void setDistancia(int[] distancia) {
        this.distancia = distancia;
    }

    public int getPosXelemCentral() {
        return posXelemCentral;
    }

    public int getPosYelemCentral() {
        return posYelemCentral;
    }

    public void setPosXelemCentral(int posXelemCentral) {
        this.posXelemCentral = posXelemCentral;
    }

    public void setPosYelemCentral(int posYelemCentral) {
        this.posYelemCentral = posYelemCentral;
    }

    public int getDsvStandar() {
        return dsvStandar;
    }

    public void setDsvStandar(int dsvStandar) {
        this.dsvStandar = dsvStandar;
    }

    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
    }
       
}
