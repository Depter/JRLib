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
package org.jreserve.grscript.gui.plot.plots;

import java.awt.Color;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PlotFormat {
    
    private final static Color BG_COLOR = Color.WHITE;
    private final static Color GRID_COLOR = Color.LIGHT_GRAY;
    
    private String title;
    private String domainTitle;
    private String rangeTitle;
    private PlotOrientation orientation = PlotOrientation.VERTICAL;
    private boolean showLegend;
    private boolean showTooltip;

    private Color bgColor = BG_COLOR;

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor!=null? bgColor : BG_COLOR;
    }
    
    public Color getGridLineColor() {
        return GRID_COLOR;
    }
    
    public boolean isShowLegend() {
        return showLegend;
    }

    public void setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
    }

    public boolean isShowTooltip() {
        return showTooltip;
    }

    public void setShowTooltip(boolean showTooltip) {
        this.showTooltip = showTooltip;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDomainTitle() {
        return domainTitle;
    }
    
    public void setDomainTitle(String title) {
        this.domainTitle = title;
    }
    
    public String getRangeTitle() {
        return rangeTitle;
    }
    
    public void setRangeTitle(String title) {
        this.domainTitle = title;
    }
    
    public PlotOrientation getOrientation() {
        return orientation;
    }
    
    public void setOrientation(PlotOrientation orientation) {
        this.orientation = orientation!=null? orientation : PlotOrientation.VERTICAL;
    }
    
    
}
