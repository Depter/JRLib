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

package org.jreserve.gui.misc.utils.actions;

import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;
import javax.swing.Icon;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.netbeans.spi.actions.AbstractSavable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractDisplayableSavable extends AbstractSavable implements Icon {

    private final Displayable displayable;
    
    protected AbstractDisplayableSavable(Displayable displayable) {
        this.displayable = displayable;
    }
    
    @Override
    protected String findDisplayName() {
        return displayable.getDisplayName();
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        displayable.getIcon().paintIcon(c, g, x, y);
    }

    @Override
    public int getIconWidth() {
        return displayable.getIcon().getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return displayable.getIcon().getIconHeight();
    }
}
