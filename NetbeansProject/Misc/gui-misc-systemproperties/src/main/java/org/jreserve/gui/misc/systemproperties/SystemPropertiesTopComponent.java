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
package org.jreserve.gui.misc.systemproperties;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.ExecutionException;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.gui.misc.utils.widgets.TextPrompt;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;
import org.netbeans.swing.outline.RowModel;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//org.jreserve.gui.misc.systemproperties//SystemProperties//EN",
    autostore = false
)
@TopComponent.Description(
    preferredID = "SystemPropertiesTopComponent",
    iconBase = "org/jreserve/gui/misc/systemproperties/sys_props.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(
    mode = "rightSlidingSide", 
    openAtStartup = false
)
@ActionID(
    category = "Window", 
    id = "org.jreserve.gui.misc.systemproperties.SystemPropertiesTopComponent"
)
@ActionReferences({
    @ActionReference(path = "Menu/Window", position = 90),
    @ActionReference(path = "Ribbon/TaskPanes/Windows/Development", position = 300)
})
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_SystemPropertiesAction",
    preferredID = "SystemPropertiesTopComponent")
@Messages({
    "CTL_SystemPropertiesAction=System Properties",
    "CTL_SystemPropertiesTopComponent=System Properties",
    "CTL.SystemPropertiesTopComponent.SearchPrompt=Search"
})
public final class SystemPropertiesTopComponent extends TopComponent {
    
    private final static String IMG_HOME = "org/jreserve/gui/misc/systemproperties/";
    private final static Icon SEARCH_IMG = ImageUtilities.loadImageIcon(IMG_HOME+"search.png", false);
    private final static Icon REFRESH_IMG = ImageUtilities.loadImageIcon(IMG_HOME+"refresh.png", false);
    private final static String TREE = "tree";
    private final static String LIST = "list";
    
    private CardLayout viewLayout = new CardLayout();
    private PropertyTableModel treeModel;
    private PropertyListModel listModel;
    private TextPrompt searchPrompt;
    
    public SystemPropertiesTopComponent() {
        setName(Bundle.CTL_SystemPropertiesTopComponent());
        initComponents();
        initSearchPrompt();
        
        PropertyItem root = new PropertyItem(null, "Root");
        root.getChildren().add(new PropertyItem("Loading...", "Loading"));
        initTree(root);
        initList(root);
        viewLayout.show(contentPanel, TREE);
        loadProperties();
    }
    
    private void initSearchPrompt() {
        searchPrompt = new TextPrompt(Bundle.CTL_SystemPropertiesTopComponent_SearchPrompt(), searchText, TextPrompt.PromptStyle.FOCUS_LOST);
        searchPrompt.setForeground(Color.GRAY);
        searchPrompt.changeAlpha(0.5f);
        searchPrompt.changeStyle(Font.BOLD + Font.ITALIC);
        searchPrompt.setIcon(SEARCH_IMG);
    }
    
    private void initTree(PropertyItem root) {
        treeModel = new PropertyTableModel(root);
        RowModel rowModel = new PropertyRowModel();
        OutlineModel model = DefaultOutlineModel.createOutlineModel(treeModel, rowModel, false, "Property");
        Outline outline = new Outline(model);
        outline.setRootVisible(false);
        outline.setFillsViewportHeight(true);
        outline.setShowHorizontalLines(true);
        outline.setRenderDataProvider(new RenderData());
        JScrollPane scroll = new JScrollPane(outline);
        contentPanel.add(scroll, TREE);
    }
    
    private void initList(PropertyItem root) {
        listModel = new PropertyListModel(root);
        RowModel rowModel = new PropertyRowModel();
        OutlineModel model = DefaultOutlineModel.createOutlineModel(listModel, rowModel, false, "Property");
        Outline outline = new Outline(model);
        outline.setRootVisible(false);
        outline.setFillsViewportHeight(true);
        outline.setShowHorizontalLines(true);
        outline.setRenderDataProvider(new RenderData());
        JScrollPane scroll = new JScrollPane(outline);
        contentPanel.add(scroll, LIST);
    }
    
    private void loadProperties() {
        refreshButton.setEnabled(false);
        searchText.setEnabled(false);
        
        PropertyLoader loader = new PropertyLoader();
        loader.execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        toolBarPanel = new javax.swing.JPanel();
        searchText = new javax.swing.JTextField();
        toolBarFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        refreshButton = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        toolBarPanel.setLayout(new java.awt.GridBagLayout());

        searchText.setText(null);
        searchText.getDocument().addDocumentListener(new SearchListener());
        searchText.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        toolBarPanel.add(searchText, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        toolBarPanel.add(toolBarFiller, gridBagConstraints);

        refreshButton.setIcon(REFRESH_IMG);
        org.openide.awt.Mnemonics.setLocalizedText(refreshButton, org.openide.util.NbBundle.getMessage(SystemPropertiesTopComponent.class, "LBL.SystemPropertiesTopComponent")); // NOI18N
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        toolBarPanel.add(refreshButton, gridBagConstraints);

        add(toolBarPanel, java.awt.BorderLayout.PAGE_START);

        contentPanel.setLayout(new java.awt.CardLayout());
        contentPanel.setLayout(viewLayout);
        add(contentPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        loadProperties();
    }//GEN-LAST:event_refreshButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTextField searchText;
    private javax.swing.Box.Filler toolBarFiller;
    private javax.swing.JPanel toolBarPanel;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    
    private class SearchListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
        
        private void update() {
            String search = getSearchString();
            listModel.setSearch(search);
            if(search == null) {
                viewLayout.show(contentPanel, TREE);
            } else {
                viewLayout.show(contentPanel, LIST);
            }
        }
        
        private String getSearchString() {
            String search = searchText.getText();
            if(search == null)
                return null;
            search = search.trim();
            return search.length()==0? null : search.toLowerCase();
        }
    }
    
    private class PropertyLoader extends SwingWorker<PropertyItem, Object> {

        PropertyLoader() {
        }

        @Override
        protected PropertyItem doInBackground() throws Exception {
            return PropertyItem.createRoot();
        }

        @Override
        protected void done() {
            try {
                PropertyItem item = get();
                treeModel.setRoot(item);
                listModel.setRoot(item);
            } catch (InterruptedException ex) {
            
            } catch (ExecutionException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                searchText.setEnabled(true);
                refreshButton.setEnabled(true);
            }
        }
        
        
    
    }
}
