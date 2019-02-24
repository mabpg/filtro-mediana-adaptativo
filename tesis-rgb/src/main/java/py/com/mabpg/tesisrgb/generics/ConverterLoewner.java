package py.com.mabpg.tesisrgb.generics;



public class ConverterLoewner {

	public static final float unoSobreRaizDos = (float)(1D/ Math.sqrt(2D));
	
	public float[] rgbToHcl(int[] rgb){
		
		 

		double h1 = 0d, s1 = 0d, l1 = 0d;
		double r =  (double)(rgb[0])/255D, g = (double)(rgb[1]) /255D, b = (double)(rgb[2])/255D;
//		System.out.println("rgb: "+ r +" "+ g + " " + b);
		
		
		// rgb a hsl
		double maxRGB = Math.max(b, Math.max(r, g));
		double minRGB = Math.min(b, Math.min(r, g));
//		System.out.println("max: " + maxRGB +", min: " + minRGB +" r:" + r + " g: " + g+ " b:" + b);
		
		if (maxRGB ==minRGB){
			h1 = 0;
//			System.out.println("1");
		}else if(maxRGB ==r){
//			h1 = (60 * ((g-b)/(maxRGB-minRGB)) + 360 )%360;
			h1 = ((g-b)/(maxRGB-minRGB))/6;
//			System.out.println("2");
		}else if(maxRGB ==g){
//			h1= 60 * ((b-r)/(maxRGB-minRGB)) + 120;
			h1= (2+(b-r)/(maxRGB-minRGB))/6;
//			System.out.println("3");
		}else if(maxRGB==b){
//			h1= 60 * ((r-g)/(maxRGB-minRGB)) + 240;;
			h1= (4+(r-g)/(maxRGB-minRGB))/6;
//			System.out.println("4");
		}
		
		l1 = (maxRGB + minRGB) /2;
		
//		System.out.println("hsl: " +h1 + " " + s1 + " " + l1);
		if (h1<0) h1=h1+1; // if hue is negative, add 1 to get it within 0 and 1
//		System.out.println("hsl: " +h1 + " " + s1 + " " + l1);
		float[] hcl = new float[3];
		hcl[0] = (float)h1; 
//		hcl[0] = (float)h1 / 360; ;
		hcl[1] = (float)(Math.max(r,Math.max(g, b)) - Math.min(r,Math.min(g, b)));
		hcl[2] = (float)(2 * l1  - 1);;
		return hcl;
		 
	}

	public float[] hclToXyz(float[] hcl){
		float[] xyz = new float[3];
//		xyz[0] = (float)(hcl[1] * Math.cos(2 * Math.PI  * Math.toRadians(hcl[0])));
		xyz[0] = (float)(hcl[1] * Math.cos(2 * Math.PI  * hcl[0]));
//		System.out.println("C: "+hcl[1]+ ", H: " + hcl[0]);
//		xyz[1] = (float)(hcl[1] * Math.sin(2 * Math.PI * Math.toRadians(hcl[0])));;
		xyz[1] = (float)(hcl[1] * Math.sin(2 * Math.PI * hcl[0]));;
		xyz[2] = hcl[2];
		return xyz;
	}
	
	public float[][] xyzToSymmetricMatrix(float[] xyz){
		float[][] symmetric = new float[2][2];
		float x = xyz[0];
		float y = xyz[1];
		float z = xyz[2];
		
		symmetric [0][0]= z - y;
		symmetric [0][1]= x;
		symmetric [1][0]= x;
		symmetric [1][1]= z + y;
		

		return symmetric;
	}
	
	/*A PARTIR DE ACA FUNCIONES PARA CONVERTIR MATRIZ SIMETRICA A RGB*/
	public int[] mat2x2ToRgb(float[][] mat){
		float xyz2[] = matToXyz(mat);
		float[] hcl2 = xyzToHcl(xyz2);
		float[] hsl2 = hclToHsl(hcl2);
		return hsltoRgb(hsl2[0], hsl2[1], hsl2[2]);
	}
	
	private  float[] matToXyz(float[][] mat){
		float xyz[] = new float[3];
		xyz[0] = unoSobreRaizDos  * 2 * mat[0][1];
		xyz[1] = unoSobreRaizDos  * (mat[1][1] - mat[0][0]);
		xyz[2] = unoSobreRaizDos  * (mat[1][1] + mat[0][0]);
		return xyz;
	}
	
	private  float[] xyzToHcl(float xyz[]){
		double at=Math.atan2(xyz[1],xyz[0]);
		if (at<0) at=at+2*Math.PI;
		
		float[] hcl  = new float[3];
		hcl[0] = (float)(at/(2*Math.PI));
		hcl[1] = (float)Math.hypot(xyz[0], xyz[1]);
		hcl[2] = xyz[2];
		return hcl;	
	}
	
	private static float[] hclToHsl(float hcl[]){
		float[] hsl  = new float[3];
		
		hsl[2] = (hcl[2] + 1F)/2F;
		if (hcl[1]==0){
			hsl[1] = 0;
		}else{
			//hsl[1] = hcl[1]/(1 - Math.abs(2*hsl[2]-1));
			hsl[1] = hcl[1]/(1F - Math.abs(2F*hsl[2]-1F));
		}
		hsl[0] = hcl[0];
		
		
		return hsl;
	}
	
	public static int[] hsltoRgb(float h, float s, float l) {

		  float r, g, b;
		 
		  if (s == 0) {
		    r = g = b = l; // achromatic
		  } else {
		    
		 
		    float q = l < 0.5 ? l * (1 + s) : l + s - l * s;
		    float p = 2 * l - q;
		 
		    r = hue2rgb(p, q, h + 1F/3F);
		    g = hue2rgb(p, q, h);
		    b = hue2rgb(p, q, h - 1F/3F);
		}
		
		int[] rgb = new int[3];
		rgb[0] = (int)Math.round(r*255.0);
		rgb[1] = (int)Math.round(g*255.0);
		rgb[2] = (int)Math.round(b*255.0);
		return rgb;

	}
	
	public static float hue2rgb(float p, float q, float t) {
	      if (t < 0) t += 1;
	      if (t > 1) t -= 1;
	      if (t < 1F/6F) return p + (q - p) * 6 * t;
	      if (t < 1F/2F) return q;
	      if (t < 2F/3F) return p + (q - p) * (2F/3F - t) * 6;
	      return p;
	}
}
