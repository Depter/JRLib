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

import java.util.List;
import org.jreserve.gui.misc.expandable.registration.ExpandableElementLayerRegistrationLoader;
import org.jreserve.gui.misc.expandable.view.ExpandablePanelHandler;
import org.jreserve.gui.misc.expandable.view.ExpandableScrollHandler;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExpandableFactory {

    public static ExpandableContainerHandler createPanel(String mimeType) {
        Lookup context = Utilities.actionsGlobalContext();
        return createPanel(mimeType, context);
    }

    public static ExpandableContainerHandler createPanel(String mimeType, Lookup context) {
        ExpandableElementDescription[] elements = getDescriptions(mimeType, context);
        return createPanel(elements);
    }
    
    private static ExpandableElementDescription[] getDescriptions(String mimeType, Lookup context) {
        List<ExpandableElementDescription> elements = new ExpandableElementLayerRegistrationLoader(mimeType, context).getValues();
        ExpandableElementDescription[] result = new ExpandableElementDescription[elements.size()];
        return elements.toArray(result);
    }

    public static ExpandableContainerHandler createPanel(ExpandableElementDescription[] elements) {
        return new ExpandablePanelHandler(elements);
    }

    public static ExpandableContainerHandler createScrollPanel(String mimeType) {
        Lookup context = Utilities.actionsGlobalContext();
        return createScrollPanel(mimeType, context);
    }

    public static ExpandableContainerHandler createScrollPanel(String mimeType, Lookup context) {
        ExpandableElementDescription[] elements = getDescriptions(mimeType, context);
        return createScrollPanel(elements);
    }

    public static ExpandableContainerHandler createScrollPanel(ExpandableElementDescription[] elements) {
        return new ExpandableScrollHandler(elements);
    }
}
