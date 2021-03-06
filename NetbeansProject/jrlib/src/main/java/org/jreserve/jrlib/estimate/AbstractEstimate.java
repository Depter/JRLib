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
package org.jreserve.jrlib.estimate;

import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.TriangleUtil;

/**
 * Abstract class for {@link Estimate Estimates}. The class provides 
 * implementations for most of the methods declared in the estimate
 * interface. It is enough to implement the 
 * {@link Estimate#getObservedDevelopmentCount(int) getObservedDevelopmentCount()}
 * and the {@link AbstractCalculationData#recalculateLayer() recalculateLayer()}
 * methods.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractEstimate<T extends CalculationData>
    extends AbstractCalculationData<T> 
    implements Estimate {

    protected int accidents;
    protected int developments;
    protected double[][] values;
    
    protected AbstractEstimate() {
    }
    
    protected AbstractEstimate(T source) {
        super(source);
    }
    
    @Override
    public int getAccidentCount() {
        return accidents;
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    @Override
    public double getValue(Cell cell) {
        return getValue(cell.getAccident(), cell.getDevelopment());
    }

    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
    
    protected boolean withinBounds(int accident, int development) {
        return accident >= 0 && accident < accidents &&
               development >= 0 && development < developments;
    }

    @Override
    public double[][] toArray() {
        return TriangleUtil.copy(values);
    }

    @Override
    public double getReserve(int accident) {
        int dLast = getObservedDevelopmentCount(accident)-1;
        double lastObserved = getValue(accident, dLast);
        double lastEstimated = getValue(accident, developments-1);
        return lastEstimated - lastObserved;
    }

    @Override
    public double getReserve() {
        double sum = 0d;
        for(int a=0; a<accidents; a++)
            sum += getReserve(a);
        return sum;
    }

    @Override
    public double[] toArrayReserve() {
        double[] result = new double[accidents];
        for(int a=0; a<accidents; a++)
            result[a] = getReserve(a);
        return result;
    }    
}
