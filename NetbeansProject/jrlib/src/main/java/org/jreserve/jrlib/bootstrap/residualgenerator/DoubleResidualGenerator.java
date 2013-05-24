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

import java.util.ArrayList;
import java.util.List;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.util.random.Random;

/**
 * Instances of DoubleResidualGenerator generate residuals from a triangle,
 * containing double values.
 * 
 * @see AbstractResidualGenerator
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleResidualGenerator<E extends Triangle> extends AbstractResidualGenerator<Double, DoubleResidualSegment, E> {
    
    /**
     * Creates an instance with the given random generator.
     * 
     * @throws NullPointerException if `rnd` is null.
     */
    public DoubleResidualGenerator(Random rnd) {
        super(rnd);
    }

    /**
     * Creates an initialised instance with the given random generator.
     * 
     * @see #initialize(java.lang.Object, java.util.List) 
     * @throws NullPointerException if on of the parameters is null.
     */
    public DoubleResidualGenerator(Random rnd, E residuals, List<int[][]> segments) {
        super(rnd, residuals, segments);
    }
    
    @Override
    protected DoubleResidualSegment createSegment() {
        return new DoubleResidualSegment(rnd);
    }

    @Override
    protected DoubleResidualSegment[] createEmptySegmentArray(int count) {
        return new DoubleResidualSegment[count];
    }

    @Override
    protected DoubleResidualSegment createSegment(int[][] cells) {
        return new DoubleResidualSegment(rnd, cells);
    }

    @Override
    protected List<Double> getValuesForSegment(E residuals, DoubleResidualSegment segment) {
        List<Double> values = new ArrayList<Double>();
        int accidents = residuals.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = residuals.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double r = residuals.getValue(a, d);
                if(!Double.isNaN(r) && segment.containsCell(a, d))
                    values.add(r);
            }
        }
        return values;
    }

    @Override
    protected List<Double> getValuesForDefaultSegment(E residuals) {
        List<Double> values = new ArrayList<Double>();
        int accidents = residuals.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = residuals.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double r = residuals.getValue(a, d);
                if(!Double.isNaN(r) && getSegment(a, d) == defaultSegment)
                    values.add(r);
            }
        }
        return values;
    }
    
    /**
     * Generates a residual for the given accident and development
     * period.
     */
    public double getValue(int accident, int development) {
        DoubleResidualSegment s = getSegment(accident, development);
        return getSegment(accident, development).getResidual();
    }
}
