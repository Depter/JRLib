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

import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.CalculationState;

/**
 * Input triangles are the first links in all
 * calculation chains. They simply wrap a trangular
 * array and expose it's contents via the
 * {@link Triangle Triangle} interface.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class InputTriangle extends AbstractTriangle<CalculationData> implements Triangle {

    private double[][] values;
    private int accidents;
    private int developments;
    
    /**
     * Creates a triangle, from the given values. Later modifying 
     * the values of the inut array does not affect the state
     * of this instance.
     * 
     * @throws IllegalArgumentException if the geometry of 
     * the input data does not conform the contract of the
     * {@link Triangle Triangle} interface.
     */
    public InputTriangle(double[][] data) {
        initData(data);
    }
    
    private void initData(double[][] data) {
        if(data == null || data.length == 0) {
            fillEmptyData();
        } else {
            fillData(data);
        }
        TriangleDimensionCheck.validateDimensions(this);
    }
    
    private void fillEmptyData() {
        accidents = 0;
        developments = 0;
    }
    
    private void fillData(double[][] values) {
        accidents = values.length;
        this.values = new double[accidents][];
        for(int a=0; a<accidents; a++)
            copyDevelopment(a, values[a]);
    }
    
    private void copyDevelopment(int accident, double[] values) {
        int devs = setDevelopments(values);
        this.values[accident] = new double[devs];
        System.arraycopy(values, 0, this.values[accident], 0, devs);
    }
    
    private int setDevelopments(double[] values) {
        int devs = values.length;
        if(devs > developments) 
            developments = devs;
        return devs;
    }
    
    @Override
    protected boolean withinBounds(int accident) {
        return 0 <= accident && accident < accidents;
    }
    
    /**
     * Sets the given values. Later modifying 
     * the values of the inut array does not affect the state
     * of this instance.
     * 
     * Calling this method will fire a change event.
     * 
     * @throws IllegalArgumentException if the geometry of 
     * the input data does not conform the contract of the
     * {@link Triangle Triangle} interface.
     */
    protected void setData(double[][] values) {
        setState(CalculationState.INVALID);
        initData(values);
        setState(CalculationState.INVALID);
    }
    
    @Override
    protected void recalculateLayer() {
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
    public int getDevelopmentCount(int accident) {
        if(withinBounds(accident))
            return values[accident].length;
        return 0;
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
    
    @Override
    public String toString() {
        int step = developments - getDevelopmentCount(1);
        return String.format(
                "InputTriangle [accidents=%d; developments=%d; step=%d]",
                accidents, developments, step);
    }
}