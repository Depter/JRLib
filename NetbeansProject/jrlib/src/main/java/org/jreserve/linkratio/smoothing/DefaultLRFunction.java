package org.jreserve.linkratio.smoothing;

import org.jreserve.linkratio.LinkRatio;

/**
 * This class simply mirrors the fitted {@link LinkRatio LinkRatio}, and
 * for development periods, where there is no input link ratio, simply returns
 * 1.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLRFunction implements LinkRatioFunction {
    
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
        return 1d;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof DefaultLRFunction);
    }
    
    @Override
    public int hashCode() {
        return DefaultLRFunction.class.hashCode();
    }

    @Override
    public String toString() {
        return "DefaultLR";
    }
    
    @Override
    public DefaultLRFunction copy() {
        return new DefaultLRFunction();
    }
}
