/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.mabpg.tesisrgb.impl;



import py.com.mabpg.imagestorage.models.RgbImage;
import py.com.mabpg.tesisrgb.generics.Entropy;
import py.com.mabpg.tesisrgb.generics.TesisRGBBasicAbstractWW;
import py.com.mabpg.tesisrgb.generics.Weight;
import py.com.mabpg.tesisrgb.models.Pixel;

/**
 *
 * @author Derlis Argüello
 * @Nomenclatura: [Metodo][Orden] 
 * @Ejemplo: [TesisRGB][Mean]
 */
public class TesisRGBEntropyWW extends TesisRGBBasicAbstractWW {

    public TesisRGBEntropyWW(int roiWindow, String filter, RgbImage rgbImage, Pixel[] se) {
        super(roiWindow, filter, rgbImage, se);
        setFilterName("TesisRGBEntropyWW");
    }

    @Override
    public Weight getWeight() {
        return new Entropy();
    }
}