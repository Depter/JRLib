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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jreserve.gui.misc.expandable.ExpandableComponentHandler;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.jreserve.gui.misc.expandable.ExpandableElementDescription;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExpandablePanel extends JPanel {
    
    private final static String DEFAULT_IMG = "org/openide/nodes/defaultNode.png";
    
    private ExpandableElementDescription description;
    private ExpandableElement element;
    private JLabel titleLabel;
    private JPanel contentPanel;
    private boolean docked = true;
    
    public ExpandablePanel(ExpandableElementDescription description) {
        this.description = description;
        this.element = description.getElement();
        
        super.setName(description.getDisplayName());
        initPanel();
    }
    
    private void initPanel() {
        setLayout(new BorderLayout());
        setBackground(element.getBackground());
        setOpaque(true);
        setBorder(ExpandablePanelBorder.getInstance());
        
        add(createTitlePanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new TitlePanel(new GridBagLayout());
        panel.setBackground(getBackground());
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0;
        gc.weightx=0d; gc.weighty=0d;
        gc.anchor=GridBagConstraints.BASELINE_LEADING;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 5, 0, 5);
        panel.add(createTitleLabel(), gc);
        
        gc.gridx=1; gc.weightx=1d;
        gc.anchor=GridBagConstraints.BASELINE_TRAILING;
        gc.insets = new Insets(0, 0, 0, 0);
        panel.add(Box.createHorizontalGlue(), gc);
        
        gc.gridx=2; gc.weightx=0d;
        panel.add(createButtons(), gc);
        
        return panel;
    }
    
    private JLabel createTitleLabel() {
        titleLabel = new JLabel(getName());
        titleLabel.setIcon(new ImageIcon(getImage()));
        titleLabel.setForeground(element.getForeground());
        Font font = titleLabel.getFont();
        titleLabel.setFont(font.deriveFont(Font.BOLD));
        return titleLabel;
    }
    
    private Image getImage() {
        String path = description.getIconBase();
        if(path == null || path.trim().length()==0)
            path = DEFAULT_IMG;
        return ImageUtilities.loadImage(path);
    }
    
    private JPanel createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        for(JComponent button : element.getFrameComponents()) {
            button.setForeground(element.getForeground());
            panel.add(button);
        }
        return panel;
    }
    
    private JPanel createContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        this.docked = true;
        contentPanel.add(element.getVisualComponent(), BorderLayout.CENTER);
        return contentPanel;
    }
    
    private class Handler implements ExpandableComponentHandler {

        @Override
        public void setTitle(String title) {
            titleLabel.setText(title);
            revalidate();
        }

        @Override
        public void setIcon(Image icon) {
            titleLabel.setIcon(new ImageIcon(icon));
            revalidate();
        }

        @Override
        public void minimize() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void maximize() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undock() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void dock() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    
    }
}