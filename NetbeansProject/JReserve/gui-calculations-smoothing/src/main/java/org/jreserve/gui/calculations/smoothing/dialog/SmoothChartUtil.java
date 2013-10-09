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
package org.jreserve.gui.calculations.smoothing.dialog;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.jfree.ui.RectangleEdge;
import org.jreserve.gui.plot.ChartWrapper;
import org.jreserve.gui.plot.ColorGenerator;
import org.jreserve.gui.plot.PlotFactory;
import org.jreserve.gui.plot.PlotFormat;
import org.jreserve.gui.plot.PlotLabel;
import org.jreserve.gui.plot.PlotSerie;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.SmoothChartUtil.Original=Original",
    "LBL.SmoothChartUtil.Smoothed=Smoothed",
    "LBL.SmoothChartUtil.Output=Output",
    "LBL.SmoothChartUtil.Accident=Accident",
    "LBL.SmoothChartUtil.Development=Development"
})
class SmoothChartUtil {
    
    private final static ColorGenerator COLORS = PlotFactory.createColorGenerator(
        Color.RED, Color.BLUE, new Color(0, 153, 51)
    );
    private final static int ID_ORIGINAL = 0;
    private final static int ID_SMOOTHED = 1;
    private final static int ID_OUTPUT = 2;
    
    static ChartWrapper createChart(List<SmoothRecord> records) {
        boolean horizontal = isHorizontal(records);
        List<PlotSerie> series = new ArrayList<PlotSerie>();
        series.add(createOriginalSerie(records, horizontal));
        series.add(createSmoothedSerie(records, horizontal));
        series.add(createOutputSerie(records, horizontal));
        
        PlotFormat format = createFormat(horizontal);
        
        return PlotFactory.createLinePlot(format, series);
    }
    
    private static boolean isHorizontal(List<SmoothRecord> records) {
        SmoothRecord prev = null;
        for(SmoothRecord record : records) {
            if(prev != null) {
                if(prev.getDevelopment() != record.getDevelopment())
                    return true;
            }
            prev = record;
        }
        return false;
    }
    
    private static PlotSerie createOriginalSerie(List<SmoothRecord> records, boolean horizontal) {
        PlotSerie serie = new PlotSerie(new PlotLabel(ID_ORIGINAL, Bundle.LBL_SmoothChartUtil_Original()));
        for(SmoothRecord record : records) {
            Comparable key = getKey(record, horizontal);
            serie.addEntry(key, record.getOriginal());
        }
        return serie;
    }
    
    private static Comparable getKey(SmoothRecord record, boolean horizontal) {
        if(horizontal)
            return record.getDevelopment()+1;
        return record.getAccident();
    }
    
    private static PlotSerie createSmoothedSerie(List<SmoothRecord> records, boolean horizontal) {
        PlotSerie serie = new PlotSerie(new PlotLabel(ID_SMOOTHED, Bundle.LBL_SmoothChartUtil_Smoothed()));
        for(SmoothRecord record : records) {
            Comparable key = getKey(record, horizontal);
            serie.addEntry(key, record.getSmoothed());
        }
        return serie;
    }
    
    private static PlotSerie createOutputSerie(List<SmoothRecord> records, boolean horizontal) {
        PlotSerie serie = new PlotSerie(new PlotLabel(ID_OUTPUT, Bundle.LBL_SmoothChartUtil_Output()));
        for(SmoothRecord record : records) {
            Comparable key = getKey(record, horizontal);
            double value = record.isUsed()? record.getSmoothed() : record.getOriginal();
            serie.addEntry(key, value);
        }
        return serie;
    }
    
    private static PlotFormat createFormat(boolean horizontal) {
        return new PlotFormat()
                .setXTitle(getPlotTitle(horizontal))
                .setLegendVisible(true)
                .setLegendPosition(RectangleEdge.RIGHT)
                .setColors(COLORS);
    }
    
    private static String getPlotTitle(boolean horizontal) {
        if(horizontal)
            return Bundle.LBL_SmoothChartUtil_Accident();
        return Bundle.LBL_SmoothChartUtil_Development();
    }
    
    private SmoothChartUtil() {}
}
