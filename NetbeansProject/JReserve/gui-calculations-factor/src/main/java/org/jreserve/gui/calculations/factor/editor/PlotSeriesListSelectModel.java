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
package org.jreserve.gui.calculations.factor.editor;

import javax.swing.DefaultListSelectionModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PlotSeriesListSelectModel extends DefaultListSelectionModel {

    private boolean gestureStarted = false;
    
    @Override
    public void setSelectionInterval(int index0, int index1) {
        if(!gestureStarted) {
            if(isSelectedIndex(index0))
                super.removeSelectionInterval(index0, index0);
            else
                super.addSelectionInterval(index0, index0);
        }
        gestureStarted = true;
    }
    
    @Override
    public void setValueIsAdjusting(boolean isAdjusting) {
        if(isAdjusting == false)
            gestureStarted = false;
    }
}
