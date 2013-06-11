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
 * The class calculates the expected loss-ratio reserve estimates. The 
 * cells of the input claim triangle `C(a,d)` are calculated as:
 *       C(a,d) = E(a) * ELR(a) * g(d)
 * where:
 * -   `C(a,d)` is the estimated claims for accident period `a` and
 *     development period `d`.
 * -   `E(a)` is the exposure for accident period `a`.
 * -   `ELR(a)` is the expected loss-ratio for accident period `a`.
 * -   `g(d)` is the {@link EstimateUtil#getCompletionRatios(LinkRatio) complaetion-ratio}
 *     for development period `d`.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExpectedLossRatioEstimate extends AbstractTriangleEstimate<LossRatioEstimateInput> {

    public ExpectedLossRatioEstimate(LinkRatio lrs, Vector exposure, Vector lossRatio) {
        this(new LossRatioEstimateInput(lrs, exposure, lossRatio));
    }
    
    public ExpectedLossRatioEstimate(LossRatioEstimateInput source) {
        super(source, source.getSourceLinkRatio().getSourceTriangle());
        super.recalculateLayer();
    }

    public LossRatioEstimateInput getSource() {
        return source;
    }
    
    private double[] quotas;
    private double ultimate;
    
    @Override
    protected void initDimensions() {
        if(source == null) {
            accidents = 0;
            developments = 0;
        } else {
            accidents = triangle.getAccidentCount();
            developments = source.getDevelopmentCount()+1;
            calculateQuotas();
        }
    }
    
    private void calculateQuotas() {
        quotas = new double[developments];
        
        quotas[developments-1] = 1d;
        for(int d=(developments-2); d>=0; d--) {
            double lr = source.getLinkRatio(d);
            quotas[d] = (lr == 0d)? Double.NaN : quotas[d+1] / lr;
        }
    }
    
    @Override
    protected void fillAccident(int accident) {
        ultimate = source.getExposure(accident) * source.getLossRatio(accident);
        super.fillAccident(accident);
    }
    
    @Override
    protected double getEstimatedValue(int accident, int development) {
        return ultimate * quotas[development];
    }
    
    @Override
    protected void cleanUpCalculation() {
        quotas = null;
    }
}
