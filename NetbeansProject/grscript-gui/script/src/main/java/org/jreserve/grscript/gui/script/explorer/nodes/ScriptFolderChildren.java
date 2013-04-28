package org.jreserve.grscript.gui.script.explorer.nodes;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.grscript.gui.eventbus.EventBusListener;
import org.jreserve.grscript.gui.eventbus.EventBusRegistry;
import org.jreserve.grscript.gui.notificationutil.BubbleUtil;
import org.jreserve.grscript.gui.script.explorer.data.AbstractScriptFile;
import org.jreserve.grscript.gui.script.explorer.data.ScriptEvent;
import org.jreserve.grscript.gui.script.explorer.data.ScriptFile;
import org.jreserve.grscript.gui.script.explorer.data.ScriptFolder;
import org.jreserve.grscript.gui.script.explorer.data.ScriptRegistry;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.ScriptFolderChildren.LoadError=Unable to load script!"
})
public class ScriptFolderChildren extends ChildFactory<AbstractScriptFile> {

    private final static Logger logger = Logger.getLogger(ScriptFolderChildren.class.getName());
    
    private ScriptFolder root;
    private ScriptEventListener listener;
    
    public ScriptFolderChildren() {
        this(null);
    }

    public ScriptFolderChildren(ScriptFolder root) {
        this.root = root;
        this.listener = new ScriptEventListener();
        EventBusRegistry.getDefault().subscribe(ScriptEvent.class, listener);
    }
    
    
    @Override
    protected boolean createKeys(List<AbstractScriptFile> list) {
        if(root == null)
            root = ScriptRegistry.getRoot();
        list.addAll(root.getFolders());
        list.addAll(root.getFiles());
        return true;
    }

    @Override
    protected Node createNodeForKey(AbstractScriptFile key) {
        if(key instanceof ScriptFolder) {
            return new ScriptFolderNode((ScriptFolder)key);
        } else if(key instanceof ScriptFile) {
            return createFileNode((ScriptFile) key);
        } else {
            return super.createNodeForKey(key);
        }
    }
    
    private Node createFileNode(ScriptFile file) {
        FileObject fo = FileUtil.toFileObject(new File(file.getPath()));
        try {
            DataObject dObj = DataObject.find(fo);
            return dObj.getNodeDelegate();
        } catch (DataObjectNotFoundException ex) {
            logger.log(Level.SEVERE, "Unable to load data object from: "+file.getPath(), ex);
            BubbleUtil.showException(Bundle.MSG_ScriptFolderChildren_LoadError(), ex);
            return new ScriptFileNode(file);
        }
    }
    
    private class ScriptEventListener implements EventBusListener<ScriptEvent> {
        
        private boolean refreshed = false;
        
        @Override
        public void published(Collection<ScriptEvent> events) {
            refreshed = false;
            for(ScriptEvent event : events) {
                if(shouldRefresh(event)) {
                    refresh(false);
                    refreshed = true;
                }
            }
        }
        
        private boolean shouldRefresh(ScriptEvent event) {
            if(refreshed) return false;
            if(event.getParent() != root) return false;
            switch(event.getType()) {
                case ADDED: return true;
                case DELETED: return true;
                default: return false;
            }
        }
    }
}
