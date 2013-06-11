/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.estimate;

import org.jreserve.jrlib.AbstractMultiSourceCalculationData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * The average cost methods takes the link-ratios for the number of claims
 * triangle and the cost per claim and combines them to a paid triangle estimate.
 * If the inputs have different dimensions, then always the smaller one is choosen.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class AverageCostEstimate extends AbstractTriangleEstimate<AverageCostEstimate.AverageCostEstimateInput> {
    
    /**
     * Creates a new instance for the given input.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public AverageCostEstimate(LinkRatio numberLrs, LinkRatio costLrs) {
        this(new AverageCostEstimateInput(numberLrs, costLrs));
    }
    
    /**
     * Creates a new instance for the given input.
     * 
     * @throws NullPointerException if `input` is null.
     */
    public AverageCostEstimate(AverageCostEstimateInput input) {
        super(input, input.numberCik);
        super.recalculateLayer();
    }

    public LinkRatio getSourceCostLinkRatios() {
        return source.costLrs;
    }
    
    public LinkRatio getSourceNumberLinkRatios() {
        return source.numberLrs;
    }
    
    private double[][] numbers;
    private double[][] costs;
    
    @Override
    protected void initDimensions() {
        if(source == null) {
            accidents = 0;
            developments = 0;
        } else {
            initAccidents();
            initDevelopments();
            initValues();
        }
    }
    
    private void initAccidents() {
        int numberAccidents = source.numberCik.getAccidentCount();
        int costAccidents = source.costCik.getAccidentCount();
        accidents = (numberAccidents < costAccidents)? numberAccidents : costAccidents;
    }
    
    private void initDevelopments() {
        int numberDevs = source.numberLrs.getLength();
        int costDevs = source.costLrs.getLength();
        developments = (numberDevs < costDevs)? numberDevs : costDevs;
        developments++;
    }    
    
    private void initValues() {
        numbers = EstimateUtil.completeTriangle(source.numberCik, source.numberLrs);
        costs = EstimateUtil.completeTriangle(source.costCik, source.costLrs);
    }
    
    @Override
    protected double getObservedValue(int accident, int development) {
        return getEstimatedValue(accident, development);
    }
    
    @Override
    protected double getEstimatedValue(int accident, int development) {
        return numbers[accident][development] * costs[accident][development];
    }
    
    @Override
    protected void cleanUpCalculation() {
        numbers = null;
        costs = null;
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
    }
}
