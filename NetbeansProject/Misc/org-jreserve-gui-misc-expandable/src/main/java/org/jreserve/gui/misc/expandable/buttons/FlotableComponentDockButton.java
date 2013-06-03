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
    "LBL.FlotableComponentDockButton.Dock=Dock",
    "LBL.FlotableComponentDockButton.UnDock=Float"
})
public class FlotableComponentDockButton extends FlotableComponentButton {
    
    private boolean docked = true;
    
    public void setDocked(boolean docked) {
        this.docked = docked;
        super.repaint();
        setToolTip();
    }
    
    private void setToolTip() {
        if(docked)
            setToolTipText(Bundle.LBL_FlotableComponentDockButton_UnDock());
        else
            setToolTipText(Bundle.LBL_FlotableComponentDockButton_Dock());
    }
    
    @Override 
    public void mouseClicked(MouseEvent e) {
        docked = !docked;
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
        
        g2.fillRect(2, 8, 6, 6);
        g2.drawLine(9, 6, 13, 2);
        
        if(docked) {
            paintDocked(g2);
        } else {
            paintUndocked(g2);
        }
        
        g2.dispose();
    }    
    
    private void paintDocked(Graphics2D g2) {
        g2.drawLine(11, 2, 13, 2);
        g2.drawLine(13, 2, 13, 4);
    }
    
    private void paintUndocked(Graphics2D g2) {
        g2.drawLine(9, 4, 9, 6);
        g2.drawLine(9, 6, 12, 6);
    }
}
