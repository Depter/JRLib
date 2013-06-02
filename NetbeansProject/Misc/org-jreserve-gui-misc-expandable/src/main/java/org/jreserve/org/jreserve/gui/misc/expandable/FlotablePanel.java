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
package org.jreserve.org.jreserve.gui.misc.expandable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.jreserve.org.jreserve.gui.misc.expandable.buttons.FlotableComponentDockButton;
import org.jreserve.org.jreserve.gui.misc.expandable.buttons.FlotableComponentOpenButton;
import org.jreserve.org.jreserve.gui.misc.expandable.undocked.DockTarget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class FlotablePanel extends JPanel implements ActionListener, DockTarget {

    private final static Color BACKGROUND = new Color(67, 196, 67);
    private final static Color FOREGROUND = Color.WHITE;
    private final static int BORDER_WIDTH = 2;
    
    private Image icon;
    
    private JPanel contentPanel;
    private JPanel titlePanel;
    private JLabel titleLabel;
    
    private JPanel buttonPanel;
    private FlotableComponentOpenButton openButton;
    private FlotableComponentDockButton dockButton;
    
    private JPanel userButtonPanel;
    private List<JComponent> userButtons = new ArrayList<JComponent>();
    
    private Color background = BACKGROUND;
    private Color foreground = FOREGROUND;
    private boolean opened = true;
    
    private boolean docked = true;
    private FlotableComponent component;
    
//    private UndockedTopComponent tc = null;
    
    FlotablePanel(FlotableComponent component) {
        this.component = component;
        super.setName(component.getDisplayName());
        this.icon = component.getIcon();
        initPanel();
    }
    
    private void initPanel() {
        setLayout(new BorderLayout());
        initTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        setBorder(new LineBorder(background, BORDER_WIDTH, true));
    }
    
    private void initTitlePanel() {
        titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(background);
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0;
        gc.weightx=0d; gc.weighty=0d;
        gc.anchor=GridBagConstraints.BASELINE_LEADING;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 0, 5);
        
        titleLabel = new JLabel(getName());
        if(icon != null)
            titleLabel.setIcon(new ImageIcon(icon));
        titleLabel.setForeground(foreground);
        Font font = titleLabel.getFont();
        titleLabel.setFont(font.deriveFont(Font.BOLD));
        titlePanel.add(titleLabel, gc);
        
        gc.gridx=1; gc.weightx=1d;
        gc.anchor=GridBagConstraints.BASELINE_TRAILING;
        gc.insets = new Insets(0, 0, 0, 0);
        titlePanel.add(Box.createHorizontalGlue(), gc);
        
        gc.gridx=2; gc.weightx=0d;
        userButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        userButtonPanel.setOpaque(false);
        titlePanel.add(userButtonPanel, gc);
        
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setOpaque(false);
        dockButton = new FlotableComponentDockButton();
        dockButton.setForeground(foreground);
        dockButton.addActionListener(this);
        buttonPanel.add(dockButton);
        
        buttonPanel.add(Box.createHorizontalStrut(5));
        openButton = new FlotableComponentOpenButton();
        openButton.setForeground(foreground);
        openButton.addActionListener(this);
        buttonPanel.add(openButton);
        
        gc.gridx=3; gc.weightx=0d;
        titlePanel.add(buttonPanel, gc);
        
        titlePanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 2, 1));
        titlePanel.addMouseListener(new DblClickHandler());
    }

    private class DblClickHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2)
                setOpened(!opened);
        }
    }
}
