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
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.vector.Vector;

/**
 * LossRatioEstimateInput is the common calculation source for 
 * {@link Estimate Estimates} based on loss-ratio principles. It's simply
 * bundles the {@link LinkRatio LinkRatios} and the {@link Vector Vectos}
 * containing the exposure and the loss-ratios.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class LossRatioEstimateInput extends AbstractMultiSourceCalculationData<CalculationData> {

    private final LinkRatio lrs;
    private final Vector exposure;
    private final Vector lossRatio;
    
    /**
     * Creates an instance with the given input.
     * 
     * @throws NullPointerException if either of the parameters is null.
     */
    public LossRatioEstimateInput(LinkRatio lrs, Vector exposure, Vector lossRatio) {
        super(lrs, exposure, lossRatio);
        this.lrs = lrs;
        this.exposure = exposure;
        this.lossRatio = lossRatio;
    }
    
    /**
     * Returns the number of development periods.
     * 
     * @see LinkRatio#getLength() 
     */
    public int getDevelopmentCount() {
        return lrs.getLength();
    }
    
    /**
     * Returns the {@link Vector Vector} containing the exposures
     * for each accident period.
     */
    public Vector getSourceExposure() {
        return exposure;
    }
    
    /**
     * Returns the exposure for the given accident period.
     * 
     * @see Vector#getValue(int) 
     */
    public double getExposure(int accident) {
        return exposure.getValue(accident);
    }
    
    /**
     * Returns the {@link LinkRatio LinkRatio} containing the link-ratios.
     */
    public LinkRatio getSourceLinkRatio() {
        return lrs;
    }
    
    /**
     * Returns the link-ratio for the given development period.
     * 
     * @see LinkRatio#getValue(int) 
     */
    public double getLinkRatio(int development) {
        return lrs.getValue(development);
    }
    
    /**
     * Returns the {@link Vector Vector} containing the loss-ratios
     * for each accident period.
     */
    public Vector getSourceLossRatio() {
        return lossRatio;
    }
    
    /**
     * Returns the loss-ratio for the given accident period.
     * 
     * @see Vector#getValue(int) 
     */
    public double getLossRatio(int accident) {
        return lossRatio.getValue(accident);
    }
    
    /**
     * Does nothing.
     */
    @Override
    protected void recalculateLayer() {
    }
}