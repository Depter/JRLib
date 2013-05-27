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
import org.jreserve.grscript.AbstractDelegate
import org.jreserve.grscript.plot.colors.ListColorGenerator
import org.jreserve.grscript.plot.colors.ColorUtil

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PlotFormatBuilder {
    
    private AbstractDelegate scriptDelegate 
    private PlotFormat format = new PlotFormat()
	
    PlotFormatBuilder(AbstractDelegate scriptDelegate) {
        this.scriptDelegate = scriptDelegate
    }
        
    def getProperty(String name) {
        this.scriptDelegate.getProperty(name)
    }
    
    void title(String title) {
        this.format.title = title
    }
    
    void xTitle(String title) {
        this.format.xTitle = title
    }
    
    void yTitle(String title) {
        this.format.yTitle = title
    }
    
    void legend(boolean show) {
        this.format.showLegend = show
    }
    
    void tooltips(boolean show) {
        this.format.showTooltips = show
    }
    
    void background(Color color) {
        this.format.backgroundColor = color
    }
    
    void background(def color) {
        this.background(ColorUtil.getColor(color))
    }
    
    void gridColor(Color color) {
        this.format.gridColor = color
    }
    
    void gridColor(def color) {
        this.gridColor(ColorUtil.getColor(color))
    }
    
    void foreColor(Color color) {
        this.format.foreColor = color
    }
    
    void foreColor(def color) {
        this.foreColor(ColorUtil.getColor(color))
    }
    
    void series(String... names) {
        this.series(Arrays.asList(names))
    }
    
    void series(List names) {
        this.format.seriesNames = names
    }
    
    void colors(Object... colors) {
        this.colors(Arrays.asList(colors))
    }
    
    void colors(Collection colors) {
        colors = colors.collect{ColorUtil.getColor(it)}
        if(colors.isEmpty())
            colors.add(Color.RED)
        this.format.setColorGenerator(new ListColorGenerator(colors))
    }
    
    PlotFormat getFormat() {
        this.format
    }
}

