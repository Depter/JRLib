package org.jreserve.grscript.gui.script.registry;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.grscript.gui.eventbus.EventBusRegistry;

/**
 *
 * @author Peti
 */
public class ScriptEvent {
    
    static void folderAdded(ScriptFolder parent, ScriptFolder folder) {
        publishEvent(new ScriptFolderEvent(Type.ADD, parent, folder));
    }
    
    private static void publishEvent(ScriptEvent event) {
        EventBusRegistry.getDefault().publishEvent(event);
    }
    
    static void fileAdded(ScriptFolder parent, ScriptFile file) {
        publishEvent(new ScriptFileEvent(Type.ADD, parent, file));
    }
    
    static void folderDeleted(ScriptFolder parent, ScriptFolder folder) {
        publishEvent(new ScriptFolderEvent(Type.DELETE, parent, folder));
    }
    
    static void fileDeleted(ScriptFolder parent, ScriptFile file) {
        publishEvent(new ScriptFileEvent(Type.DELETE, parent, file));
    }
    
    static void folderRenamed(ScriptFolder parent, ScriptFolder folder) {
        publishEvent(new ScriptFolderEvent(Type.RENAME, parent, folder));
    }
    
    static void fileRenamed(ScriptFolder parent, ScriptFile file) {
        publishEvent(new ScriptFileEvent(Type.RENAME, parent, file));
    }
    
    static void filesAdded(ScriptFolder parent, List<ScriptFile> files) {
        List<ScriptEvent> events = new ArrayList<ScriptEvent>(files.size());
        for(ScriptFile file : files)
            events.add(new ScriptFileEvent(Type.ADD, parent, file));
        EventBusRegistry.getDefault().publishEvents(events);
    }
    
    private ScriptFolder parent;
    private Type type;
    
    private ScriptEvent(Type type, ScriptFolder parent) {
        this.type = type;
        this.parent = parent;
    }
    
    public Type getType() {
        return type;
    }
    
    public ScriptFolder getParentFolder() {
        return parent;
    }
    
    public static class ScriptFolderEvent extends ScriptEvent {
    
        private ScriptFolder folder;
        
        private ScriptFolderEvent(Type type, ScriptFolder parent, ScriptFolder folder) {
            super(type, parent);
            this.folder = folder;
        }
        
        public ScriptFolder getFolder() {
            return folder;
        }
    }
    
    public static class ScriptFileEvent extends ScriptEvent {
    
        private ScriptFile file;
        
        private ScriptFileEvent(Type type, ScriptFolder parent, ScriptFile file) {
            super(type, parent);
            this.file = file;
        }
        
        public ScriptFile getFile() {
            return file;
        }
    }
    
    public static enum Type {
        ADD,
        DELETE,
        RENAME;
    }
}
