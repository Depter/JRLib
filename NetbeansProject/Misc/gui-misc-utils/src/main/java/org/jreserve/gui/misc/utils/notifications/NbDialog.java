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

package org.jreserve.gui.misc.utils.notifications;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JDialog;
import org.openide.windows.WindowManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class NbDialog extends JDialog {
    
    NbDialog(String title, Component content) {
        super(WindowManager.getDefault().getMainWindow(), title);
        getContentPane().add(content);
        pack();
        centerDialog();
    }
    
    private void centerDialog() {
        Point ownerLoc = getOwner().getLocationOnScreen();
        Dimension ownerSize = getOwner().getSize();
        Dimension dSize = getSize();
        
        int x = ownerLoc.x + (ownerSize.width - dSize.width)/2;
        int y = ownerLoc.y + (ownerSize.height - dSize.height)/2;
        setLocation(x, y);
    }
    
}
