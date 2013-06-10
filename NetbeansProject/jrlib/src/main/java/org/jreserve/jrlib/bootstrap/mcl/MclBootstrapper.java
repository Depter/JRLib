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
package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.bootstrap.Bootstrapper;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclPseudoData;
import org.jreserve.jrlib.estimate.Estimate;

/**
 * Bootstrapper for MCL-bootstrapping. The class keeps trak of both
 * the incurred and paid reserves.
 * 
 * The MCL-bootsrap method extends the Mack-bootstrap method, by including
 * the Paid/Incurred and Incurred/Paid ratios. In order to preserve
 * the correlation between paid and incurred claims, the residuals of the
 * four residual triangle (link-ratios and claim-ratios) are linked together.
 * This means that if cell (1,1) in the paid link-ratio triangle got
 * it's residual from cell(2,3), the all other residuals will get their
 * residuals from the same cell.
 * 
 * @see "Liu, Verrall [2010]: Bootstrap Estimation of the Predictive Distributions of Reserves Using Paid and Incurred Claims, Variance 4:2, 2010, pp. 121-135."
 * @author Peter Decsi
 * @version 1.0
 */
public class MclBootstrapper extends Bootstrapper<MclPseudoData> {
    
    private MclBootstrapEstimateBundle bundle;
    protected final double [][] paidReserves;
    protected final double [][] incurredReserves;
    protected final double [][] paidIncurredReserves;
    private int iteration = 0;
    
    /**
     * Creates in instance, which will bootstrap the given source.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws IllegalArgumentException if `bootstrapCount` is less then 1.
     */
    public MclBootstrapper(MclBootstrapEstimateBundle bundle, int bootstrapCount) {
        super(bundle.getSourcePseudoData(), bootstrapCount);
        this.bundle = bundle;
        paidReserves = new double[bootstrapCount][];
        incurredReserves = new double[bootstrapCount][];
        paidIncurredReserves = new double[bootstrapCount][];
    }
    
    @Override
    protected void collectBootstrapResult() {
        paidReserves[iteration] = bundle.getPaidReserves();
        incurredReserves[iteration] = bundle.getIncurredReserves();
        paidIncurredReserves[iteration] = bundle.getPaidIncurredReserves();
        iteration++;
    }
    
    /**
     * Retunrs the paid pseudo reserves. Modifiying the returned array
     * will change the state of this instance.
     */
    public double[][] getPaidReserves() {
        return paidReserves;
    }
    
    /**
     * Retunrs the incurred pseudo reserves. Modifiying the returned array
     * will change the state of this instance.
     */
    public double[][] getIncurredReserves() {
        return incurredReserves;
    }
    
    /**
     * Returns a second variant for the pseudo incurred reserves. The reserve
     * is calculated as the difference of the incurred ultimate and the
     * last observed paid claim.
     */
    public double[][] getPaidIncurredReserves() {
        return paidIncurredReserves;
    }
}
