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

package org.jreserve.grscript.plot.lrcurve

import groovy.transform.PackageScope
import org.jreserve.grscript.plot.AbstractLinePlot
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.vector.Vector as RVector
import org.jreserve.grscript.plot.PlotFormat
import org.jfree.data.category.DefaultCategoryDataset
import org.jreserve.grscript.plot.PlotLabel
import java.util.Map.Entry

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@PackageScope class LrCurvePlot extends AbstractLinePlot {

    private LinkRatio lrs
    private Map curves
    private double minValue = Double.NaN
    private double maxValue = Double.NaN
    
    LrCurvePlot(LinkRatio lrs, Map curves, PlotFormat format) {
        super(format)
        this.lrs = lrs
        this.curves = curves
    }
    
    protected String getChartTitle() {
        return null
    }
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        
        int index = 0;
        addVector(ds, getLabel(index++), lrs)
        for(Entry e : curves.entrySet())
            addVector(ds, new PlotLabel(index++, e.key), e.value)
        
        return ds
    }
    
    private PlotLabel getLabel(int index) {
        String name = format.getSeriesName(index)
        return new PlotLabel(index, name ?: (index==0? "Link-Ratio" : "Curve_${index}"))
    }
    
    private void addVector(DefaultCategoryDataset ds, PlotLabel rowKey, RVector data) {
        int devs = data.getLength()
        for(int d=0; d<devs; d++) {
            double v = data.getValue(d)
            calculateBounds(v)
            ds.addValue(v, rowKey, Integer.valueOf(d))
        }
    }
	
    private void calculateBounds(double v) {
        if(Double.isNaN(minValue) || minValue > v) minValue = v
        if(Double.isNaN(maxValue) || maxValue < v) maxValue = v
    }
    
    protected double getMinValue() {
        minValue
    }
    
    protected double getMaxValue() {
        maxValue
    }    
}

