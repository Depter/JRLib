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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleDataObject;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.jreserve.gui.plot.ChartWrapper;
import org.jreserve.gui.plot.PlotFactory;
import org.jreserve.gui.plot.PlotFormat;
import org.jreserve.gui.plot.PlotLabel;
import org.jreserve.gui.plot.PlotSerie;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Triangle;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ExpandableElement.Registration(
    displayName = "#LBL.AccidentPeriodPlot.Title",
    mimeType = ClaimTriangleDataObject.MIME_TYPE,
    position = 400,
    prefferedID = "org.jreserve.gui.calculations.claimtriangle.editor.AccidentPeriodPlot",
    background = "#COLOR.AccidentPeriodPlot.Background",
    iconBase = "org/jreserve/gui/plot/icons/chart_line.png"
)
@Messages({
    "LBL.AccidentPeriodPlot.Title=Accident Periods",
    "COLOR.AccidentPeriodPlot.Background=FF7D30"
})
public class AccidentPeriodPlot extends AbstractPlotElement {
    
    private Triangle triangle;
    private int accidents;
    private TriangleGeometry geometry;
    
    public AccidentPeriodPlot() {
        this(Lookup.EMPTY);
    }
    
    public AccidentPeriodPlot(Lookup context) {
        super(context);
    }
    
    @Override
    protected ChartWrapper createPlot() {
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
        accidents = triangle.getAccidentCount();
        geometry = calculation.getGeometry();
    }

    private List<PlotSerie> createSeries() {
        List<PlotSerie> series = new ArrayList<PlotSerie>(accidents);
        
        for(int a=0; a<accidents; a++) {
            PlotSerie serie = new PlotSerie(createAccidentLabel(a));
            series.add(serie);
            
            int devs = triangle.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                serie.addEntry(createDevelopmentLabel(d), triangle.getValue(a, d));
        }
        
        return series;
    }
    
    private PlotLabel createAccidentLabel(int accident) {
        MonthDate date = geometry.getAccidentDate(accident);
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
        this.accidents = 0;
    }
}
