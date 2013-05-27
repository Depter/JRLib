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

import java.awt.Color
import org.jreserve.grscript.plot.colors.ColorGenerator
import org.jreserve.grscript.plot.colors.DefaultColorGenerator
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PlotFormat {

    private final static Color FORE_COLOR = Color.BLACK;
    private final static Color BACKGROUND = Color.WHITE;
    private final static Color GRID_COLOR = Color.LIGHT_GRAY;
    
    String title = null;
    String xTitle = null;
    String yTitle = null;
    Color backgroundColor = BACKGROUND;
    Color gridColor = GRID_COLOR;
    Color foreColor = FORE_COLOR;
    boolean showLegend = true;
    boolean showTooltips = true;
    private ColorGenerator colors = new DefaultColorGenerator();
    List seriesNames
    
    void setColorGenerator(ColorGenerator generator) {
        this.colors = generator ?: new DefaultColorGenerator()
    }
    
    void setForeColor(Color color) {
        this.foreColor = color ?: FORE_COLOR
    }
    
    Color getForeColor() {
        foreColor
    }
    
    Color nextColor() {
        return colors.nextColor()
    }
    
    void resetColors() {
        colors.reset()
    }
    
    String getSeriesName(int index) {
        return (!seriesNames || index<0 || index>=seriesNames.size())?
            null : 
            seriesNames.get(index)
    }
}

