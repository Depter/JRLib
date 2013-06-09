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
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ExpandableElement.Registration(
    displayName = "#LBL.AccidentPeriodPlot.Title",
    mimeType = TriangleEditorMultiview.MIME_TYPE,
    position = 3000,
    prefferedID = "org.jreserve.dummy.claimtriangle.edtior.AccidentPeriodPlot",
    background = "#COLOR.AccidentPeriodPlot.Background"
)
@Messages({
    "LBL.AccidentPeriodPlot.Title=Accident Periods",
    "COLOR.AccidentPeriodPlot.Background=FF7D30"
})
public class AccidentPeriodPlot extends AbstractExpandableElement {

    private Lookup lookup = Lookups.singleton("Accident periods");
    private LayerBundle bundle;

    //Plot elements
    private Triangle triangle;
    private TitleModel aNames;
    private TitleModel dNames;
    private int accidents;

    public AccidentPeriodPlot(Lookup context) {
        bundle = context.lookup(LayerBundle.class);
    }

    @Override
    protected Component createVisualComponent() {
        initInputData();
        List<PlotSerie> series = createSeries();
        PlotFormat format = createFormat();
        clearCalculations();
        return PlotFactory.createLinePlot(format, series);
    }

    private void initInputData() {
        triangle = bundle.getTriangle();
        accidents = triangle.getAccidentCount();
        aNames = bundle.getAccidentTitles();
        dNames = bundle.getDevelopmentTitle();
    }

    private List<PlotSerie> createSeries() {
        List<PlotSerie> series = new ArrayList<PlotSerie>(accidents);
        for(int a=0; a<accidents; a++) {
            PlotSerie serie = new PlotSerie(new PlotLabel(a, aNames.getName(a)));
            series.add(serie);
            
            int devs = triangle.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                serie.addEntry(new PlotLabel(d, dNames.getName(d)), triangle.getValue(a, d));
        }
        
        return series;
    }

    private PlotFormat createFormat() {
        return new PlotFormat().setSeriesNames(getSeriesNames()).setLegendVisible(true);
    }
    
    private String[] getSeriesNames() {
        String[] names = new String[accidents];
        for(int a=0; a<accidents; a++)
            names[a] = aNames.getName(a);
        return names;
    }

    private void clearCalculations() {
        this.triangle = null;
        this.aNames = null;
        this.dNames = null;
        this.accidents = 0;
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
