/*
 * Tesis Arguello Balbuena
 * Derechos Reservados 2015 - 2016
 */
package py.com.mabpg.tesisrgb.models;

import java.util.Comparator;

/**
 *
 * @author daasalbion
 */
public class TesisComparator implements Comparator<PixelWeight2>{
    
    //cantidad de veces que se opta por canal
    public long [] chooseChannel;
    public long valorReducido = 0;
    
    public TesisComparator(int cSize) {
        this.chooseChannel = new long[cSize];
    }

    @Override
    public int compare(PixelWeight2 o1, PixelWeight2 o2) {
        if (o1.getElemento() == o2.getElemento()) {
            //pesos iguales, se va a decidir en el lexicografico
            int [] color1 = o1.getPixel();
            int [] color2 = o2.getPixel();

            for (int i = 0; i < color2.length; i++) {
                if ( color1[i] < color2[i] ) {
                    chooseChannel[i]++;
                    return -1;
                } else if ( color1[i] > color2[i] ) {
                    chooseChannel[i]++;
                    return 1;
                }
            }
            return 0;
        }else if ( o1.getElemento() < o2.getElemento() ) {
            valorReducido++;
            return -1;
        } else {
            valorReducido++;
            return  1;
        }
    }   
}