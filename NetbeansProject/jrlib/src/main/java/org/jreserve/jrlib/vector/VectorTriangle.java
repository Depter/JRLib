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

import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;

/**
 * Thjis utilitiy class allows to handle a {@link Vector Vector} as
 * a {@link org.jreserve.jrlib.triangle.Triangle Triangle}.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorTriangle<V extends Vector> 
    extends AbstractCalculationData<V> 
    implements Triangle {
    
    private boolean isAccident;

    public VectorTriangle(V source) {
        super(source);
        isAccident = source.isAccident();
    }
    
    @Override
    protected void recalculateLayer() {
        isAccident = source.isAccident();
    }

    @Override
    public int getAccidentCount() {
        return isAccident? source.getLength() : 1;
    }

    @Override
    public int getDevelopmentCount() {
        return isAccident? 1 : source.getLength();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        if(isAccident) {
            if(accident < 0 || accident >= source.getLength())
                return 0;
            return 1;
        } else {
            return accident==0? source.getLength() : 0;
        }
    }

    @Override
    public double getValue(Cell cell) {
        return getValue(cell.getAccident(), cell.getDevelopment());
    }

    @Override
    public double getValue(int accident, int development) {
        if(isAccident) {
            return development==0? source.getValue(accident) : Double.NaN;
        } else {
            return accident==0? source.getValue(development) : Double.NaN;
        }
    }

    @Override
    public double[][] toArray() {
        if(isAccident) {
            int length = source.getLength();
            double[][] result = new double[length][1];
            for(int i=0; i<length; i++)
                result[i][0] = source.getValue(i);
            return result;
        } else {
            return new double[][]{source.toArray()};
        }
    }
    
    public V getSource() {
        return source;
    }
}
