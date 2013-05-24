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
package org.jreserve.grscript.gui.script.explorer.nodes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.SwingWorker;
import org.jreserve.grscript.gui.notificationutil.BubbleUtil;
import org.jreserve.grscript.gui.script.registry.ScriptFile;
import org.jreserve.grscript.gui.script.registry.ScriptFolder;
import org.openide.actions.DeleteAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ScriptFileNode extends AbstractNode {
    
    public final static String ACTION_PATH = "Scripts/Actions/Nodes/ScriptFileNode";
    private ScriptFile file;

    public ScriptFileNode(ScriptFile file) {
        super(Children.LEAF, Lookups.singleton(file));
        this.file = file;
        setDisplayName(file.getName());
    }
    

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        ScriptFolder parent = file.getParent();
        if(parent != null) {
            parent.removeFile(file);
            deleteFile();
        }
    }
    
    private void deleteFile() {
        final File f = new File(file.getPath()); 
        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                f.delete();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception ex) {
                    BubbleUtil.showException(ex);
                }
            }
        };
        worker.execute();
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = new ArrayList<Action>(Utilities.actionsForPath(ACTION_PATH));
        Action delete = SystemAction.get(DeleteAction.class);
        if(!actions.contains(delete))
            actions.add(delete);
        return actions.toArray(new Action[actions.size()]);
    }
    
}
