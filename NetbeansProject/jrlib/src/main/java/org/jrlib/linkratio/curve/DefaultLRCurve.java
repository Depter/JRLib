package org.jrlib.linkratio.curve;

import org.jrlib.linkratio.LinkRatio;

/**
 * This class simply mirrors the fitted {@link LinkRatio LinkRatio}. 
 * For development periods, where there is no input link ratio returns 
 * NaN.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLRCurve implements LinkRatioCurve {
    
    private double[] lrs;
    private int developments;
    
    @Override
    public void fit(LinkRatio lr) {
        this.lrs = lr.toArray();
        this.developments = lrs.length;
    }

    @Override
    public double getValue(int development) {
        if(development >= 0 && development < developments)
            return lrs[development];
        return Double.NaN;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof DefaultLRCurve);
    }
    
    @Override
    public int hashCode() {
        return DefaultLRCurve.class.hashCode();
    }

    @Override
    public String toString() {
        return "DefaultLRCurve";
    }
}
