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
package org.jreserve.jrlib.bootstrap.util;

import org.jreserve.jrlib.triangle.TriangleUtil;

/**
 * HistogramData stores the data for a histogram. It provides
 * the number of intervals, the size of an interval, the upper and
 * lower bounds for the intervals, and the number of values per intervals.
 * 
 * @see HistogramDataFactory
 * @author Peter Decsi
 * @version 1.0
 */
public class HistogramData {
    
    private final double intervalSize;
    private final int intervalCount;
    private final double[][] bounds;
    private final int counts[];
    
    /**
     * Creates a new instance with the given parameters. No sanity check
     * is maed on the input. Use the {@link HistogramDataFactory HistogramDataFactory}
     * to create instances.
     * 
     * @throws NullPointerException if `data` is null.
     */
    protected HistogramData(double firstUpperBound, double intervalSize, int intervalCount, double[] data) {
        this.intervalSize = intervalSize;
        this.intervalCount = intervalCount;
        
        counts = new int[intervalCount];
        bounds = new double[intervalCount][2];
        
        if(intervalCount > 0) {
            fillBounds(firstUpperBound);
            fillIntervals(data);
        }
    }
    
    private void fillBounds(double upperBound) {
        bounds[0][0] = Double.NEGATIVE_INFINITY;
        bounds[0][1] = upperBound;
        for(int i=1; i<intervalCount; i++) {
            bounds[i][0] = upperBound;
            upperBound+=intervalSize;
            bounds[i][1] = upperBound;
        }
        bounds[intervalCount-1][1] = Double.POSITIVE_INFINITY;
    }
    
    private void fillIntervals(double[] data) {
        int size = data.length;
        for(int i=0; i<size; i++) {
            double value = data[i];
            if(!Double.isNaN(value))
                counts[getIndex(value)]++;
        }
    }
    
    private int getIndex(double value) {
        for(int i=0; i<intervalCount; i++)
            if(value < bounds[i][1])
                return i;
        return intervalCount-1;
    }
    
    /**
     * Returns the number of intervals.
     */
    public int getIntervalCount() {
        return intervalCount;
    }
    
    /**
     * Returns the number of elements in a given interval. If `interval`
     * falls outside of the bounds 
     * `[0; {@link #getIntervalCount() getIntervalCount()}]`, 0 is returned.
     */
    public int getCount(int interval) {
        return withinBounds(interval)? counts[interval] : 0;
    }
    
    private boolean withinBounds(int interval) {
        return 0<=interval && interval < intervalCount;
    }
    
    /**
     * Returns the lower bound for the given interval. If `interval`
     * falls outside of the bounds 
     * `[0; {@link #getIntervalCount() getIntervalCount()}]`, NaN is returned.
     */
    public double getLowerBound(int interval) {
        return withinBounds(interval)? bounds[interval][0] : Double.NaN;
    }
    
    /**
     * Returns the upper bound for the given interval. If `interval`
     * falls outside of the bounds 
     * `[0; {@link #getIntervalCount() getIntervalCount()}]`, NaN is returned.
     */
    public double getUpperBound(int interval) {
        return withinBounds(interval)? bounds[interval][1] : Double.NaN;
    }
    
    /**
     * Returns the lower and upper bound for the given interval. If `interval`
     * falls outside of the bounds 
     * `[0; {@link #getIntervalCount() getIntervalCount()}]`, null is returned.
     */
    public double[] getBounds(int interval) {
        return withinBounds(interval)? TriangleUtil.copy(bounds[interval]) : null;
    }
    
    /**
     * Returns all bounds. Modifying the retunred array does not affect
     * the internal state of this instance.
     */
    public double[][] getBounds() {
        return TriangleUtil.copy(bounds);
    }
    
    /**
     * Returns all counts. Modifying the retunred array does not affect
     * the internal state of this instance.
     */
    public int[] getCounts() {
        int[] result = new int[intervalCount];
        System.arraycopy(counts, 0, result, 0, intervalCount);
        return result;
    }
    
    /**
     * Returns the histogram data as a matrix. The first coordinate
     * is the row, the second coordinate points to the lower bound,
     * upper bound and to the count.
     */
    public double[][] toArray() {
        double[][] result = new double[intervalCount][3];
        for(int i=0; i<intervalCount; i++) {
            System.arraycopy(bounds[i], 0, result[i], 0, 2);
            result[i][2] = counts[i];
        }
        return result;    
    }
}
