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
