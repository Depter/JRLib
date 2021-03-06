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
package org.jreserve.gui.misc.expandable.view;

import javax.swing.JComponent;
import org.jreserve.gui.misc.expandable.ExpandableElementDescription;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExpandablePanelHandler extends AbstractExpandableContainerHandler {
    
    private ExpandableView component;

    public ExpandablePanelHandler(ExpandableElementDescription[] elements) {
        super(elements);
    }

    @Override
    protected JComponent createComponent() {
        component = new ExpandableView(this, getElements());
        return component;
    }

    @Override
    protected JComponent getComponentFor(ExpandableElementDescription description) {
        return component != null?
                component.getPanelForDescription(description) :
                null;
    }
}
