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
 * The class calculates the Cape-Cod reserve estimates. The Cape-Cod
 * method estimates the expected loss-ratio kappa based on an exposure 
 * and link-ratios. Kappa is calculated as follows:
 *             sum(S(a))
 *      k = ----------------
 *          sum(E(a) * g(a))
 * where:
 * -   `S(a)` is the last observed claim for development period `a`.
 * -   `E(a)` is the exposure for development period `a`.
 * -   `g(a)` is the {@link EstimateUtil#getCompletionRatios(LinkRatio)  completion-ratio} 
 *     for the development period, that belongs to `S(a)`.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class CapeCodEstimate extends AbstractTriangleEstimate<CapeCodEstimateInput> {

    /**
     * Creates an instance for the given input.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public CapeCodEstimate(LinkRatio lrs, Vector exposure) {
        this(new CapeCodEstimateInput(lrs, exposure));
    }
    
    /**
     * Creates an instance for the given input.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public CapeCodEstimate(CapeCodEstimateInput source) {
        super(source, source.getSourceLinkRatio().getSourceTriangle());
        super.recalculateLayer();
    }
    
    public CapeCodEstimateInput getSource() {
        return source;
    }
    
    private double[] gammas;
    private double kappa;
    private double ultimate;
    
    @Override
    protected void initDimensions() {
        if(source == null)
            initDimensionsFromEmpty();
        else
            initDimensionsFromSource();
    }
    
    private void initDimensionsFromEmpty() {
        accidents = 0;
        developments = 0;
    }
    
    private void initDimensionsFromSource() {
        accidents = triangle.getAccidentCount();
        developments = source.getDevelopmentCount() + 1;
        gammas = EstimateUtil.getCompletionRatios(source.getSourceLinkRatio());
        kappa = getKappa(gammas);
    }

    private double getKappa(double[] gammas) {
        int gammaLength = gammas.length;
        double sumS = 0d;
        double sumEG = 0d;
        
        for(int a=0; a<accidents; a++) {
            int dev = triangle.getDevelopmentCount(a) - 1;
            
            sumS += triangle.getValue(a, dev);
            double gamma = (dev<0)? Double.NaN : dev >= gammaLength? 1d : gammas[dev];
            
            sumEG += (gamma * source.getExposure(a));
        }
        
        return (sumEG == 0d)? Double.NaN : (sumS / sumEG);
    }
    
    @Override
    protected void fillAccident(int accident) {
        ultimate = source.getExposure(accident) * kappa;
        super.fillAccident(accident);
    }
    
    @Override
    protected double getEstimatedValue(int a, int d) {
        return ((d+1)==developments? 1d: gammas[d]) * ultimate;
    }
}