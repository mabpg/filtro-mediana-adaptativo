package py.com.mabpg.tesisrgb.models;

import java.util.Arrays;

public class PixelHCL {
	    private int[] pixelRGB;
	    private float[] pixelHCL;
	    private float[][] hclColorMatrizSimetrica;
	    private int posicionX;
	    private int posicionY;
	    
	    public PixelHCL(int[] pixelRGB, float[] pixelHCL, float[][] hclColorMatrizSimetrica, int x, int y) {
	        this.pixelRGB = pixelRGB;
	        this.pixelHCL = pixelHCL;
	        this.hclColorMatrizSimetrica = hclColorMatrizSimetrica;
	        this.posicionX = x;
	        this.posicionY = y;
	    }

	    

	    



		public int getPosicionX() {
			return posicionX;
		}







		public void setPosicionX(int posicionX) {
			this.posicionX = posicionX;
		}







		public int getPosicionY() {
			return posicionY;
		}







		public void setPosicionY(int posicionY) {
			this.posicionY = posicionY;
		}







		public int[] getPixelRGB() {
			return pixelRGB;
		}







		public void setPixelRGB(int[] pixelRGB) {
			this.pixelRGB = pixelRGB;
		}







		public float[] getPixelHCL() {
			return pixelHCL;
		}







		public void setPixelHCL(float[] pixelHCL) {
			this.pixelHCL = pixelHCL;
		}







		public float[][] getHclColorMatrizSimetrica() {
			return hclColorMatrizSimetrica;
		}







		public void setHclColorMatrizSimetrica(float[][] hclColorMatrizSimetrica) {
			this.hclColorMatrizSimetrica = hclColorMatrizSimetrica;
		}







		@Override
		public String toString() {
			return "PixelHCL [pixelRGB=" + Arrays.toString(pixelRGB) + ", pixelHCL=" + Arrays.toString(pixelHCL)
					+ ", hclColorMatrizSimetrica=" + Arrays.toString(hclColorMatrizSimetrica) + "]";
		}

	
}
