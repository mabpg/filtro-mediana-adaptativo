package py.com.mabpg.tesisrgb.impl;


import py.com.mabpg.imagestorage.models.RgbImage;
import py.com.mabpg.tesisrgb.generics.Entropy;
import py.com.mabpg.tesisrgb.generics.TesisRGBBasicAbstract;
import py.com.mabpg.tesisrgb.generics.Weight;
import py.com.mabpg.tesisrgb.models.Pixel;

/**
 *
 * @author Derlis Argüello
 * @Nomenclatura: [Metodo][Orden] 
 * @Ejemplo: [TesisRGB][Mean]
 */
public class TesisRGBEntropy extends TesisRGBBasicAbstract {

    public TesisRGBEntropy(int roiWindow, String filter, RgbImage rgbImage, Pixel[] se) {
        super(roiWindow, filter, rgbImage, se);
        setFilterName("TesisRGBEntropy");
    }

    @Override
    public Weight getWeight() {
        return new Entropy();
    }
}