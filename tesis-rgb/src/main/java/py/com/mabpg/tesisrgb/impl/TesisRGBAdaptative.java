/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.mabpg.tesisrgb.impl;

import py.com.mabpg.imagestorage.models.RgbImage;
import py.com.mabpg.tesisrgb.generics.Mean;
import py.com.mabpg.tesisrgb.generics.TesisRGBBasicAbstract;
import py.com.mabpg.tesisrgb.generics.Weight;
import py.com.mabpg.tesisrgb.models.Pixel;

/**
 *
 * @author belis
 */
public class TesisRGBAdaptative extends TesisRGBBasicAbstract {
    public TesisRGBAdaptative(int roiWindow, String filter, RgbImage rgbImage, Pixel[] se) {
        super(roiWindow, filter, rgbImage, se);
        setFilterName("TesisRGBAdaptative");
    }

    @Override
    public Weight getWeight() {
        return new Mean();
    }
}
