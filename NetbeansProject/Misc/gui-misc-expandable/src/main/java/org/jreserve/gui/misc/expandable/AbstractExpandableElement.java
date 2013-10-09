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
package org.jreserve.gui.misc.expandable;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import org.jreserve.gui.misc.expandable.buttons.FlotableComponentDockButton;
import org.jreserve.gui.misc.expandable.buttons.FlotableComponentOpenButton;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractExpandableElement implements ExpandableElement {

    public final static String MINIMIZE_ACTION = "minimize_panel";
    public final static String DOCK_ACTION = "dock_panel";
    private Component visualComponent;
    private JComponent[] frameComponents;
    private ExpandableComponentHandler handler;
    private ActionListener listener = new ButtonListener();
    private FlotableComponentDockButton dockButton;
    private UndockListener undockListenr = new UndockListener();

    @Override
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }
    
    @Override
    public Lookup getGlobalLookup() {
        return Lookup.EMPTY;
    }
    
    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }
    
    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    @Override
    public Component getVisualComponent() {
        if (visualComponent == null) {
            visualComponent = createVisualComponent();
        }
        return visualComponent;
    }

    protected abstract Component createVisualComponent();

    @Override
    public JComponent[] getFrameComponents() {
        if (frameComponents == null) {
            frameComponents = createFrameComponents();
        }
        return frameComponents;
    }

    protected JComponent[] createFrameComponents() {
        return new JComponent[]{
            createDockButton(),
            createMinimizeButton(openMaximized())
        };
    }

    protected boolean openMaximized() {
        return true;
    }

    protected JComponent createDockButton() {
        dockButton = new FlotableComponentDockButton();
        dockButton.setActionCommand(DOCK_ACTION);
        dockButton.addActionListener(listener);
        return dockButton;
    }

    protected JComponent createMinimizeButton(boolean isMaximized) {
        FlotableComponentOpenButton button = new FlotableComponentOpenButton();
        button.setOpened(isMaximized);
        button.setActionCommand(MINIMIZE_ACTION);
        button.addActionListener(listener);
        return button;
    }

    @Override
    public void setHandler(ExpandableComponentHandler handler) {
        releaseOldHandler();
        this.handler = handler;
        if (handler != null)
            initHandler();
    }

    private void releaseOldHandler() {
        if (handler != null) {
            handler.removePropertyChangeListener(undockListenr);
        }
    }

    private void initHandler() {
        if (openMaximized())
            handler.maximize();
        if (dockButton != null)
            handler.addPropertyChangeListener(undockListenr);
    }

    @Override
    public ExpandableComponentHandler getHandler() {
        return handler;
    }

    protected boolean processAction(ActionEvent evt) {
        String command = evt.getActionCommand();
        if (MINIMIZE_ACTION.equals(command)) {
            minMaxWindow();
            return true;
        } else if (DOCK_ACTION.equals(command)) {
            dockUndockWindow();
            return true;
        }
        return false;
    }

    private void minMaxWindow() {
        if (handler != null) {
            if (handler.isMaximized()) {
                handler.minimize();
            } else {
                handler.maximize();
            }
        }
    }

    private void dockUndockWindow() {
        if (handler != null) {
            if (handler.isDocked()) {
                handler.undock();
            } else {
                handler.dock();
            }
        }
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            processAction(e);
        }
    }

    private class UndockListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(isDockedChange(evt)) {
                dockButton.setDocked(handler.isDocked());
            }
        }
        
        private boolean isDockedChange(PropertyChangeEvent evt) {
            String prop = evt.getPropertyName();
            return ExpandableComponentHandler.DOCKED.equals(prop);
        }
    }
}
