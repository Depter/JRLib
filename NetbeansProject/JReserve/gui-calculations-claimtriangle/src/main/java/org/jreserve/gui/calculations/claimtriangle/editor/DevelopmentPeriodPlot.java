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
package org.jreserve.gui.calculations.claimtriangle.editor;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleDataObject;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.jreserve.gui.plot.PlotFactory;
import org.jreserve.gui.plot.PlotFormat;
import org.jreserve.gui.plot.PlotLabel;
import org.jreserve.gui.plot.PlotSerie;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.TriangleUtil;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ExpandableElement.Registration(
    displayName = "#LBL.DevelopmentPeriodPlot.Title",
    mimeType = ClaimTriangleDataObject.MIME_TYPE,
    position = 500,
    prefferedID = "org.jreserve.gui.calculations.claimtriangle.editor.DevelopmentPeriodPlot",
    background = "#COLOR.DevelopmentPeriodPlot.Background",
    iconBase = "org/jreserve/gui/plot/icons/chart_line.png"
)
@Messages({
    "LBL.DevelopmentPeriodPlot.Title=Development Periods",
    "COLOR.DevelopmentPeriodPlot.Background=FF7D30"
})
public class DevelopmentPeriodPlot extends AbstractPlotElement {
    
    private Triangle triangle;
    private int developments;
    private TriangleGeometry geometry;
    
    public DevelopmentPeriodPlot() {
        this(Lookup.EMPTY);
    }
    
    public DevelopmentPeriodPlot(Lookup context) {
        super(context);
    }
    
    protected Component createPlotComponent() {
        List<PlotSerie> series;
        if(calculation == null) {
            series = Collections.EMPTY_LIST;
        } else {
            initInputData();
            series = createSeries();
        }
        
        PlotFormat format = createFormat(series);
        clearCalculations();
        return PlotFactory.createLinePlot(format, series);
    }
    
    private void initInputData() {
        triangle = calculation.getCalculation();
        triangle.getDevelopmentCount();
        developments = triangle.getDevelopmentCount();
        geometry = calculation.getGeometry();
    }

    private List<PlotSerie> createSeries() {
        List<PlotSerie> series = new ArrayList<PlotSerie>(developments);
        
        double[][] values = triangle.toArray();
        TriangleUtil.deCummulate(values);
        
        for(int d=0; d<developments; d++) {
            PlotSerie serie = createSerie(d, values);
            if(serie != null)
                series.add(serie);
        }
        
        return series;
    }
    
    private PlotSerie createSerie(int development, double[][] values) {
        int accidents = TriangleUtil.getAccidentCount(triangle, development);
        if(accidents < 2)
            return null;
        
        PlotSerie serie = new PlotSerie(createDevelopmentLabel(development));
        double prev = values[0][development];
        for(int a=1; a<accidents; a++) {
            double v = values[a][development];
            PlotLabel label = createCalendarLabel(a, development);
            serie.addEntry(label, divide(v, prev));
            prev = v;
        }
        
        return serie;
    }
    
    private double divide(double a, double b) {
        if(Double.isNaN(a) || Double.isInfinite(a) ||
           Double.isNaN(b) || Double.isInfinite(b) ||
           b == 0d)
            return Double.NaN;
        return a/b;
    }
    
    private PlotLabel createCalendarLabel(int accident, int development) {
        MonthDate date = geometry.getDevelopmentDate(accident, development);
        String name = date.toString();
        return new PlotLabel(accident, name);
    }
    
    private PlotLabel createDevelopmentLabel(int development) {
        String name = ""+(development+1);
        return new PlotLabel(development, name);
    }

    private PlotFormat createFormat(List<PlotSerie> series) {
        return new PlotFormat()
                .setSeriesNames(getSeriesNames(series))
                .setLegendVisible(true);
    }

    private void clearCalculations() {
        this.triangle = null;
        this.geometry = null;
        this.developments = 0;
    }
}
