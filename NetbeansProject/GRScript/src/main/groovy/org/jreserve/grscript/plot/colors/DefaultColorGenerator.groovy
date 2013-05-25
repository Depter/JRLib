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

package org.jreserve.grscript.plot.colors

import java.awt.Color
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DefaultColorGenerator implements ColorGenerator {
    private static enum Component {
        RED,
        GREEN,
        BLUE
    }
    
    private int value = 255;
    private boolean isDouble = false;
    private Component component = Component.RED;
    private Color next = new Color(255, 0, 0);
    
    @Override
    public Color nextColor() {
        Color result = next;
        
        switch(component) {
            case Component.RED:
                stepFromRed();
                break;
            case Component.GREEN:
                stepFromGreen();
                break;
            default:
                stepFromBlue();
                break;
        }
        
        return result;
    }
    
    
    private void stepFromRed() {
        component = Component.GREEN;
        next = isDouble? new Color(value, 0, value) : new Color(0, value, 0);
    }
    
    private void stepFromGreen() {
        component = Component.BLUE;
        next = isDouble? new Color(0, value, value) : new Color(0, 0, value);
    }
    
    private void stepFromBlue() {
        component = Component.RED;
        isDouble = !isDouble;
        stepValue();
        next = isDouble? new Color(value, value, 0) : new Color(value, 0, 0);
    }
    
    private void stepValue() {
        if(!isDouble) {
            value = (value==255)? 128 : value/2;
            if(value < 32)
                value = 255;
        }
    }
    
    @Override
    public void reset() {
        value = 255;
        isDouble = false;
        component = Component.RED;
        next = new Color(255, 0, 0);
    }
}

