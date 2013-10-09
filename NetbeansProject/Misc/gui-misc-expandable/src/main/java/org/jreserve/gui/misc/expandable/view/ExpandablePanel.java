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
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jreserve.gui.misc.expandable.ExpandableComponentHandler;
import org.jreserve.gui.misc.expandable.ExpandableContainerHandler;
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
    private final static int TITLE_MARGIN = 5;
    
    private ExpandableComponentHandler handler = new Handler();
    private TcCallback tcCallback = new TcCallback();
            
    private ExpandableElementDescription description;
    private ExpandableElement element;
    
    private JLabel titleLabel;
    private JPanel contentPanel;
    
    private UndockedTopComponent tc;
    private ExpandableContainerHandler container;
    private boolean docked = true;
    private boolean opened = false;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    //private SelectListener selectListener = new SelectListener();
    private ContainerSelectAdapter selectAdapter = new ContainerSelectAdapter();
    
    public ExpandablePanel(ExpandableContainerHandler container, ExpandableElementDescription description) {
        this.container = container;
        super.addContainerListener(selectAdapter);
        initElement(description);
        initSuperProperties();
        initPanel();
        this.element.setHandler(handler);
    }
    
    private void initElement(ExpandableElementDescription description) {
        this.description = description;
        this.element = description.getElement();
    }
    
    private void initSuperProperties() {
        setBackground(description.getBackground());
        setForeground(description.getForeground());
        setName(description.getDisplayName());
    }
    
    private void initPanel() {
        setOpaque(true);
        setLayout(new BorderLayout());
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
        gc.insets = new Insets(0, TITLE_MARGIN, 0, TITLE_MARGIN);
        panel.add(createTitleLabel(), gc);
        
        gc.gridx=1; gc.weightx=1d;
        gc.anchor=GridBagConstraints.BASELINE_TRAILING;
        gc.insets = new Insets(0, 0, 0, 0);
        panel.add(Box.createHorizontalGlue(), gc);
        
        gc.gridx=2; gc.weightx=0d;
        gc.insets = new Insets(0, 0, 0, TITLE_MARGIN);
        panel.add(createButtons(), gc);
        
        return panel;
    }
    
    private JLabel createTitleLabel() {
        titleLabel = new JLabel(getName());
        titleLabel.setIcon(new ImageIcon(getImage()));
        titleLabel.setForeground(getForeground());
        Font font = titleLabel.getFont();
        titleLabel.setFont(font.deriveFont(Font.BOLD));
        titleLabel.setOpaque(false);
        return titleLabel;
    }
    
    private Image getImage() {
        String path = description.getIconBase();
        if(path == null || path.trim().length()==0)
            path = DEFAULT_IMG;
        Image img = ImageUtilities.loadImage(path);
        return img!=null? img : ImageUtilities.loadImage(DEFAULT_IMG);
    }
    
    private JPanel createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        for(JComponent button : element.getFrameComponents()) {
            button.setForeground(getForeground());
            panel.add(button);
        }
        return panel;
    }
    
    private JPanel createContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        this.docked = true;
        contentPanel.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, getBackground()));
        return contentPanel;
    }
    
    ExpandableElementDescription getDescription() {
        return description;
    }
    
    private void setOpened(boolean opened) {
        if(this.opened != opened) {
            this.opened = opened;
            createContent();
            
            contentPanel.setVisible(opened);
            if(opened)
                container.setSelected(description);
            pcs.firePropertyChange(ExpandableComponentHandler.OPENED, !opened, opened);
        }
    }
    
    private boolean contentCreated = false;
    
    private void createContent() {
        if(!contentCreated) {
            Component c = element.getVisualComponent();
            contentPanel.add(c, BorderLayout.CENTER);
            contentCreated = true;
        }
    }
    
    private void setDocked(boolean docked) {
        this.docked = docked;
        pcs.firePropertyChange(ExpandableComponentHandler.DOCKED, !docked, docked);
    }
    
    ExpandableComponentHandler getComponentHandler() {
        return handler;
    }
    
    private class Handler implements ExpandableComponentHandler {
        
        @Override
        public ExpandableContainerHandler getContainer() {
            return container;
        }
        
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
        public boolean isMaximized() {
            return opened;
        }
        
        @Override
        public void minimize() {
            setOpened(false);
        }

        @Override
        public void maximize() {
            setOpened(true);
        }

        @Override
        public boolean isDocked() {
            return docked;
        }
        
        @Override
        public void undock() {
            if(docked) {
                String title = description.getDisplayName();
                if(contentCreated)
                    contentPanel.remove(element.getVisualComponent());
                tc = UndockedTopComponent.create(title, element, tcCallback);
                setDocked(false);
            }
        }

        @Override
        public void dock() {
            if(tc != null)
                tc.close();
        }
        
        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }
    
        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }
    
    private class ContainerSelectAdapter extends MouseAdapter implements ContainerListener {
        
        @Override
        public void componentAdded(ContainerEvent e) {
            register(e.getChild());
        }

        private void register(Component c) {
            if(c instanceof Container) {
                Container container = (Container) c;
                for(Component child : container.getComponents())
                    register(child);
                container.addContainerListener(this);
            }
            c.addMouseListener(this);
        }
        
        @Override
        public void componentRemoved(ContainerEvent e) {
            unregister(e.getChild());
        }

        private void unregister(Component c) {
            if(c instanceof Container) {
                Container container = (Container) c;
                for(Component child : container.getComponents())
                    unregister(child);
                container.removeContainerListener(this);
            }
            c.removeMouseListener(this);
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            container.setSelected(description);
        }
    }
    
    private class TcCallback implements UndockedTopComponent.Callback {

        @Override
        public void tcClosed() {
            tc = null;
            if(opened) {
                contentPanel.add(element.getVisualComponent(), BorderLayout.CENTER);
                contentCreated = true;
            }
            setDocked(true);
        }
    }
}
