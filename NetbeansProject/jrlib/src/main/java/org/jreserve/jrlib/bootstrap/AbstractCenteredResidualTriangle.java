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
package org.jreserve.jrlib.bootstrap;

import org.jreserve.jrlib.triangle.AbstractTriangleModification;
import org.jreserve.jrlib.triangle.Triangle;

/**
 * This class centers a residual triangl in such a way, that the
 * mean of the residuals will be 0.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractCenteredResidualTriangle<T extends Triangle> extends AbstractTriangleModification<T> {

    private double mean;
    
    public AbstractCenteredResidualTriangle(T source) {
        super(source);
        doRecalculate();
    }
    
    public double getMean() {
        return mean;
    }
    
    @Override
    public double getValue(int accident, int development) {
        return source.getValue(accident, development) - mean;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        int accidents = source.getAccidentCount();
        mean = 0d;
        int n = 0;
        
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double v = source.getValue(a, d);
                if(!Double.isNaN(v)) {
                    n++;
                    mean += v;
                }
            }
        }
        
        mean = n==0? 0d : mean / (double)n;
    }
}
