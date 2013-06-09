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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jreserve.gui.misc.expandable.ExpandableContainerHandler;
import org.jreserve.gui.misc.expandable.ExpandableElementDescription;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ExpandableView extends JPanel {
    private final static int BORDER_WIDHT = 15;
    private final static int COMPONENT_SPACING = 5;
    
    private ExpandableContainerHandler handler;
    private ExpandableElementDescription[] descriptions;
    private List<ExpandablePanel> panels;
    
    ExpandableView(ExpandableContainerHandler handler, ExpandableElementDescription[] descriptions) {
        super(new ExpandableLayout(COMPONENT_SPACING));
        this.handler = handler;
        this.descriptions = descriptions;
        initComponents();
    }
    
    private void initComponents() {
        addPanels();
        add(Box.createVerticalGlue());
        setBorder(BorderFactory.createEmptyBorder(BORDER_WIDHT, BORDER_WIDHT, BORDER_WIDHT, BORDER_WIDHT));
        
    }
    
    private void addPanels() {
        panels = new ArrayList<ExpandablePanel>(descriptions.length);
        for(ExpandableElementDescription desc : descriptions) {
            ExpandablePanel panel = new ExpandablePanel(handler, desc);
            panels.add(panel);
            add(panel);
        }
    }
    
    JComponent getPanelForDescription(ExpandableElementDescription description) {
        int index = getIndexOfDescription(description);
        return index < 0? null : panels.get(index);
    }
    
    private int getIndexOfDescription(ExpandableElementDescription description) {
        int size = descriptions.length;
        for(int i=0; i<size; i++)
            if(descriptions[i] == description)
                return i;
        return -1;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(10, 10);
    }
}
