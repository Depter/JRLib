package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class SimpleAbstractMclProcessSimulator extends AbstractMclProcessSimulator {
    
    private double[][] values;
    
    protected SimpleAbstractMclProcessSimulator(LinkRatioScale scales, boolean isPaid) {
        super(scales, isPaid);
    }

    @Override
    public void setBundle(MclBootstrapEstimateBundle bundle) {
        super.setBundle(bundle);
    }
    
    @Override
    protected double getVariance(int accident, int development) {
        if(values == null)
            values = isPaid? bundle.getPaidValues() : bundle.getIncurredValues();
            
        int d = development - 1;
        double cik = getClaim(accident, d);
        double scale = getScale(accident, d);
        return cik * scale * scale;
    }
    
    private double getClaim(int accident, int development) {
        double cik = values[accident][development];
        return cik < 0d? -cik : cik;
    }    
}
