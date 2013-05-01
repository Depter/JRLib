package org.jreserve.grscript.gui.script.registry;

import java.io.File;
import java.util.Collection;
import org.jreserve.grscript.gui.eventbus.EventBusListener;
import org.jreserve.grscript.gui.eventbus.EventBusRegistry;
import org.netbeans.api.actions.Openable;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ScriptRegistry {
    
    private static ScriptFolder root;
    private static ScriptEventListener listener;
    
    public static synchronized ScriptFolder getRoot() {
        if(root == null) {
            root = ScriptRegistryUtil.loadRoot();
            listener = new ScriptEventListener();
            EventBusRegistry.getDefault().subscribe(ScriptEvent.class, listener);
        }
        return root;
    }
    
    public static synchronized ScriptFile getScriptFile(File file) {
        return getScriptFile(getRoot(), file);
    }
    
    private static ScriptFile getScriptFile(ScriptFolder folder, File file) {
        for(ScriptFile sf : folder.getFiles())
            if(file.equals(new File(sf.getPath())))
                return sf;
        
        for(ScriptFolder sf : folder.getFolders()) {
            ScriptFile sFile = getScriptFile(sf, file);
            if(sFile != null) return sFile;
        }
        return null;
    }
    
    private ScriptRegistry() {}
    
    private static class ScriptEventListener implements EventBusListener<ScriptEvent> {

        @Override
        public void published(Collection<ScriptEvent> events) {
            if(root != null)
                ScriptRegistryUtil.saveRegistry(root);
            openNewScripts(events);
        }

        private void openNewScripts(Collection<ScriptEvent> events) {
            for(ScriptEvent evt : events) {
                if(ScriptEvent.Type.ADD == evt.getType() &&
                   (evt instanceof ScriptEvent.ScriptFileEvent))
                    openScript((ScriptEvent.ScriptFileEvent) evt);
            }
        }

        private void openScript(ScriptEvent.ScriptFileEvent event) {
            DataObject obj = getDataObject(event);
            if(obj != null) {
                Openable openable = obj.getLookup().lookup(Openable.class);
                if(openable != null)
                    openable.open();
            }
        }
        
        private DataObject getDataObject(ScriptEvent.ScriptFileEvent event) {
            try {
                File file = new File(event.getFile().getPath());
                FileObject fo = FileUtil.toFileObject(file);
                return DataObject.find(fo);
            } catch (DataObjectNotFoundException ex) {
                return null;
            }
        }
    }
}
