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

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MsgLabel extends JLabel {
    
    private final static Color NORMAL_COLOR = new JLabel().getForeground();
    private final static Color ERR_COLOR = Color.RED;
    private final static Color INFO_COLOR = ERR_COLOR;
    private final static Color WARNING_COLOR = NORMAL_COLOR;
    
    private static enum State {
        ERROR(ERR_COLOR, CommonIcons.error()),
        WARNING(WARNING_COLOR, CommonIcons.warning()),
        INFO(INFO_COLOR, CommonIcons.info()),
        NORMAL(NORMAL_COLOR, EmptyIcon.EMPTY_16);
        
        private final Color foreGround;
        private final Icon icon;

        private State(Color foreGround, Icon icon) {
            this.foreGround = foreGround;
            this.icon = icon;
        }
        
        Color getForeground() {
            return foreGround;
        }
        
        Icon getIcon() {
            return icon;
        }
    }
    
    public MsgLabel() {
        super.setIcon(EmptyIcon.EMPTY_16);
        super.setForeground(INFO_COLOR);
    }
    
    public void showError(String msg) {
        showMsg(State.ERROR, msg);
    }

    private void showMsg(State state, String msg) {
        if(msg == null)
            state = State.NORMAL;
        super.setText(msg);
        super.setIcon(state.getIcon());
        super.setForeground(state.getForeground());
    }
    
    public void showWarning(String msg) {
        showMsg(State.ERROR, msg);
    }
    
    public void showInfo(String msg) {
        showMsg(State.INFO, msg);
    }
    
    public void clearMessage() {
        showMsg(State.NORMAL, null);
    }
    
    @Override
    public void setText(String msg) {
        showInfo(msg);
    }
}
