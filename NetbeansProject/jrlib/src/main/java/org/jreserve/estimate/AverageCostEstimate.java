package org.jreserve.estimate;

import org.jreserve.CalculationData;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AverageCostEstimate extends AbstractEstimate {
    
    private LinkRatio nLrs;
    private Triangle nCik;
    private LinkRatio cLrs;
    private Triangle cCik;
    
    public AverageCostEstimate(LinkRatio nLrs, LinkRatio cLrs) {
        initSources(nLrs, cLrs);
        attachSources();
    }
    
    private void initSources(LinkRatio nLrs, LinkRatio cLrs) {
        this.nLrs = nLrs;
        this.nCik = nLrs.getInputFactors().getInputTriangle();
        this.cLrs = cLrs;
        this.cCik = cLrs.getInputFactors().getInputTriangle();
    }
    
    private void attachSources() {
        attachSource(nLrs);
        attachSource(cLrs);
    }
    
    private void checkSources() {
        //TODO check sources
    }
    
    @Override
    protected int getObservedDevelopmentCount(int accident) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void recalculateSource() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void recalculateLayer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void detachSource() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CalculationData getSource() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
