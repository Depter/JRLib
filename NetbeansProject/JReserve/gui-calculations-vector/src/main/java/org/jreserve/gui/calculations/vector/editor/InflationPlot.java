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
package org.jreserve.gui.calculations.vector.editor;

import java.util.Collections;
import java.util.List;
import org.jreserve.gui.calculations.vector.impl.VectorDataObject;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.jreserve.gui.plot.PlotSerie;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ExpandableElement.Registration(
    displayName = "#LBL.InflationPlot.Title",
    mimeType = VectorDataObject.MIME_TYPE,
    position = 500,
    prefferedID = "org.jreserve.gui.calculations.vector.editor.InflationPlot",
    background = "#COLOR.InflationPlot.Background",
    iconBase = "org/jreserve/gui/plot/icons/chart_line.png"
)
@Messages({
    "LBL.InflationPlot.Title=Inflation",
    "COLOR.InflationPlot.Background=FF7D30"
})
public class InflationPlot extends AbstractVectorPlot {
    
    public InflationPlot() {
        this(Lookup.EMPTY);
    }
    
    public InflationPlot(Lookup context) {
        super(context);
    }

    protected List<PlotSerie> createSeries() {
        PlotSerie serie = new PlotSerie("inflation");
        double prev = vector.getValue(0);
        for(int a=1; a<accidents; a++) {
            double value = vector.getValue(a);
            double perc;
            if(Double.isNaN(prev) || Double.isNaN(value) || prev == 0d)
                perc = Double.NaN;
            else
                perc = value / prev - 1d;
            serie.addEntry(createAccidentLabel(a-1), perc);
            prev = value;
        }
        return Collections.singletonList(serie);
    }
}