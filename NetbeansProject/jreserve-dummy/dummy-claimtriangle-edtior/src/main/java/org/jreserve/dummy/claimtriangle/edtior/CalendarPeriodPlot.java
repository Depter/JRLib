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

package org.jreserve.dummy.claimtriangle.edtior;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.dummy.plot.PlotFactory;
import org.jreserve.dummy.plot.PlotFormat;
import org.jreserve.dummy.plot.PlotLabel;
import org.jreserve.dummy.plot.PlotSerie;
import org.jreserve.gui.misc.expandable.AbstractExpandableElement;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.jreserve.gui.triangletable.trianglemodel.TitleModel;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.TriangleUtil;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ExpandableElement.Registration(
    displayName = "#LBL.CalendarPeriodPlot.Title",
    mimeType = TriangleEditorMultiview.MIME_TYPE,
    position = 5000,
    prefferedID = "org.jreserve.dummy.claimtriangle.edtior.CalendarPeriodPlot",
    background = "#COLOR.CalendarPeriodPlot.Background",
    iconBase = "org/jreserve/dummy/claimtriangle/edtior/chart_bar.png"
)
@Messages({
    "LBL.CalendarPeriodPlot.Title=Calendar Periods",
    "COLOR.CalendarPeriodPlot.Background=FF7D30"
})
public class CalendarPeriodPlot extends AbstractExpandableElement {

    private Lookup lookup = Lookups.singleton("Calendar periods");
    private LayerBundle bundle;

    //Plot elements
    private Triangle triangle;
    private TitleModel dNames;
    private int accidents;
    private int developments;
    private double[][] values;
    
    public CalendarPeriodPlot(Lookup context) {
        bundle = context.lookup(LayerBundle.class);
    }

    @Override
    protected Component createVisualComponent() {
        initInputData();
        List<PlotSerie> series = createSeries();
        PlotFormat format = createFormat();
        clearCalculations();
        return PlotFactory.createBarPlot(format, series);
    }

    private void initInputData() {
        triangle = bundle.getTriangle();
        accidents = triangle.getAccidentCount();
        developments = triangle.getDevelopmentCount();
        dNames = bundle.getDevelopmentTitle();
        values = triangle.toArray();
        TriangleUtil.deCummulate(values);
    }

    private List<PlotSerie> createSeries() {
        List<PlotSerie> series = new ArrayList<PlotSerie>(1);
        PlotSerie serie = new PlotSerie(new PlotLabel(0, "Total"));
        series.add(serie);
        
        for (int d = 0; d < developments; d++)
            serie.addEntry(new PlotLabel(d, dNames.getName(d)), sumDiagonal(d));
        
        return series;
    }

    private double sumDiagonal(int d) {
        double s = 0d;
        for(int a=0; a<accidents; a++) {
            int dev = triangle.getDevelopmentCount(a) - d - 1;
            if(dev < 0) continue;
            
            double v = values[a][dev];
            if(!Double.isNaN(v))
                s += v;
        }
        return s;
    }

    private PlotFormat createFormat() {
        return new PlotFormat().setSeriesNames("Total").setLegendVisible(false);
    }

    private void clearCalculations() {
        this.triangle = null;
        this.dNames = null;
        this.accidents = 0;
        this.developments = 0;
        this.values = null;
    }

    @Override
    protected boolean openMaximized() {
        return false;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

}
