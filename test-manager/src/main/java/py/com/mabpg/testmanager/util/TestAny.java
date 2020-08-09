package py.com.mabpg.testmanager.util;


import py.com.mabpg.imagestorage.models.RgbImage;
import py.com.mabpg.tesisrgb.generics.BasicFilterAbstract;
import py.com.mabpg.tesisrgb.impl.*;
import py.com.mabpg.tesisrgb.models.Pixel;
import static py.com.mabpg.testmanager.util.TestConstantsManager.Filters.TesisRGB.ConVentanas.*;
import static py.com.mabpg.testmanager.util.TestConstantsManager.Filters.TesisRGB.SinVentanas.*;

/**
 *
 * @author Thelma
 */
public class TestAny {
    
    public static BasicFilterAbstract getFilterMethod(int windowCount, String filterName,
                                                      String filterType, RgbImage rgbImage, Pixel[] seEight){
        
        BasicFilterAbstract test;

        switch (filterName){
            case TESIS_RGB_ADAPTATIVE:
                test = new TesisRGBAdaptative(windowCount, filterType,  
                        rgbImage, seEight);
                break;
            case TESIS_RGB_MEAN:
                test = new TesisRGBMean(windowCount, filterType, rgbImage, seEight);
                break;
            case TESIS_RGB_VARIANCE:
                test = new TesisRGBVariance(windowCount, filterType,
                        rgbImage, seEight);
                break;
            case TESIS_RGB_MODE:
                test = new TesisRGBMode(windowCount, filterType,
                        rgbImage, seEight);
                break;
            case TESIS_RGB_MODE2:
                test = new TesisRGBMode2(windowCount, filterType,
                        rgbImage, seEight);
                break;
            case TESIS_RGB_MIN:
                test = new TesisRGBMin(windowCount, filterType,
                        rgbImage, seEight);
                break;
            case TESIS_RGB_MAX:
                test = new TesisRGBMax(windowCount, filterType,  
                        rgbImage, seEight);
                break;
            case TESIS_RGB_ENTROPY:
                test = new TesisRGBEntropy(windowCount, filterType,  
                        rgbImage, seEight);
                break;
            case TESIS_RGB_SMOOTHNESS:
                test = new TesisRGBSmoothness(windowCount, filterType,
                        rgbImage, seEight);
                break;
            case TESIS_RGB_MEAN_WW:
                test = new TesisRGBMeanWW(windowCount, filterType, rgbImage, seEight);
                break;
            case TESIS_RGB_VARIANCE_WW:
                test = new TesisRGBVarianceWW(windowCount, filterType,
                        rgbImage, seEight);
                break;
            case TESIS_RGB_MODE_WW:
                test = new TesisRGBModeWW(windowCount, filterType,
                        rgbImage, seEight);
                break;
            case TESIS_RGB_MIN_WW:
                test = new TesisRGBMinWW(windowCount, filterType,
                        rgbImage, seEight);
                break;
            case TESIS_RGB_MAX_WW:
                test = new TesisRGBMaxWW(windowCount, filterType,
                        rgbImage, seEight);
                break;
            case TESIS_RGB_ENTROPY_WW:
                test = new TesisRGBEntropyWW(windowCount, filterType,
                        rgbImage, seEight);
                break;
            case TESIS_RGB_SMOOTHNESS_WW:
                test = new TesisRGBSmoothnessWW(windowCount, filterType,
                        rgbImage, seEight);
                break;
            case TESIS_RGB_MODE2_WW:
                test = new TesisRGBMode2WW(windowCount, filterType,
                        rgbImage, seEight);
                break;
            default:
                test = null;
                break;
        }
        
        return test;
    }
}