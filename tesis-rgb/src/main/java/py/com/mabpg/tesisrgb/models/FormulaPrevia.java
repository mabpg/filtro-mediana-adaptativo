/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.mabpg.tesisrgb.models;

import java.util.List;

/**
 *
 * @author belis
 */
public class FormulaPrevia {
    
    private List<Double> distancia;
    
    private int posXelemCentral;
    
    private int posYelemCentral;
    
    private int media;
    
    private int varianza;
    
    private double cteEscalamiento;

    public FormulaPrevia() {
    }   

    public List<Double> getDistancia() {
        return distancia;
    }

    public void setDistancia(List<Double> distancia) {
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

    public double getDsvStandar() {
        return varianza;
    }

    public void setVarianza(int varianza) {
        this.varianza = varianza;
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
    }

    public double getCteEscalamiento() {
        return cteEscalamiento;
    }

    public void setCteEscalamiento(double cteEscalamiento) {
        this.cteEscalamiento = cteEscalamiento;
    }
    
}
