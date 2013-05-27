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
package org.jreserve.grscript.gui.script.examples.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jreserve.grscript.gui.script.explorer.nodes.ScriptFolderNode;
import org.jreserve.grscript.gui.script.registry.ScriptFolder;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

@ActionID(
    category = "File",
    id = "org.jreserve.grscript.gui.script.examples.actions.Examples"
)
@ActionRegistration(
    displayName = "#CTL_Examples", lazy = false
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1378),
    @ActionReference(path = ScriptFolderNode.ACTION_PATH, position = 325)
})
@Messages("CTL_Examples=Examples")
public final class Examples extends AbstractAction 
implements Presenter.Popup, Presenter.Menu {
    private final static String PATH = "Scripts/Examples";
    
    
    private static Comparator<FileObject> COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject o1, FileObject o2) {
            int p1 = getPosition(o1);
            int p2 = getPosition(o2);
            return p1-p2;
        }
        
        private int getPosition(FileObject fo) {
            Object p = fo.getAttribute("position");
            if(p instanceof Integer)
                return ((Integer)p).intValue();
            return Integer.MAX_VALUE;
        }
    };
    //private final ScriptFolder context;

    
    private JMenu menu;
    
    public Examples() {
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
    }

    @Override
    public JMenuItem getPopupPresenter() {
        return createMenu();
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return createMenu();
    }
    
    private JMenu createMenu() {
        JMenu m = new JMenu(Bundle.CTL_Examples());
        
        
        ScriptFolder folder = lookupFolder();
        if(folder == null) {
            m.setEnabled(false);
        } else {
            for(FileObject f : loadFiles())
                m.add(new ExampleAction(folder, f));
        }
        return m;
    }
    
    private List<FileObject> loadFiles() {
        FileObject folder = FileUtil.getConfigFile(PATH);
        List<FileObject> files = new ArrayList<FileObject>();
        if(folder != null) {
            Enumeration<? extends FileObject> e = folder.getData(false);
            while(e.hasMoreElements())
                files.add(e.nextElement());
        }
        Collections.sort(files, COMPARATOR);
        return files;
    }
    
    private ScriptFolder lookupFolder() {
        Collection<? extends ScriptFolder> folders = Utilities.actionsGlobalContext().lookupAll(ScriptFolder.class);
        if(folders.size() != 1)
            return null;
        return folders.iterator().next();
    }
}
