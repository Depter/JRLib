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

package org.jreserve.dummy.plot.charts;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PlotLabel implements Comparable<PlotLabel> {
	
    private int index;
    private String name;
    
    PlotLabel(int index, String name) {
        this.index = index;
        this.name = name;
    }
    
    @Override
    public int compareTo(PlotLabel pl) {
        return (pl==null)? -1 : index - pl.index;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
