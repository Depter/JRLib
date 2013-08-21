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
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class UndockedTopComponent extends TopComponent {
    
    private final static String UNDOCK_ACTION = "Actions/Window/org-netbeans-core-windows-actions-UndockWindowAction.instance";
    
    static UndockedTopComponent create(String title, ExpandableElement element, Callback callback) {//, DockTarget target) {
        UndockedTopComponent tc = new UndockedTopComponent(title, element, callback);
        open(tc);
        undock(tc);
        tc.opening = false;
        return tc;
    }
    
    private static void open(UndockedTopComponent tc) {
        tc.open();
        tc.requestVisible();
        tc.requestActive();
    }
    
    private static void undock(UndockedTopComponent tc) {
        Action action = FileUtil.getConfigObject(UNDOCK_ACTION, Action.class);
        if(action != null && WindowManager.getDefault().getRegistry().getActivated() == tc) {
            action.actionPerformed(new ActionEvent(tc, ActionEvent.ACTION_FIRST, "open-tc"));
        }
    }
    
    private JPanel contentPane = new JPanel(new BorderLayout());
    private ExpandableElement element;
    private boolean opening = true;
    private Callback callback;
    
    private UndockedTopComponent(String title, ExpandableElement element, Callback callback) {//, DockTarget source) {
        this.element = element;
        this.callback = callback;
        setDisplayName(title);
        initComponents();
        setActionMap(new ActionMap());
    }
    
    private void initComponents() {
        super.setLayout(new BorderLayout());
        super.add(new JScrollPane(contentPane), BorderLayout.CENTER);
        contentPane.add(element.getVisualComponent());
    }

    @Override
    public Lookup getLookup() {
        return element.getLookup();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }    
    
    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        Component c = element.getVisualComponent();
        if(!opening && c!=null)
            contentPane.remove(c);
        
        if(callback != null)
            callback.tcClosed();
    }

    void writeProperties(java.util.Properties p) {
    }

    void readProperties(java.util.Properties p) {
    }
    
    interface Callback {
        void tcClosed();
    }
}
