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
package org.jreserve.gui.misc.expandable.buttons;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.FlotableComponentOpenButton.Open=Show",
    "LBL.FlotableComponentOpenButton.Close=Hide"
})
public class FlotableComponentOpenButton extends FlotableComponentButton {
    
    private boolean opened = true;
    
    public void setOpened(boolean opened) {
        this.opened = opened;
        super.repaint();
        setToolTip();
    }
    
    private void setToolTip() {
        if(opened)
            setToolTipText(Bundle.LBL_FlotableComponentOpenButton_Close());
        else
            setToolTipText(Bundle.LBL_FlotableComponentOpenButton_Open());
    }
    
    @Override 
    public void mouseClicked(MouseEvent e) {
        opened = !opened;
        setToolTip();
        super.mouseClicked(e);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(super.getForeground());
        g2.setStroke(new BasicStroke(1));
        
        if(pressed)
            g2.translate(1, 1);
        
        g2.drawLine(3, 7, 12, 7);
        g2.drawLine(3, 8, 12, 8);
        if(!opened) {
            g2.drawLine(7, 3, 7, 12);
            g2.drawLine(8, 3, 8, 12);
        }
        g2.dispose();
    }    
}

