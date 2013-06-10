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
package org.jreserve.jrlib.triangle.claim;

import org.jreserve.jrlib.triangle.AbstractTriangleModification;
import org.jreserve.jrlib.triangle.TriangleUtil;

/**
 * A cummulated claim triangle takes the values of it's source triangle and
 * cummulates them.
 * 
 * @see TriangleUtil#cummulate(double[][]) 
 * @author Peter Decsi
 * @version 1.0
 */
public class CummulatedClaimTriangle extends AbstractTriangleModification<ClaimTriangle> implements ModifiedClaimTriangle {
    
    private int accidents;
    private double[][] values;
    
    /**
     * Creates an instance with the given source triangle.
     * 
     * @throws NullPointerException if `source` is null!
     */
    public CummulatedClaimTriangle(ClaimTriangle source) {
        super(source);
        values = source.toArray();
        TriangleUtil.cummulate(values);
        this.accidents = values.length;
    }

    @Override
    public ClaimTriangle getSourceClaimTriangle() {
        return source;
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
    
    @Override
    protected void recalculateLayer() {
        values = source.toArray();
        TriangleUtil.cummulate(values);
        this.accidents = values.length;
    }

    @Override
    protected boolean withinBounds(int accident) {
        return 0<=accident && accident<accidents;
    }
}
