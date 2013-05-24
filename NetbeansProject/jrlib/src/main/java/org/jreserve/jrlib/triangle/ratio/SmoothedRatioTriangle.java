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
package org.jreserve.jrlib.triangle.ratio;

import org.jreserve.jrlib.triangle.SmoothedTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedRatioTriangle extends SmoothedTriangle<RatioTriangle> implements ModifiedRatioTriangle {

    public SmoothedRatioTriangle(RatioTriangle source, TriangleSmoothing smoothing) {
        super(source, smoothing);
    }
    
    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return source;
    }

    @Override
    public RatioTriangleInput getSourceInput() {
        return source.getSourceInput();
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
    public void setSource(RatioTriangleInput source) {
        this.source.setSource(source);
    }
}