package py.com.mabpg.tesisrgb.models;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import py.com.mabpg.tesisrgb.generics.ConverterLoewner;


public class TesisComparatorSymmetricMatrix{
	    
	    //cantidad de veces que se opta por canal
	    public long [] chooseChannel;
	    public long valorReducido = 0;
	    public float [][] matrizLoewnerA_B;
	    public float [][] matrizLoewnerB_A;
	    public float [][] A;
	    public float [][] Min;
	   
	   
	    
	    public TesisComparatorSymmetricMatrix(int cSize) {
	        this.chooseChannel = new long[cSize];
	    }

	   
	    
	    public List<PixelHCL> ordenar (List<PixelHCL> orderPixelWeight){
	    	int tamanho=0;
	    	List<PixelHCL> orderPixelWeightClone= new ArrayList<PixelHCL>();
	    	List<PixelHCL> orderPixelWeightMinimos= new ArrayList<PixelHCL>();
	    	tamanho=orderPixelWeight.size();
	    	int xMedio;
	    	int yMedio;
	    	ConverterLoewner converterL = new ConverterLoewner();
	    	PixelHCL Min;
	    	orderPixelWeightClone = orderPixelWeight;
	    	
	    	while (orderPixelWeightMinimos.size()<tamanho){
	    		Iterator<PixelHCL> it=orderPixelWeightClone.iterator();
	    		Min = medianNew(orderPixelWeightClone);
	    		
				while (it.hasNext()){
					PixelHCL recorrerLista=it.next();
					xMedio = Min.getPosicionX();
			    	yMedio = Min.getPosicionY();
					
					if (recorrerLista.getPosicionX()==xMedio && recorrerLista.getPosicionY()==yMedio)
						continue;
				
			    	matrizLoewnerA_B =resta(recorrerLista.getHclColorMatrizSimetrica(),Min.getHclColorMatrizSimetrica());
			    	
			    	if(semidefinidaPositiva(matrizLoewnerA_B)) {
			    		Min = recorrerLista;
					}else{
						matrizLoewnerA_B =resta(Min.getHclColorMatrizSimetrica(),recorrerLista.getHclColorMatrizSimetrica());
						if(semidefinidaPositiva(matrizLoewnerA_B)) {
							Min = ordenLexicografico(Min,recorrerLista);
						}
					}
				}//cierre del while
				orderPixelWeightMinimos.add(Min);
				orderPixelWeightClone.remove(Min);
				
	    	}
	    	
	    	return orderPixelWeightMinimos;
	    	
	    }
	    
	    
	    public PixelHCL medianNew(List<PixelHCL> orderPixelLoewner) {
	        int element = (int) Math.ceil(orderPixelLoewner.size() / 2);
	        return orderPixelLoewner.get(element);
	    }
	    
	    
	


	private float [][] resta (float [][] A, float [][] B){
		
		float [][] resta = new float [2][2];		
		for(int i=0; i<2; i++) 
			for(int j=0; j< 2; j++) 
				resta[i][j]=A[i][j]-B[i][j]; 

		return resta;
	}
	
	
	public boolean semidefinidaPositiva(float [][] matriz){
		
		if(matriz[0][0]<0){
			return false;
		}
		
		if((matriz[0][0] * matriz[1][1] - matriz[1][0] * matriz[0][1]) >=0F ){
			return true;
		}
		return false;
	}

	
	public PixelHCL ordenLexicografico(PixelHCL color1, PixelHCL color2){
		int[] colorRgb1;
    	int[] colorRgb2;
    	
    	ConverterLoewner converterL = new ConverterLoewner();
    	colorRgb1 = converterL.mat2x2ToRgb(color1.getHclColorMatrizSimetrica());
    	colorRgb2 = converterL.mat2x2ToRgb(color2.getHclColorMatrizSimetrica());
    	
    	
		
	    for (int i = 0; i < colorRgb2.length; i++) {
	        if ( colorRgb1[i] < colorRgb2[i] ) {
	            return color1;
	        } else if ( colorRgb1[i] > colorRgb2[i] ) {
	            return color2;
	        }
	    }
	    
	    return color1;
	}
	
}
