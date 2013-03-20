package org.jreserve.estimate;

import org.jreserve.linkratio.LinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AverageCostEstimate extends AbstractEstimate<LinkRatio> {
    
    private final static int NUMBERS = 0;
    private final static int COSTS = 1;
    
    private LinkRatio numberLrs;
    private ClaimTriangle numberCik;
    private LinkRatio costLrs;
    private ClaimTriangle costCik;
    
    public AverageCostEstimate(LinkRatio numberLrs, LinkRatio costLrs) {
        super(numberLrs, costLrs);
        initSources();
        doRecalculate();
    }
    
    private void initSources() {
        this.numberLrs = sources[NUMBERS];
        this.numberCik = numberLrs.getSourceTriangle();
        this.costLrs = sources[COSTS];
        this.costCik = costLrs.getSourceTriangle();
    }
    
    @Override
    public int getObservedDevelopmentCount(int accident) {
        return numberCik.getDevelopmentCount(accident);
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
    
    @Override
    public AverageCostEstimate copy() {
        return new AverageCostEstimate(numberLrs.copy(), costLrs.copy());
    }
}
