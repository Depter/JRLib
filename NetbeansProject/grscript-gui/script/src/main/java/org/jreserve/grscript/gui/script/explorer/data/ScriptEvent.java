package org.jreserve.grscript.gui.script.explorer.data;

import org.jreserve.grscript.gui.eventbus.EventBusRegistry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ScriptEvent {
    
    public static void folderRenamed(ScriptFolder folder) {
        publish(new ScriptFolderEvent(folder, folder.parent, Type.RENAMED));
    }
    
    public static void folderAdded(ScriptFolder folder) {
        publish(new ScriptFolderEvent(folder, folder.parent, Type.ADDED));
    }
    
    private static void publish(ScriptEvent event) {
        EventBusRegistry.getDefault().publish(event);
    }
    
    public static void folderRemoved(ScriptFolder folder, ScriptFolder parent) {
        publish(new ScriptFolderEvent(folder, parent, Type.DELETED));
    }
    
    public static void fileAdded(ScriptFile file) {
        publish(new ScriptFileEvent(file, file.parent, Type.ADDED));
    }
    
    public static void fileRemoved(ScriptFile file, ScriptFolder parent) {
        publish(new ScriptFileEvent(file, parent, Type.DELETED));
    }
    
    protected ScriptFolder parent;
    protected Type type;
    
    protected ScriptEvent(ScriptFolder parent, Type type) {
        this.type = type;
        this.parent = parent;
    }
    
    public Type getType() {
        return type;
    }
    
    public ScriptFolder getParent() {
        return parent;
    }
    
    public static class ScriptFolderEvent extends ScriptEvent {
        private ScriptFolder folder;
        
        public ScriptFolderEvent(ScriptFolder folder, ScriptFolder parent, Type type) {
            super(parent, type);
            this.folder = folder;
        }
        
        public ScriptFolder getFolder() {
            return folder;
        }
        
    }
    
    public static class ScriptFileEvent extends ScriptEvent {
        private ScriptFile file;
        
        public ScriptFileEvent(ScriptFile file, ScriptFolder parent, Type type) {
            super(parent, type);
            this.file = file;
        }
        
        public ScriptFile getFile() {
            return file;
        }
    }
    
    public static enum Type {
        ADDED,
        DELETED,
        RENAMED;
    }
}
