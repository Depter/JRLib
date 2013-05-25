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

package org.jreserve.grscript.plot

import org.jreserve.grscript.AbstractDelegate
import org.jreserve.jrlib.triangle.claim.ClaimTriangle
import org.jreserve.grscript.plot.claimtriangle.ClaimTrianglePlot
import org.jreserve.grscript.plot.factortriangle.FactorTrianglePlot
import org.jreserve.grscript.plot.ratio.RatioPlot
import org.jfree.chart.ChartPanel
import org.jreserve.jrlib.triangle.factor.FactorTriangle
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.claimratio.ClaimRatio

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PlotterDelegate extends AbstractDelegate {
	
    private PlotterFactory plotterFactory;
    
    PlotterDelegate(PlotterFactory plotterFactory) {
        this.plotterFactory = plotterFactory
    }
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        
        emc.plot << this.&plot
    }
    
    void plot(ClaimTriangle triangle) {
        this.plot(triangle,new PlotFormat())
    }
    
    void plot(ClaimTriangle triangle, PlotFormat format) {
        Map charts = ClaimTrianglePlot.createPlot(triangle, format)
        String title = format.getTitle()
        showPlot(title ?: "Claim Triangle", charts)
    }
    
    private void showPlot(String title, Map charts) {
        Plotter plotter = plotterFactory.createPlotter(title)
        charts.each {name, chart -> 
            ChartPanel panel = new ChartPanel(chart)
            plotter.addChart(name, panel)
        }
        plotter.showPlot()
    }
    
    void plot(ClaimTriangle triangle, Closure cl) {
        plot(triangle, buildFormat(cl))
    }
    
    private PlotFormat buildFormat(Closure cl) {
        PlotFormatBuilder builder = new PlotFormatBuilder(this)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        builder.getFormat()
    }
    
    void plot(FactorTriangle factors) {
        this.plot(factors ,new PlotFormat())
    }
    
    void plot(FactorTriangle factors, PlotFormat format) {
        Map charts = FactorTrianglePlot.createPlot(factors, format)
        String title = format.getTitle()
        showPlot(title ?: "Factor Triangle", charts)
    }
    
    void plot(FactorTriangle factors, Closure cl) {
        plot(factors, buildFormat(cl))
    }
    
    void plot(LinkRatio lrs) {
        this.plot(lrs ,new PlotFormat())
    }
    
    void plot(LinkRatio lrs, PlotFormat format) {
        Map charts = RatioPlot.createPlot(lrs, format)
        String title = format.getTitle()
        showPlot(title ?: "Link-Ratios", charts)
    }
    
    void plot(LinkRatio lrs, Closure cl) {
        plot(lrs, buildFormat(cl))
    }
    
    void plot(ClaimRatio lrs) {
        this.plot(lrs ,new PlotFormat())
    }
    
    void plot(ClaimRatio lrs, PlotFormat format) {
        Map charts = RatioPlot.createPlot(lrs, format)
        String title = format.getTitle()
        showPlot(title ?: "Claim-Ratios", charts)
    }
    
    void plot(ClaimRatio lrs, Closure cl) {
        plot(lrs, buildFormat(cl))
    }
}

