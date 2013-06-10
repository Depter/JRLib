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
package org.jreserve.jrlib.triangle;

import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.CalculationData;

/**
 * Abstract implementation of the {@link Triangle Triangle} interface. This
 * implementation handles the utility methods from the Triangle interface,
 * extending classes should provide implementations for the bounds and
 * values of the triangle.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangle<T extends CalculationData> extends AbstractCalculationData<T> implements Triangle {
    
    /**
     * Creates an instance, with the given `source` as input.
     * 
     * @throws NullPointerException if `source` is null.
     */
    protected AbstractTriangle(T source) {
        super(source);
    }
    
    /**
     * Creates an instance with no source.
     */
    protected AbstractTriangle() {
    }
    
    /**
     * Forwards the call to the {@link Triangle#getValue(int, int) getValue(accident, development)}
     * method.
     * 
     * @throws NullPointerException when 'cell' is null.
     */
    @Override
    public double getValue(Cell cell) {
        return getValue(cell.getAccident(), cell.getDevelopment());
    }
    
    /**
     * Checks wether the given `accident` and `development` periods
     * are within the bound of the triangle. This method uses
     * the {@link #withinBounds(int) withinBounds(accident)} and
     * the {@link Triangle#getDevelopmentCount(int) getDevelopmentCount(accident)}
     * methods to check the bounds.
     * 
     * @return `true` if the given cell is contained in the triangle.
     */
    protected boolean withinBounds(int accident, int development) {
        return withinBounds(accident) &&
               development >= 0 &&
               development < getDevelopmentCount(accident);
    }
    
    /**
     * Checks wether the given `accident` period is within the bound of 
     * the triangle. This method uses the 
     * {@link Triangle#getAccidentCount() getAccidentCount()} and methods 
     * to check the bound.
     * 
     * @return `true` if the given accident period is contained in the 
     *          triangle.
     */
    protected abstract boolean withinBounds(int accident);// {
//        return accident >= 0 &&
//               accident < getAccidentCount();
//    }

    /**
     * Fills an array with the same dimensions as the triangle with
     * the values from the triangle.
     * 
     * @see Triangle#toArray() 
     */
    @Override
    public double[][] toArray() {
        int accidents = getAccidentCount();
        double[][] result = new double[accidents][];
        for(int a=0; a<accidents; a++)
            result[a] = toArray(a);
        return result;
    }
    
    private double[] toArray(int accident) {
        int devs = getDevelopmentCount(accident);
        double[] result = new double[devs];
        for(int d=0; d<devs; d++)
            result[d] = getValue(accident, d);
        return result;
    }        
}
