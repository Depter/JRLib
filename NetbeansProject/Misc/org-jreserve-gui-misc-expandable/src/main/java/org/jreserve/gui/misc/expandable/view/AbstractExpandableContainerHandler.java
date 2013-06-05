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

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.jreserve.gui.misc.expandable.ExpandableContainerHandler;
import org.jreserve.gui.misc.expandable.ExpandableElementDescription;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 */
abstract class AbstractExpandableContainerHandler implements ExpandableContainerHandler {

    private final static String FOCUS_OWNER = "focusOwner";
    private final static int UNDO_LIMIT = 30;
    
    private ExpandableElementDescription[] elements;
    private JComponent component;
    private FocusListenr focusListener;
    private ExpandableElementDescription selected;
    private ExtendableLookup lookup = new ExtendableLookup();
    private UndoRedo.Manager undoRedo;
    
    protected AbstractExpandableContainerHandler(ExpandableElementDescription[] elements) {
        this.elements = elements;
    }
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    @Override
    public UndoRedo.Manager getUndoRedo() {
        if(undoRedo == null) {
            undoRedo = new UndoRedo.Manager();
            undoRedo.setLimit(UNDO_LIMIT);
        }
        return undoRedo;
    }
    
    @Override
    public JComponent getComponent() {
        if(component == null) {
            this.component = createComponent();
            initFocusListenr();
        }
        return component;
    }
    
    protected abstract JComponent createComponent();

    private void initFocusListenr() {
        focusListener = new FocusListenr();
        KeyboardFocusManager mgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        mgr.addPropertyChangeListener(FOCUS_OWNER, focusListener);
    }
    
    @Override
    public ExpandableElementDescription[] getElements() {
        return elements;
    }

    @Override
    public void componentOpened() {
        for(ExpandableElementDescription description : elements)
            description.getElement().componentOpened();
    }

    @Override
    public void componentClosed() {
        removeFocusListener();
        for(ExpandableElementDescription description : elements)
            description.getElement().componentClosed();
    }
    
    private void removeFocusListener() {
        if(focusListener != null) {
            KeyboardFocusManager mgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            mgr.removePropertyChangeListener(focusListener);
            focusListener = null;
        }
    }

    @Override
    public void componentShowing() {
        for(ExpandableElementDescription description : elements)
            description.getElement().componentShowing();
    }

    @Override
    public void componentHidden() {
        for(ExpandableElementDescription description : elements)
            description.getElement().componentHidden();
    }

    @Override
    public void componentActivated() {
        for(ExpandableElementDescription description : elements)
            description.getElement().componentActivated();
    }

    @Override
    public void componentDeactivated() {
        for(ExpandableElementDescription description : elements)
            description.getElement().componentDeactivated();
    }
    
    @Override
    public CloseOperationState canCloseElement() {
        List<CloseOperationState> states = getInvalidStates();
        return states.isEmpty()?
                CloseOperationState.STATE_OK :
                createCompositeState(states);
    }
    
    private List<CloseOperationState> getInvalidStates() {
        List<CloseOperationState> states = new ArrayList<CloseOperationState>(elements.length);
        for(ExpandableElementDescription description : elements) {
            CloseOperationState state = description.getElement().canCloseElement();
            if(CloseOperationState.STATE_OK != state)
                states.add(state);
        }
        return states;
    }
    
    private CloseOperationState createCompositeState(List<CloseOperationState> states) {
        StringBuilder id = new StringBuilder();
        StateAction proceed = new StateAction();
        StateAction discard = new StateAction();
        for(CloseOperationState state : states) {
            if(id.length() > 0)
                id.append(" | ");
            id.append(state.getCloseWarningID());
            
            proceed.actions.add(state.getProceedAction());
            discard.actions.add(state.getDiscardAction());
        }
        
        return MultiViewFactory.createUnsafeCloseState(id.toString(), proceed, discard);
    }
    
    
    protected abstract JComponent getComponentFor(ExpandableElementDescription description);
    
    private class FocusListenr implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Object owner = evt.getNewValue();
            if(owner instanceof Component) {
                focusChanged((Component) owner);
                lookup.updateLookups();
            }
        }
        
        private void focusChanged(Component owner) {
            selected = null;
            for(ExpandableElementDescription description : elements) {
                JComponent container = getComponentFor(description);
                if(SwingUtilities.isDescendingFrom(owner, container)) {
                    selected = description;
                    return;
                }
            }
        }
    }
    
    private class ExtendableLookup extends ProxyLookup {

        public ExtendableLookup() {
            super(Lookup.EMPTY);
        }
        
        void updateLookups() {
            Lookup lkp = selected==null? Lookup.EMPTY : selected.getElement().getLookup();
            setLookups(lkp);
        }
    }    
    
    private class StateAction extends AbstractAction {
        
        private List<Action> actions = new ArrayList<Action>();
        
        @Override
        public void actionPerformed(ActionEvent e) {
            for(Action action : actions)
                if(action != null)
                    action.actionPerformed(e);
        }
    }
}
