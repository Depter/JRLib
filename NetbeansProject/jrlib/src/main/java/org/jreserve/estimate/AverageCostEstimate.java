package org.jreserve.estimate;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AverageCostEstimate extends AbstractEstimate {
    
    private LinkRatio numberLrs;
    private Triangle numberCik;
    private LinkRatio costLrs;
    private Triangle costCik;
    
    public AverageCostEstimate(LinkRatio numberLrs, LinkRatio costLrs) {
        initSources(numberLrs, costLrs);
        attachSources();
        doRecalculate();
    }
    
    private void initSources(LinkRatio nLrs, LinkRatio cLrs) {
        this.numberLrs = nLrs;
        this.numberCik = nLrs.getSourceFactors().getSourceTriangle();
        this.costLrs = cLrs;
        this.costCik = cLrs.getSourceFactors().getSourceTriangle();
    }
    
    private void attachSources() {
        attachSource(numberLrs);
        attachSource(costLrs);
    }
    
    @Override
    protected int getObservedDevelopmentCount(int accident) {
        return numberCik.getDevelopmentCount(accident);
    }

    @Override
    protected void recalculateSource() {
        recalculateSource(numberLrs);
        recalculateSource(costLrs);
    }

    @Override
    protected void detachSource() {
        detachSource(numberLrs);
        detachSource(costLrs);
    }

    public LinkRatio getSourceCostLinkRatios() {
        return costLrs;
    }
    
    public LinkRatio getSourceNumberLinkRatios() {
        return numberLrs;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initValues();
        double[][] number = EstimateUtil.completeTriangle(numberCik, numberLrs);
        double[][] cost = EstimateUtil.completeTriangle(costCik, costLrs);
        
        for(int a=0; a<accidents; a++)
            for(int d=0; d<developments; d++)
                values[a][d] = number[a][d] * cost[a][d];
    }
    
    private void initValues() {
        initAccidents();
        initDevelopments();
        values = new double[accidents][developments];
    }
    
    private void initAccidents() {
        int numberAccidents = numberCik.getAccidentCount();
        int costAccidents = costCik.getAccidentCount();
        accidents = (numberAccidents < costAccidents)? numberAccidents : costAccidents;
    }
    
    private void initDevelopments() {
        int numberDevs = numberLrs.getDevelopmentCount();
        int costDevs = costLrs.getDevelopmentCount();
        developments = (numberDevs < costDevs)? numberDevs : costDevs;
        developments++;
    }
}
