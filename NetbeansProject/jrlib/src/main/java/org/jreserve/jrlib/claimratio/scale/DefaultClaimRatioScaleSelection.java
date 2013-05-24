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
import org.jreserve.jrlib.scale.*;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultClaimRatioScaleSelection extends DefaultScaleSelection<ClaimRatioScaleInput> implements ClaimRatioScaleSelection {

    private ClaimRatioScaleInput sourceInput;

    public DefaultClaimRatioScaleSelection(ClaimRatio source) {
        this(new ClaimRatioScaleInput(source));
    }
    
    public DefaultClaimRatioScaleSelection(ClaimRatio source, ScaleEstimator<ClaimRatioScaleInput> method) {
        this(new ClaimRatioScaleInput(source), method);
    }

    public DefaultClaimRatioScaleSelection(ClaimRatioScaleInput source) {
        this(source, new DefaultScaleEstimator<ClaimRatioScaleInput>());
    }
    
    public DefaultClaimRatioScaleSelection(ClaimRatioScaleInput source, ScaleEstimator<ClaimRatioScaleInput> method) {
        this(new DefaultScaleCalculator<ClaimRatioScaleInput>(source), method);
    }

    public DefaultClaimRatioScaleSelection(Scale<ClaimRatioScaleInput> source) {
        this(source, new DefaultScaleEstimator<ClaimRatioScaleInput>());
    }

    public DefaultClaimRatioScaleSelection(Scale<ClaimRatioScaleInput> source, ScaleEstimator<ClaimRatioScaleInput> method) {
        super(source, method);
        this.sourceInput = source.getSourceInput();
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
