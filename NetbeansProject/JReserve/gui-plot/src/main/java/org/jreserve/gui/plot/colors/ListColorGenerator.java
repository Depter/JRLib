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
package org.jreserve.gui.plot.colors;

import java.awt.Color;
import java.awt.Paint;
import java.util.Collection;
import org.jreserve.gui.plot.ColorGenerator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ListColorGenerator implements ColorGenerator {

    private Paint[] colors;
    private int size;
    private int index = 0;
    
    public ListColorGenerator(Collection<? extends Paint> colorList) {
        size = colorList.size();
        this.colors = colorList.toArray(new Paint[size]);
    }
    
    public ListColorGenerator(Paint... colors) {
        size = colors.length;
        this.colors = new Color[size];
        System.arraycopy(colors, 0, this.colors, 0, size);
    }

    @Override
    public Paint nextColor() {
        if(index == size)
            index = 0;
        return colors[index++];
    }	
    
    @Override
    public void reset() {
        index = 0;
    }
}
