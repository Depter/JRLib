package org.jreserve.grscript.gui.script.explorer;

import org.jreserve.grscript.gui.script.explorer.nodes.ScriptFolderNode;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//org.jreserve.grscript.gui.script.explorer//ScriptExplorer//EN",
    autostore = false
)
@TopComponent.Description(
    preferredID = "ScriptExplorerTopComponent",
    //iconBase="SET/PATH/TO/ICON/HERE", 
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(
    mode = "explorer", 
    openAtStartup = true
)
@ActionID(
    category = "Window", 
    id = "org.jreserve.grscript.gui.script.explorer.ScriptExplorerTopComponent"
)
@ActionReference(
    path = "Menu/Window" /*, position = 333 */
)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_ScriptExplorerAction",
    preferredID = "ScriptExplorerTopComponent")
@Messages({
    "CTL_ScriptExplorerAction=Script Explorer",
    "CTL_ScriptExplorerTopComponent=Scripts",
    "HINT_ScriptExplorerTopComponent=Manage scripts"
})
public final class ScriptExplorerTopComponent extends TopComponent implements ExplorerManager.Provider {

    private final static ExplorerManager em = new ExplorerManager();
    
    public ScriptExplorerTopComponent() {
        initComponents();
        setName(Bundle.CTL_ScriptExplorerTopComponent());
        setToolTipText(Bundle.HINT_ScriptExplorerTopComponent());

        em.setRootContext(new ScriptFolderNode());
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tree = new org.openide.explorer.view.BeanTreeView();

        setLayout(new java.awt.BorderLayout());

        tree.setRootVisible(false);
        add(tree, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.openide.explorer.view.BeanTreeView tree;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    void writeProperties(java.util.Properties p) {
    }

    void readProperties(java.util.Properties p) {
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
}
