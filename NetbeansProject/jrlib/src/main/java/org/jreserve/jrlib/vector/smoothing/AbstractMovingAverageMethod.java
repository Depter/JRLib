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
package org.jreserve.jrlib.vector.smoothing;

/**
 * Abstract class for smoothing data based on some kind of moving average
 * (arithmetic, harmonic, etc.).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMovingAverageMethod implements VectorSmoothingMethod {
    
    protected int maLength;
    
    /**
     * Creates an instance for the given length for the moving 
     * average.
     * 
     * @throws IllegalArgumentException if `length` is ;ess then 1.
     */
    protected AbstractMovingAverageMethod(int maLength) {
        if(maLength <= 0)
            throw new IllegalArgumentException("Length must be at least 1, but was "+maLength+"!");
        this.maLength = maLength;
    }
    
    @Override
    public void smooth(double[] input) {
        int size = input.length;
        if(size < maLength)
            return;
        
        double[] temp = new double[size];
        for(int i=(maLength-1); i<size; i++)
            temp[i] = mean(input, i);
        
        System.arraycopy(temp, maLength-1, input, maLength-1, size-maLength+1);
    }
    
    /**
     * Extending classes should calculate a mean for the given location.
     */
    protected abstract double mean(double[] input, int index);
    
}
