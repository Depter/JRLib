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

package org.jreserve.gui.excel.dataimport;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleTableReader extends NumericTableReader<double[][]>{
    
    private final List<List<Double>> values = new ArrayList<List<Double>>();
    
    private List<Double> currentRow;
    
    private int prevRow = -1;
    
    @Override
    public void numberFound(int row, short column, double value) throws Exception {
        if(prevRow != row) {
            currentRow = new ArrayList<Double>();
            values.add(currentRow);
            prevRow = row;
        }
        currentRow.add(value);
    }

    @Override
    public double[][] getTable() throws Exception {
        int size = values.size();
        double[][] result = new double[size][];
        for(int r=0; r<size; r++)
            result[r] = toArray(values.get(r));
        return result;
    }
    
    private double[] toArray(List<Double> row) {
        int size = row.size();
        double[] result = new double[size];
        for(int c=0; c<size; c++)
            result[c] = row.get(c).doubleValue();
        return result;
    }
}
