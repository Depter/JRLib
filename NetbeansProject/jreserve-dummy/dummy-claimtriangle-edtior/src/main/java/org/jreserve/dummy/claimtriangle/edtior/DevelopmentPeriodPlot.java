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
    displayName = "#LBL.DevelopmentPeriodPlot.Title",
    mimeType = TriangleEditorMultiview.MIME_TYPE,
    position = 4000,
    prefferedID = "org.jreserve.dummy.claimtriangle.edtior.DevelopmentPeriodPlot",
    background = "#COLOR.DevelopmentPeriodPlot.Background"
)
@Messages({
    "LBL.DevelopmentPeriodPlot.Title=Development Periods",
    "COLOR.DevelopmentPeriodPlot.Background=FF7D30"
})
public class DevelopmentPeriodPlot extends AbstractExpandableElement {

    private Lookup lookup = Lookups.singleton("Development periods");
    private LayerBundle bundle;

    //Plot elements
    private Triangle triangle;
    private TitleModel dNames;
    private int accidents;
    private int developments;

    public DevelopmentPeriodPlot(Lookup context) {
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
    }

    private List<PlotSerie> createSeries() {
        List<PlotSerie> series = new ArrayList<PlotSerie>(1);
        PlotSerie serie = new PlotSerie(new PlotLabel(0, "Total"));
        series.add(serie);
        
        double[] totals = getColumnTotals();
        for (int d = 0; d < developments; d++)
            serie.addEntry(new PlotLabel(d, dNames.getName(d)), totals[d]);
        
        return series;
    }

    private double[] getColumnTotals() {
        double[] sums = new double[developments];
        double[][] values = triangle.toArray();
        TriangleUtil.deCummulate(values);
        
        for (int a = 0; a < accidents; a++) {
            int devs = triangle.getDevelopmentCount(a);
            for (int d = 0; d < devs; d++) {
                double v = values[a][d];
                if (!Double.isNaN(v)) {
                    sums[d] += v;
                }
            }
        }

        return sums;
    }

    private PlotFormat createFormat() {
        return new PlotFormat().setSeriesNames("Total").setLegendVisible(false);
    }

    private void clearCalculations() {
        this.triangle = null;
        this.dNames = null;
        this.accidents = 0;
        this.developments = 0;
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
