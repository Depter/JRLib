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

package org.jreserve.gui.misc.utils.widgets;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.JLabel;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MessageLabel extends JLabel {
    
    private final static Icon ERROR_ICON = ImageUtilities.loadImageIcon("org/netbeans/modules/dialogs/error.gif", false);
    private final static Icon WARNING_ICON = ImageUtilities.loadImageIcon("org/netbeans/modules/dialogs/warning.gif", false);
    private final static Color ERROR_COLOR = Color.RED;
    private final static Color WARNING_COLOR = Color.BLACK;
    private final Color originalBg; 
    
    public MessageLabel() {
        originalBg = getBackground();
        setBackground(Color.WHITE);
    }
    
    public void setErrorMessage(String msg) {
        if(msg==null || msg.length()==0) {
            clearMessage();
        } else {
            setIcon(ERROR_ICON);
            setText(msg);
            setForeground(ERROR_COLOR);
        }
    }
    
    public void clearMessage() {
        setText(null);
        setIcon(EmptyIcon.EMPTY_16);
        setBackground(originalBg);
    }
    
    public void setWarningMessage(String msg) {
        if(msg==null || msg.length()==0) {
            clearMessage();
        } else {
            setIcon(WARNING_ICON);
            setText(msg);
            setForeground(WARNING_COLOR);
        }
    }
}
