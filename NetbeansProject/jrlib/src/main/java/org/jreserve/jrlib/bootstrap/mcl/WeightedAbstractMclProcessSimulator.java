package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class WeightedAbstractMclProcessSimulator extends AbstractMclProcessSimulator {
    
    private double[][] paid;
    private double[][] incurred;
    
    protected WeightedAbstractMclProcessSimulator(LinkRatioScale scales, boolean isPaid) {
        super(scales, isPaid);
    }

    @Override
    public void setBundle(MclBootstrapEstimateBundle bundle) {
        super.setBundle(bundle);
    }
    
    @Override
    protected double getVariance(int accident, int development) {
        int d = development - 1;
        double cik = getClaim(accident, d);
        double scale = getScale(accident, d);
        return cik * scale * scale;
    }
    
    private double getClaim(int accident, int development) {
        if(paid == null) {
            paid = bundle.getPaidValues();
            incurred = bundle.getIncurredValues();
        }
        
        double p = paid[accident][development];
        double i = incurred[accident][development];
        double cik = p * i;
        return Math.sqrt(cik<0d? -cik : cik);
    }    
}
