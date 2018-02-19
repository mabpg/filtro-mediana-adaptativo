package py.com.mabpg.tesisrgb.generics;

import py.com.mabpg.imagestorage.models.Window;

/**
 * Created by Thelma on 05/06/2016.
 */
public class Mode2 implements Weight {
    public double[] calculateWeight(int[][] channelHistogram, int numPixels){
        double[] mode = new double[channelHistogram.length];
        for (int channel = 0; channel < channelHistogram.length; channel++){
            int maxFrequency = 0;
            for (int intensity = 0; intensity < channelHistogram[0].length; intensity++){
                if(channelHistogram[channel][intensity] >= maxFrequency){
                    maxFrequency = channelHistogram[channel][intensity];
                    mode[channel] = intensity;
                }
            }
        }
        return mode;
    }

    public double[] getWeightFromDatabase(Window window){
        throw new UnsupportedOperationException("You are not supposed to call me :)");
    }

}
