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

package org.jreserve.gui.misc.expandable;

import java.awt.Image;
import java.beans.PropertyChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ExpandableComponentHandler {
    
    public final static String TITLE = "title";
    public final static String OPENED = "opened";
    public final static String DOCKED = "docked";
    
    public ExpandableContainerHandler getContainer();
    
    public void setTitle(String title);
    
    public void setIcon(Image icon);
    
    public boolean isMaximized();
    
    public void minimize();
    
    public void maximize();
    
    public boolean isDocked();
    
    public void undock();
    
    public void dock();
    
    public void addPropertyChangeListener(PropertyChangeListener listener);
    
    public void removePropertyChangeListener(PropertyChangeListener listener);
}
