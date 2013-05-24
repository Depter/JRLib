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
package org.jreserve.jrlib.vector;


/**
 * Base class for all classes that want to modify a vector. A modified 
 * vector has the same length as it's source vector, but
 * the extending classes can return a modified value for some
 * cells.
 * 
 * A modified vector should never return a non NaN value outside
 * the bounds of it's source vector.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractVectorModification extends AbstractVector<Vector> implements ModifiedVector {
    
    /**
     * Creates an instance with the given vector as source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public AbstractVectorModification(Vector source) {
        super(source);
    }
    
    @Override
    public int getLength() {
        return source.getLength();
    }

    public double getValue(int index) {
        return source.getValue(index);
    }
    
    @Override
    public Vector getSourceVector() {
        return source;
    }
}
