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
package org.jreserve.jrlib.triangle.factor;

import org.jreserve.jrlib.MutableSource;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * A factor triangle calculates the individual development factors from a
 * given {@link ClaimTriangle ClaimTriangle}. 
 * 
 * The formula used is `f(a,d) = c(a,d+1) / c(a,d)`, where `c(a,d)` is the
 * value from the input claim triangle for accident period <i>a</i> and
 * development period <i>d</i>. From this follows, that a FactorTriangle
 * has one less development period for evry accident period. If the last
 * accident period contained only one cell, then this instance contains one
 * less accident periods.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface FactorTriangle extends Triangle, MutableSource<ClaimTriangle> {

    /**
     * Returns the {@link Triangle Triangle} containing the
     * input claims for the calculations.
     */
    public ClaimTriangle getSourceTriangle();
}
