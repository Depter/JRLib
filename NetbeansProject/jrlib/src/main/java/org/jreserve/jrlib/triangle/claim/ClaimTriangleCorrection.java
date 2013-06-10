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

import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.TriangleCorrection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimTriangleCorrection extends TriangleCorrection<ClaimTriangle> 
    implements ModifiedClaimTriangle {
    
    public ClaimTriangleCorrection(ClaimTriangle source, Cell cell, double corrigatedValue) {
        super(source, cell, corrigatedValue);
    }

    public ClaimTriangleCorrection(ClaimTriangle source, int accident, int development, double corrigatedValue) {
        super(source, accident, development, corrigatedValue);
    }
    
    @Override
    public ClaimTriangle getSourceClaimTriangle() {
        return source;
    }
}
