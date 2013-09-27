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
package org.jreserve.gui.plot;

import java.awt.Color;
import org.jreserve.gui.plot.settings.PlotSettings;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PlotFormat {

    private String title;
    private String xTitle;
    private String yTitle;
    private boolean legendVisible;
    private boolean tooltipsVisible;
    private ColorGenerator colors = PlotSettings.getColorGenerator();
    private Color backgroundColor = PlotSettings.getBackground();
    private Color gridColor = PlotSettings.getBackground();
    private Color foreColor = PlotSettings.getForeground();
    private String[] seriesNames = new String[0];
    
    public String getTitle() {
        return title;
    }
    
    public PlotFormat setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public PlotFormat setSeriesNames(String... names) {
        if(names == null) {
            this.seriesNames = new String[0];
        } else {
            this.seriesNames = new String[names.length];
            System.arraycopy(names, 0, this.seriesNames, 0, names.length);
        }
        return this;
    }
    
    public String getSeriesName(int index) {
        return index<0 || index>=seriesNames.length? null : seriesNames[index];
    }
    
    public String getXTitle() {
        return xTitle;
    }
    
    public PlotFormat setXTitle(String xTitle) {
        this.xTitle = xTitle;
        return this;
    }
    
    public String getYTitle() {
        return yTitle;
    }
    
    public PlotFormat setYTitle(String yTitle) {
        this.yTitle = yTitle;
        return this;
    }
    
    public boolean isLegendVisible() {
        return legendVisible;
    }
    
    public PlotFormat setLegendVisible(boolean visible) {
        this.legendVisible = visible;
        return this;
    }
    
    public boolean isTooltipsVisible() {
        return tooltipsVisible;
    }
    
    public PlotFormat setTooltipsVisible(boolean visible) {
        this.tooltipsVisible = visible;
        return this;
    }
    
    public ColorGenerator getColors() {
        return colors;
    }
    
    public PlotFormat setColors(ColorGenerator colors) {
        this.colors = (colors==null)? PlotSettings.getColorGenerator() : colors;
        return this;
    }
    
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    public PlotFormat setBackgroundColor(Color color) {
        this.backgroundColor = (color==null)? 
                PlotSettings.getBackground() : color;
        return this;
    }
    
    public Color getForeColor() {
        return foreColor;
    }
    
    public PlotFormat setForeColor(Color color) {
        this.foreColor = (color==null)? 
                PlotSettings.getForeground() : color;
        return this;
    }
    
    public Color getGridColor() {
        return gridColor;
    }
    
    public PlotFormat setGridColor(Color color) {
        this.gridColor = (color==null)? 
                PlotSettings.getGridColor() : color;
        return this;
    }
}
