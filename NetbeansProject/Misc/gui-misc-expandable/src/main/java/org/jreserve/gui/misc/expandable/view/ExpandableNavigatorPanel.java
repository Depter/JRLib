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
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jreserve.gui.misc.expandable.ExpandableContainerHandler;
import org.jreserve.gui.misc.expandable.ExpandableElementDescription;
import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.netbeans.spi.navigator.NavigatorPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.ListView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NavigatorPanel.Registration(
    displayName = "#LBL.ExpandableNavigatorPanel.DisplayName",
    mimeType = ExpandableNavigatorPanel.MIME_TYPE
)
@Messages({
    "LBL.ExpandableNavigatorPanel.DisplayName=Navigator",
    "LBL.ExpandableNavigatorPanel.Hint=Navigate wihtin expandable views..."
})
public class ExpandableNavigatorPanel implements NavigatorPanel {
    
    final static String MIME_TYPE = "application/expandable-navigator";
    final static NavigatorLookupHint LOOKUP_HINT = new NavigatorLookupHint() {
        @Override
        public String getContentType() {
            return MIME_TYPE;
        }
    };
    private final static Lookup HINT_LOOKUP = Lookups.singleton(LOOKUP_HINT);
    
    private Panel panel;
    private NavigatorChildFactory childFactory;
    private Lookup tcLookup = Lookup.EMPTY;
    private Lookup myLookup;    
    
    @Override
    public String getDisplayName() {
        return Bundle.LBL_ExpandableNavigatorPanel_DisplayName();
    }

    @Override
    public String getDisplayHint() {
        return Bundle.LBL_ExpandableNavigatorPanel_DisplayName();
    }

    @Override
    public JComponent getComponent() {
        if(panel == null)
            panel = new Panel();
        return panel;
    }

    @Override
    public void panelActivated(Lookup lkp) {
        tcLookup = getTcLookup();
        updatePanel();
    }
    
    private Lookup getTcLookup() {
        TopComponent tc = TopComponent.getRegistry().getActivated();
        return tc==null? Lookup.EMPTY : tc.getLookup();
    }
    
    private void updatePanel() {
        if(childFactory != null)
            childFactory.refresh();
        Lookup panelLookup = panel==null? Lookup.EMPTY : panel.lookup;
        myLookup = new ProxyLookup(HINT_LOOKUP, panelLookup);
    }

    @Override
    public void panelDeactivated() {
        tcLookup = Lookup.EMPTY;
        updatePanel();
    }
    
    @Override
    public Lookup getLookup() {
        return myLookup;
    }
    
    private class Panel extends JPanel implements ExplorerManager.Provider {
        
        private ListView list;
        private final ExplorerManager em = new ExplorerManager();
        private Lookup lookup;
        
        private Panel() {
            childFactory = new NavigatorChildFactory();
            lookup = ExplorerUtils.createLookup(em, new ActionMap());
            initComponents();
            initNodes();
        }
        
        private void initNodes() {
            Children children = Children.create(childFactory, false);
            AbstractNode root = new AbstractNode(children);
            em.setRootContext(root);
        }
        
        private void initComponents() {
            setLayout(new BorderLayout());
            list = new ListView();
            add(list, BorderLayout.CENTER);
        }
        
        @Override
        public ExplorerManager getExplorerManager() {
            return em;
        }
    }
    
    private class NavigatorChildFactory extends ChildFactory<ExpandableElementDescription> {
        
        private ExpandableContainerHandler handler;
        
        @Override
        protected boolean createKeys(List<ExpandableElementDescription> list) {
            handler = tcLookup.lookup(ExpandableContainerHandler.class);
            if(handler != null)
                list.addAll(Arrays.asList(handler.getElements()));
            return true;
        }

        @Override
        protected Node createNodeForKey(ExpandableElementDescription key) {
            return new NavigatorNode(handler, key);
        }
        
        void refresh() {
            super.refresh(true);
        }
    }
    
    private static class NavigatorNode extends AbstractNode {
        
        private ExpandableElementDescription description;
        private ExpandableContainerHandler handler;
        
        private NavigatorNode(ExpandableContainerHandler handler, ExpandableElementDescription description) {
            super(Children.LEAF, description.getElement().getLookup());
            this.description = description;
            this.handler = handler;
            setDisplayName(description.getDisplayName());
            initIcon();
        }
        
        private void initIcon() {
            String iconBase = description.getIconBase();
            if(iconBase != null)
                setIconBaseWithExtension(iconBase);
        }

        @Override
        public Action getPreferredAction() {
            return new NavigateAction(handler, description);
        }
    }
    
    private static class NavigateAction extends AbstractAction {
        
        private ExpandableContainerHandler handler;
        private ExpandableElementDescription description;
        
        NavigateAction(ExpandableContainerHandler handler, ExpandableElementDescription description) {
            this.handler = handler;
            this.description = description;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            handler.navigateTo(description);
        }
    }
    
}
