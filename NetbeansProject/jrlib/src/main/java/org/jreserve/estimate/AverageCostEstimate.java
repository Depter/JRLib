package org.jreserve.estimate;

import org.jreserve.AbstractMultiSourceCalculationData;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AverageCostEstimate extends AbstractEstimate<AverageCostEstimate.AverageCostEstimateInput> {
    
    public AverageCostEstimate(LinkRatio numberLrs, LinkRatio costLrs) {
        this(new AverageCostEstimateInput(numberLrs, costLrs));
    }
    
    public AverageCostEstimate(AverageCostEstimateInput input) {
        super(input);
        doRecalculate();
    }
    
    @Override
    public int getObservedDevelopmentCount(int accident) {
        return source.numberCik.getDevelopmentCount(accident);
    }

    public LinkRatio getSourceCostLinkRatios() {
        return source.costLrs;
    }
    
    public LinkRatio getSourceNumberLinkRatios() {
        return source.numberLrs;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initValues();
        double[][] number = EstimateUtil.completeTriangle(source.numberCik, source.numberLrs);
        double[][] cost = EstimateUtil.completeTriangle(source.costCik, source.costLrs);
        
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
        int numberAccidents = source.numberCik.getAccidentCount();
        int costAccidents = source.costCik.getAccidentCount();
        accidents = (numberAccidents < costAccidents)? numberAccidents : costAccidents;
    }
    
    private void initDevelopments() {
        int numberDevs = source.numberLrs.getDevelopmentCount();
        int costDevs = source.costLrs.getDevelopmentCount();
        developments = (numberDevs < costDevs)? numberDevs : costDevs;
        developments++;
    }
    
    @Override
    public AverageCostEstimate copy() {
        return new AverageCostEstimate(source.copy());
    }
    
    public static class AverageCostEstimateInput extends AbstractMultiSourceCalculationData<LinkRatio> {

        private final LinkRatio numberLrs;
        private final ClaimTriangle numberCik;
        private final LinkRatio costLrs;
        private final ClaimTriangle costCik;
        
        public AverageCostEstimateInput(LinkRatio numberLrs, LinkRatio costLrs) {
            super(numberLrs, costLrs);
            this.numberLrs = numberLrs;
            this.numberCik = numberLrs.getSourceTriangle();
            this.costLrs = costLrs;
            this.costCik = costLrs.getSourceTriangle();
        }
        
        @Override
        protected void recalculateLayer() {
        }
        
        private AverageCostEstimateInput copy() {
            return new AverageCostEstimateInput(
                    numberLrs.copy(), 
                    costLrs.copy());
        }
    }
}
