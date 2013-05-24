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
package org.jreserve.jrlib.bootstrap.residualgenerator;

import java.util.List;
import org.jreserve.jrlib.util.random.Random;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleResidualSegment extends AbstractResidualSegment<Double> {
    
    private final Random rnd;
    private double[] residuals;
    
    public DoubleResidualSegment(Random rnd) {
        this.rnd = rnd;
    }
    
    public DoubleResidualSegment(Random rnd, int[][] cells) {
        super(cells);
        this.rnd = rnd;
    }
    
    @Override
    protected void setValues(List<Double> values) {
        super.setValues(values);
        this.residuals = new double[valueCount];
        for(int i=0; i<valueCount; i++)
            this.residuals[i] = values.get(i);
    }
    
    public double getResidual() {
        if(valueCount == 0)
            return Double.NaN;
        if(valueCount == 1)
            return residuals[0];
        return residuals[rnd.nextInt(valueCount)];
    }
}
