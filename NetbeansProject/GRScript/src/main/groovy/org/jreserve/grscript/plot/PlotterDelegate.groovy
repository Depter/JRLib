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
import org.jfree.chart.ChartPanel
import org.jreserve.jrlib.triangle.factor.FactorTriangle
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.claimratio.ClaimRatio
import org.jreserve.jrlib.triangle.ratio.RatioTriangle
import org.jreserve.grscript.plot.claimtriangle.ClaimTrianglePlot
import org.jreserve.grscript.plot.factortriangle.FactorTrianglePlot
import org.jreserve.grscript.plot.ratio.RatioPlot
import org.jreserve.grscript.plot.ratiotriangle.RatioTrianglePlot
import org.jreserve.grscript.plot.lrcurve.CurvePlot
import org.jreserve.grscript.plot.residual.ResidualPlots
import org.jreserve.grscript.plot.estimate.EstimatePlots
import org.jreserve.grscript.plot.histogram.HistogramPlots
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.scaledresiduals.OdpScaledResidualTriangle
import org.jreserve.jrlib.estimate.Estimate
import org.jreserve.jrlib.estimate.mcl.MclEstimateBundle
import org.jreserve.jrlib.bootstrap.util.HistogramData

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
    
    void plot(RatioTriangle ratios) {
        this.plot(ratios ,new PlotFormat())
    }
    
    void plot(RatioTriangle ratios, PlotFormat format) {
        Map charts = RatioTrianglePlot.createPlot(ratios, format)
        String title = format.getTitle()
        showPlot(title ?: "Ratio Triangle", charts)
    }
    
    void plot(RatioTriangle ratios, Closure cl) {
        plot(ratios, buildFormat(cl))
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

    
    void plot(LinkRatio lrs, Map curves) {
        this.plot(lrs ,curves, new PlotFormat())
    }
    
    void plot(LinkRatio lrs, Map curves, PlotFormat format) {
        Map charts = CurvePlot.createPlot(lrs, curves, format)
        String title = format.getTitle()
        showPlot(title ?: "Link-Ratio Curves", charts)
    }
    
    void plot(LinkRatio lrs, Map curves, Closure cl) {
        plot(lrs, curves, buildFormat(cl))
    }
    
    void plot(LinkRatio lrs, List curves) {
        this.plot(lrs ,curves, new PlotFormat())
    }
    
    void plot(LinkRatio lrs, List curves, PlotFormat format) {
        Map charts = CurvePlot.createPlot(lrs, curves, format)
        String title = format.getTitle()
        showPlot(title ?: "Link-Ratio Curves", charts)
    }
    
    void plot(LinkRatio lrs, List curves, Closure cl) {
        plot(lrs, curves, buildFormat(cl))
    }
    
    void plot(LRResidualTriangle residuals) {
        plot(residuals, new PlotFormat())
    }
    
    void plot(LRResidualTriangle residuals, PlotFormat format) {
        Map charts = ResidualPlots.createPlot(residuals, format)
        String title = format.getTitle()
        showPlot(title ?: "Link-Ratio Residuals", charts)
    }
    
    void plot(LRResidualTriangle residuals, Closure cl) {
        plot(residuals, buildFormat(cl))
    }
    
    void plot(CRResidualTriangle residuals) {
        plot(residuals, new PlotFormat())
    }
    
    void plot(CRResidualTriangle residuals, PlotFormat format) {
        Map charts = ResidualPlots.createPlot(residuals, format)
        String title = format.getTitle()
        showPlot(title ?: "Claim-Ratio Residuals", charts)
    }
    
    void plot(CRResidualTriangle residuals, Closure cl) {
        plot(residuals, buildFormat(cl))
    }
    
    void plot(OdpResidualTriangle residuals) {
        plot(residuals, new PlotFormat())
    }
    
    void plot(OdpResidualTriangle residuals, PlotFormat format) {
        Map charts = ResidualPlots.createPlot(residuals, format)
        String title = format.getTitle()
        showPlot(title ?: "ODP-Residuals", charts)
    }
    
    void plot(OdpResidualTriangle residuals, Closure cl) {
        plot(residuals, buildFormat(cl))
    }
    
    void plot(OdpScaledResidualTriangle residuals) {
        plot(residuals, new PlotFormat())
    }
    
    void plot(OdpScaledResidualTriangle residuals, PlotFormat format) {
        Map charts = ResidualPlots.createPlot(residuals, format)
        String title = format.getTitle()
        showPlot(title ?: "ODP-Residuals", charts)
    }
    
    void plot(OdpScaledResidualTriangle residuals, Closure cl) {
        plot(residuals, buildFormat(cl))
    }
    
    void plot(Estimate estimate) {
        plot(estimate, new PlotFormat())
    }
    
    void plot(Estimate estimate, PlotFormat format) {
        Map charts = EstimatePlots.createPlot(estimate, format)
        String title = format.getTitle()
        showPlot(title ?: "Estimate", charts)
    }
    
    void plot(Estimate estimate, Closure cl) {
        plot(estimate, buildFormat(cl))
    }
    
    void plot(MclEstimateBundle bundle) {
        plot(bundle, new PlotFormat())
    }
    
    void plot(MclEstimateBundle bundle, PlotFormat format) {
        Map charts = EstimatePlots.createPlot(bundle, format)
        String title = format.getTitle()
        showPlot(title ?: "MCL-Estimate", charts)
    }
    
    void plot(MclEstimateBundle bundle, Closure cl) {
        plot(bundle, buildFormat(cl))
    }
    
    void plot(HistogramData data, Map values) {
        plot(data, values, new PlotFormat())
    }
    
    void plot(HistogramData data, Map values, PlotFormat format) {
        Map charts = HistogramPlots.createPlot(data, values, format)
        String title = format.getTitle()
        showPlot(title ?: "Histogram", charts)
    }
    
    void plot(HistogramData data, Map values, Closure cl) {
        plot(data, values, buildFormat(cl))
    }
    
    void plot(HistogramData data) {
        plot(data, [:])
    }
    
    void plot(HistogramData data, PlotFormat format) {
        plot(data, [:], format)
    }
    
    void plot(HistogramData data, Closure cl) {
        plot(data, [:], cl)
    }
}

