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
package org.jreserve.gui.misc.lookupexplorer;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.AbstractNode;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//org.jreserve.gui.misc.lookupexplorer//LookupExplorer//EN",
    autostore = false
)
@TopComponent.Description(
    preferredID = "LookupExplorerTopComponent",
    iconBase = "org/jreserve/gui/misc/lookupexplorer/global.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(
    mode = "rightSlidingSide", 
    openAtStartup = false
)
@ActionID(
    category = "Window", 
    id = "org.jreserve.gui.misc.lookupexplorer.LookupExplorerTopComponent"
)
@ActionReferences({
    @ActionReference(path = "Menu/Window", position = 80)//,
    //@ActionReference(path = "Ribbon/TaskPanes/Windows/Development", position = 200)
})
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_LookupExplorerAction",
    preferredID = "LookupExplorerTopComponent")
@Messages({
    "CTL_LookupExplorerAction=Lookup Explorer",
    "CTL_LookupExplorerTopComponent=Lookup Explorer"
})
public final class LookupExplorerTopComponent extends TopComponent implements ExplorerManager.Provider, LookupListener {

    private final ExplorerManager em = new ExplorerManager();
    private Result<Object> result;
    private boolean active = false;
    
    public LookupExplorerTopComponent() {
        initComponents();
        setName(Bundle.CTL_LookupExplorerTopComponent());

    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    @Override
    public void resultChanged(LookupEvent le) {
        if(active)
            return;
        LookupChildFactory children = new LookupChildFactory(result.allInstances());
        AbstractNode root = new AbstractNode(children);
        em.setRootContext(root);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        list = new org.openide.explorer.view.ListView();

        setLayout(new java.awt.BorderLayout());
        add(list, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.openide.explorer.view.ListView list;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        result = Utilities.actionsGlobalContext().lookupResult(Object.class);
        result.addLookupListener(this);
        resultChanged(null);
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
    }

    void writeProperties(java.util.Properties p) {
    }

    void readProperties(java.util.Properties p) {
    }

    @Override
    protected void componentActivated() {
        active = true;
    }

    @Override
    protected void componentDeactivated() {
        active = false;
    }
}
