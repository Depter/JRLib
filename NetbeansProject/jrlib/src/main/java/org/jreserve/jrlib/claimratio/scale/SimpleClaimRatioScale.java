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
package org.jreserve.jrlib.claimratio.scale;

import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.scale.Scale;
import org.jreserve.jrlib.scale.ScaleEstimator;
import org.jreserve.jrlib.scale.SimpleScale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;

/**
 * Basic implementation for the {@link ClaimRatioScale ClaimRatioScale} interface
 * which fills in the missing values (NaNs) with a preset
 * {@link ScaleEstimator ScaleEstimator}.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleClaimRatioScale extends SimpleScale<ClaimRatioScaleInput> implements ClaimRatioScale {

    private ClaimRatioScaleInput sourceInput;
    
    /**
     * Creates an instance for the given source.
     * 
     * @see ClaimRatioScaleInput#ClaimRatioScaleInput(ClaimRatio) 
     * @see #SimpleClaimRatioScale(ClaimRatioScaleInput) 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleClaimRatioScale(ClaimRatio source) {
        this(new ClaimRatioScaleInput(source));
    }
    
    /**
     * Creates an instance for the given source and estimator.
     * 
     * @see ClaimRatioScaleInput#ClaimRatioScaleInput(ClaimRatio) 
     * @see #SimpleClaimRatioScale(ClaimRatioScaleInput, ScaleEstimator) 
     * @throws NullPointerException if `source` or `estimator` is null.
     */
    public SimpleClaimRatioScale(ClaimRatio source, ScaleEstimator<ClaimRatioScaleInput> estimator) {
        this(new ClaimRatioScaleInput(source), estimator);
    }
    
    /**
     * Creates an instance for the given source.
     * 
     * @see SimpleScale#SimpleScale(org.jreserve.jrlib.scale.ScaleInput) 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleClaimRatioScale(ClaimRatioScaleInput source) {
        super(source);
        this.sourceInput = source;
    }

    /**
     * Creates an instance for the given source and estimator.
     * 
     * @see SimpleScale#SimpleScale(ScaleInput, ScaleEstimator) 
     * @throws NullPointerException if `source` or `estimator` is null.
     */
    public SimpleClaimRatioScale(ClaimRatioScaleInput source, ScaleEstimator<ClaimRatioScaleInput> estimator) {
        super(source, estimator);
        this.sourceInput = source;
    }

    /**
     * Creates an instance for the given source.
     * 
     * @see SimpleScale#SimpleScale(org.jreserve.jrlib.scale.Scale) 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleClaimRatioScale(Scale<ClaimRatioScaleInput> source) {
        super(source);
        this.sourceInput = source.getSourceInput();
    }

    /**
     * Creates an instance for the given source and estimator.
     * 
     * @see SimpleScale#SimpleScale(Scale, ScaleEstimator) 
     * @throws NullPointerException if `source` or `estimator` is null.
     */
    public SimpleClaimRatioScale(Scale<ClaimRatioScaleInput> source, ScaleEstimator<ClaimRatioScaleInput> estimator) {
        super(source, estimator);
        this.sourceInput = source.getSourceInput();
    }
    
    /**
     * Do not get the length from the source.
     */
    @Override
    protected void initCalculation() {
    }
    
    @Override
    public int getLength() {
        return source.getLength();
    }
    
    @Override
    public ClaimRatio getSourceClaimRatios() {
        return sourceInput.getSourceClaimRatios();
    }

    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return sourceInput.getSourceRatioTriangle();
    }
    
    @Override
    public RatioTriangleInput getSourceRatioTriangleInput() {
        return sourceInput.getSourceRatioTriangle().getSourceInput();
    }

    @Override
    public ClaimTriangle getSourceNumeratorTriangle() {
        return sourceInput.getSourceNumeratorTriangle();
    }

    @Override
    public ClaimTriangle getSourceDenominatorTriangle() {
        return sourceInput.getSourceDenominatorTriangle();
    }

}
