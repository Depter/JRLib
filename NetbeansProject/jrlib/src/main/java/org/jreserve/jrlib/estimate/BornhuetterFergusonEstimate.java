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

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.vector.Vector;

/**
 * The class calculates the Bornhuetter-Ferguson reserve estimates. The
 * Bornhuetter-Ferguson method allocates the ultimate amounts, alculated
 * from the expected loss-ratios and exposures to development periods
 * based on the link-ratios.
 * 
 * The formulas to calculate the incremental amount for development
 * period `d`:
 *      I(a,d) = U(a) * q(d),
 *      U(a) = E(a) * ELR(a)
 * where:
 * -    `I(a,d)` is the incremental amount for accident period `a` and
 *      development period `d`.
 * -    `U(a)` is the expected ultimate
 * -    `E(a)` is the exposure for accident period `a`.
 * -    `ELR(a)` is the expected loss-ratio for accident period `a`.
 * -    `q(d)` is the rate of the ultimate amount allocated to development
 *      period `d`.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class BornhuetterFergusonEstimate 
    extends AbstractTriangleEstimate<LossRatioEstimateInput> 
{
    
    /**
     * Creates an instance for the given input.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public BornhuetterFergusonEstimate(LinkRatio lrs, Vector exposure, Vector lossRatio) {
        this(new LossRatioEstimateInput(lrs, exposure, lossRatio));
    }
    
    /**
     * Creates an instance for the given input.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public BornhuetterFergusonEstimate(LossRatioEstimateInput source) {
        super(source, source.getSourceLinkRatio().getSourceTriangle());
        super.recalculateLayer();
    }

    public LossRatioEstimateInput getSource() {
        return source;
    }
    
    private double[] quotas;
    private double ultimate;
    private double prev;
    
    @Override
    protected void initDimensions() {
        if(source == null) {
            accidents = 0;
            developments = 0;
        } else {
            accidents = triangle.getAccidentCount();
            developments = source.getDevelopmentCount()+1;
            initValues();
        }
    }
    
    private void initValues() {
        calculateQuotas();
    }
    
    private void calculateQuotas() {
        quotas = new double[developments];
        
        quotas[developments-1] = 1d;
        for(int d=(developments-2); d>=0; d--) {
            double lr = source.getLinkRatio(d);
            quotas[d] = (lr == 0d)? Double.NaN : quotas[d+1] / lr;
            quotas[d+1] = quotas[d+1] - quotas[d];
        }
    }
    
    @Override
    protected void fillAccident(int accident) {
        ultimate = source.getExposure(accident) * source.getLossRatio(accident);
        
        int devs = triangle.getDevelopmentCount(accident);
        prev = triangle.getValue(accident, devs-1);
        super.fillAccident(accident);
    }
    
    @Override
    protected double getEstimatedValue(int accident, int development) {
        prev += ultimate * quotas[development];
        return prev;
    }
}