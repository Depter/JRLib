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

package org.jreserve.grscript.gui.plot.plotter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.openide.filesystems.FileUtil;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class UndockedTopComponent extends TopComponent {

//    private final static Mode MODE = WindowManager.getDefault().findMode("navigableUndocked");
    
    public static UndockedTopComponent create(String title, JComponent component) {
        UndockedTopComponent tc = new UndockedTopComponent(title, component);
        open(tc);
        undock(tc);
        return tc;
    }
    
    private static void open(UndockedTopComponent tc) {
        tc.open();
        tc.requestVisible();
        tc.requestActive();
    }
    
    private static void undock(UndockedTopComponent tc) {
        Action action = FileUtil.getConfigObject("Actions/Window/org-netbeans-core-windows-actions-UndockWindowAction.instance", Action.class);
        if(action != null && WindowManager.getDefault().getRegistry().getActivated() == tc) {
            action.actionPerformed(new ActionEvent(tc, ActionEvent.ACTION_FIRST, "open-tc"));
        }
    }
    
    private UndockedTopComponent(String title, JComponent content) {
        setDisplayName(title);
        initComponents(content);
        //setActionMap(new ActionMap());
    }
    
    private void initComponents(JComponent content) {
        super.setLayout(new BorderLayout());
        super.add(content, BorderLayout.CENTER);
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }    
    
    @Override
    public void componentOpened() {
    }

    void writeProperties(java.util.Properties p) {
    }

    void readProperties(java.util.Properties p) {
    }

}
