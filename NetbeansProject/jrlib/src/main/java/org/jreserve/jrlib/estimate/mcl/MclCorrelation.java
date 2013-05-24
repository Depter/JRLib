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
package org.jreserve.jrlib.estimate.mcl;

import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;

/**
 * MclCorrelation calculates the correlation coefficient between
 * the link- and the claim-ratio residuals.
 * 
 * The following formula is used:
 *     corr = sum(f * q) / sum(q * q)
 * where:
 * -   `corr` is the correlation.
 * -   `sum()` mean an iteration for all accident and development periods.
 * -   `f` is the residual for the link-ratio for a given accident and
 *     development period.
 * -   `q` is the residual for the claim-ratio for a given accident and
 *     development period.
 * 
 * If one of `f` or `q` is a `NaN`, than the cell is ignored. If 
 * `sum(q * q)` is 0, or the input is empty, the result is `NaN`.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class MclCorrelation extends AbstractCalculationData<MclCorrelationInput> {

    private double correlation;
    
    /**
     * Creates a new instance from the given input.
     * 
     * @see ClaimRatioResiduals#ClaimRatioResiduals(ClaimRatioScale) 
     * @see LinkRatioResiduals#LinkRatioResiduals(LinkRatioScale) 
     * @see MclCorrelationInput#MclCorrelationInput(LRResidualTriangle, CRResidualTriangle) 
     * @throws NullPointerException if one of the parameters is null.
     */
    public MclCorrelation(LinkRatioScale lrScales, ClaimRatioScale crScales) {
        this(new LinkRatioResiduals(lrScales), new ClaimRatioResiduals(crScales));
    }
    
    /**
     * Creates a new instance from the given residual triangles.
     * 
     * @see MclCorrelationInput#MclCorrelationInput(LRResidualTriangle, CRResidualTriangle) 
     * @throws NullPointerException if one of the parameters is null.
     */
    public MclCorrelation(LRResidualTriangle lrResiduals, CRResidualTriangle crResiduals) {
        this(new MclCorrelationInput(lrResiduals, crResiduals));
    }
    
    /**
     * Creates a new instance from the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public MclCorrelation(MclCorrelationInput source) {
        super(source);
        doRecalculate();
    }
    
    /**
     * Returns the input used for the calculation.
     */
    public MclCorrelationInput getSourceInput() {
        return source;
    }
    
    /**
     * Returns the residual triangle for the link-ratios.
     */
    public LRResidualTriangle getSourceLinkRatioResiduals() {
        return source.getSourceLinkRatioResiduals();
    }
    
    /**
     * Returns the scale parameters for the link-rarios.
     */
    public LinkRatioScale getSourceLinkRatioScaless() {
        return source.getSourceLinkRatioScaless();
    }
    
    /**
     * Returns the link-ratios.
     */
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }
    
    /**
     * Returns the development factors, used to calculate the
     * link-ratios.
     */
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }
    
    /**
     * Returns the claim triangle, used to calculate the link-ratios. The value 
     * returned from this method is the same as the one returned from 
     * {@link #getSourceDenominatorTriangle() getSourceDenominatorTriangle}.
     */
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
    
    /**
     * Returns the residual triangle for the claim-ratios.
     */
    public CRResidualTriangle getSourceClaimRatioResidualTriangle() {
        return source.getSourceClaimRatioResiduals();
    }
    
    /**
     * Returns the scale parameters for the claim-rarios.
     */
    public ClaimRatioScale getSourceClaimRatioScales() {
        return source.getSourceClaimRatioScales();
    }
    
    /**
     * Returns the claim-ratios.
     */
    public ClaimRatio getSourceClaimRatios() {
        return source.getSourceClaimRatios();
    }
    
    /**
     * Returns the ratio triangle, used to calculate the
     * claim-ratios.
     */
    public RatioTriangle getSourceRatioTriangle() {
        return source.getSourceRatioTriangle();
    }
    
    /**
     * Returns the triangle used as denominator for the claim-ratios. The value 
     * returned from this method is the same as the one returned from 
     * {@link #getSourceTriangle() getSourceTriangle}.
     */
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }
    
    /**
     * Returns the triangle used as numerator for the claim-ratios.
     */
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }
    
    /**
     * Returns the correlation coefficient.
     */
    public double getValue() {
        return correlation;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        double sn = 0d;
        double sd = 0d;
        
        int accidents = source.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double f = source.getLinkRatioResidual(a, d);
                double q = source.getClaimRatioResidual(a, d);
                
                if(!Double.isNaN(f) && !Double.isNaN(q)) {
                    sn += f * q;
                    sd += q * q;
                }
            }
        }
        
        correlation = sd==0d? Double.NaN : sn / sd; 
    }
}
