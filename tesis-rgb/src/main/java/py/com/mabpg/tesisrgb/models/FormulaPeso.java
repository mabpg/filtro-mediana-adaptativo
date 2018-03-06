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
public class FormulaPeso {
       
    private List<PixelWeight2> orderPixelWeight;
    
    private Pixel posicionMediana;
    //private ;

    public FormulaPeso() {
    }
    
    public List<PixelWeight2> getOrderPixelWeight() {
        return orderPixelWeight;
    }

    public void setOrderPixelWeight(List<PixelWeight2> orderPixelWeight) {
        this.orderPixelWeight = orderPixelWeight;
    }
}
