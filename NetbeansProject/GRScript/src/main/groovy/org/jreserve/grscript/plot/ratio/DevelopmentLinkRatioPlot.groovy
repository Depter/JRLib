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

package org.jreserve.grscript.plot.ratio

import org.jreserve.grscript.plot.AbstractPlot
import org.jreserve.grscript.plot.PlotFormat
import org.jfree.data.xy.XYSeriesCollection
import org.jfree.data.xy.XYSeries
import org.jreserve.grscript.plot.AbstractLinePlot
import org.jfree.chart.renderer.category.LineAndShapeRenderer
import java.awt.BasicStroke
import java.awt.Color
import org.jreserve.grscript.plot.AbstractXYLinePlot
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.triangle.claim.ClaimTriangle
import org.jreserve.jrlib.triangle.TriangleUtil
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DevelopmentLinkRatioPlot extends AbstractXYLinePlot {
    
    private LinkRatio lrs
    private int development
    
    DevelopmentLinkRatioPlot(LinkRatio lrs, int development, PlotFormat format) {
        super(format)
        this.lrs= lrs
        this.development = development
    }    
    
    protected double[] getXValues() {
        getClaims(development)
    }
    
    private double[] getClaims(int development) {
        ClaimTriangle claims = lrs.getSourceTriangle();
        TriangleUtil.getAccidentValues(claims, development)
    }
    
    protected double[] getYValues() {
        getClaims(development+1)
    }
    
    protected double[] getFittedValues() {
        double ratio = lrs.getValue(development)
        double[] x = getXValues()
        x.collect {it * ratio} as double[]
    }
    
}

