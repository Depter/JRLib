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
package org.jreserve.gui.misc.flamingo.api;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ActionMenuButton extends JCommandMenuButton {
    
    public ActionMenuButton(String iconBase, String text, Action action, CommandButtonKind type) {
        this(ResizableIcons.fromResource(iconBase), text,action, type);
    }
    
    public ActionMenuButton(
            ResizableIcon icon, String text, 
            final Action action, CommandButtonKind type) {
        super(text, icon);
        setCommandButtonKind(type);
        if(action != null) {
            addActionListener(action);
            action.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if("enabled".equals(evt.getPropertyName()))
                        setEnabled(action.isEnabled());
                }
            });
            setEnabled(action.isEnabled());
        }
    }
}
