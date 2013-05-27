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

package org.jreserve.grscript.plot.data

import groovy.transform.PackageScope
import org.jreserve.grscript.plot.AbstractLinePlot
import org.jreserve.grscript.plot.PlotFormat
import org.jfree.data.category.DefaultCategoryDataset

/**
 *
 * @author Peter Deics
 */
@PackageScope class DataPlot extends AbstractLinePlot {
    
    private double[][] data
    private double minValue
    private double maxValue
    
    DataPlot(double[][] data, PlotFormat format) {
        super(format)
        this.data = data
    }
    
    protected String getChartTitle() {
        return null
    }
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        int series = data.length
        for(int s=0; s<series; s++)
            addSeries(ds, s)
        setBounds()
        return ds
    }
    
    private void addSeries(DefaultCategoryDataset ds, int index) {
        double[] series = data[index]
        if(series) {
            String rowKey = getRowKey(index)
            series.eachWithIndex{double v, int i -> 
                ds.addValue(v, rowKey, Integer.valueOf(i))
            }
        }
    }
    
    private String getRowKey(int accident) {
        String name = format.getSeriesName(accident)
        return name ?: "Row_${accident}"
    }
    
    private void setBounds() {
        minValue = data.min()
        maxValue = data.max()
    }
    
    protected double getMinValue() {
        minValue
    }
    
    protected double getMaxValue() {
        maxValue
    }
}

