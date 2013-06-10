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
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;
import org.jreserve.jrlib.vector.AbstractVector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclPseudoClaimRatio extends AbstractVector<ClaimRatio> implements ClaimRatio {
    
    static MclPseudoClaimRatio createPaid(MclResidualBundle bundle) {
        return new MclPseudoClaimRatio(bundle.getSourcePaidCRResidualTriangle().getSourceClaimRatios());
    }
    
    static MclPseudoClaimRatio createIncurred(MclResidualBundle bundle) {
        return new MclPseudoClaimRatio(bundle.getSourceIncurredCRResidualTriangle().getSourceClaimRatios());
    }
    
    private double[] originals;
    private int originalLength;
    
    private MclPseudoClaimRatio(ClaimRatio original) {
        super(original);
        originals = original.toArray();
        originalLength = originals.length;
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
    public double getValue(int index) {
        double v = source.getValue(index);
        return (!Double.isNaN(v) || index<0 || index >= originalLength)?
                v :
                originals[index];
    }
    
    @Override
    public void setSource(RatioTriangle source) {
        super.source.setSource(source);
    }

    @Override
    protected void recalculateLayer() {
        length = source.getLength();
    }
}
