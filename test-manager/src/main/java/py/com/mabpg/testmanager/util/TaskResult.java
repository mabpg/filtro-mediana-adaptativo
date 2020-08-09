/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.mabpg.testmanager.util;

import ij.process.ColorProcessor;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import py.com.mabpg.tesisrgb.generics.BasicFilterAbstract;

/**
 *
 * @author Derlis Argüello
 */
public class TaskResult {
    private BasicFilterAbstract basicAbstract;
    private ColorProcessor colProcessor;
    private double indice;
    private double varianza;
    private int ventanas;

    public void setBasicAbstract(BasicFilterAbstract basicAbstract) {
        this.basicAbstract = basicAbstract;
    }

    public void setIndice(double indice) {
        this.indice = indice;
    }

    public void setVarianza(double varianza) {
        this.varianza = varianza;
    }

    public void setVentanas(int ventanas) {
        this.ventanas = ventanas;
    }

    public double getIndice() {
        return indice;
    }

    public double getVarianza() {
        return varianza;
    }

    public int getVentanas() {
        return ventanas;
    }
    

    public TaskResult(BasicFilterAbstract basicAbstractt, double indice, double varianza, int ventanas) {
        this.indice = indice;
        this.varianza = varianza;
        this.basicAbstract = basicAbstractt;
        this.ventanas = ventanas;
    }

    public ColorProcessor getColProcessor() {
        return colProcessor;
    }

    public void setColProcessor(ColorProcessor colProcessor) {
        this.colProcessor = colProcessor;
    }

    public BasicFilterAbstract getBasicAbstract() {
        return basicAbstract;
    }
    
    @Override
    public String toString() {
        try {
            //usamos reflexion porque no puedo obtener los datos de la clase actual en caso contrario
            Field filterNameF = basicAbstract.getClass().getField("filterName");
            Field decisionValorReducidoF = basicAbstract.getClass().getField("reducedValue");
            Field decisionCompF = basicAbstract.getClass().getField("decisionByComp");
            
            String filterName = (String)filterNameF.get( basicAbstract );
            double decisionValorReducido = (double)decisionValorReducidoF.get( basicAbstract );
            double[] decisionComp = (double[])decisionCompF.get( basicAbstract );

            return indice + ", " + varianza + ", " + filterName + ", " +  ventanas;
        } catch (SecurityException ex) {
            Logger.getLogger(TaskResult.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TaskResult.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TaskResult.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(TaskResult.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}