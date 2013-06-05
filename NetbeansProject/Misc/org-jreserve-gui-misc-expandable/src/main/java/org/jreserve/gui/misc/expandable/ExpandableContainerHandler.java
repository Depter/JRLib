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

import javax.swing.JComponent;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 */
public interface ExpandableContainerHandler extends Lookup.Provider {
    
    public JComponent getComponent();
    
    public ExpandableElementDescription[] getElements();
    
    public void componentOpened();

    public void componentClosed();

    public void componentShowing();

    public void componentHidden();

    public void componentActivated();

    public void componentDeactivated();
    
    public CloseOperationState canCloseElement();
    
    public UndoRedo.Manager getUndoRedo();
}
