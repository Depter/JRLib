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
package org.jreserve.jrlib.bootstrap.mcl.pseudodata;

import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScaleInput;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;
import org.jreserve.jrlib.vector.AbstractVector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclPseudoClaimRatioScale extends AbstractVector<ClaimRatioScale> implements ClaimRatioScale {
    
    static MclPseudoClaimRatioScale createPaid(MclResidualBundle bundle) {
        return create(bundle.getSourcePaidCRResidualTriangle());
    }
    
    static MclPseudoClaimRatioScale createIncurred(MclResidualBundle bundle) {
        return create(bundle.getSourceIncurredCRResidualTriangle());
    }
    
    private static MclPseudoClaimRatioScale create(CRResidualTriangle source) {
        return new MclPseudoClaimRatioScale(source.getSourceClaimRatioScales());
    }
    
    private double[] originals;
    private int originalLength;
    
    private MclPseudoClaimRatioScale(ClaimRatioScale original) {
        super(original);
        originals = original.toArray();
        originalLength = originals.length;
    }
    
    @Override
    public ClaimRatio getSourceClaimRatios() {
        return source.getSourceClaimRatios();
    }

    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return source.getSourceRatioTriangle();
    }

    @Override
    public RatioTriangleInput getSourceRatioTriangleInput() {
        return source.getSourceRatioTriangleInput();
    }

    @Override
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }

    @Override
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }

    @Override
    public ClaimRatioScaleInput getSourceInput() {
        return source.getSourceInput();
    }
    
    @Override
    public int getLength() {
        return source.getLength();
    }

    @Override
    public double getValue(int index) {
        double v = source.getValue(index);
        return (!Double.isNaN(v) || index<0 || index >= originalLength)?
                v :
                originals[index];
    }

    @Override
    protected void recalculateLayer() {
    }
}
