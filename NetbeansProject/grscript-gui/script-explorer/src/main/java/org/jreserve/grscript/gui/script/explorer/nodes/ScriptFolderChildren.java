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
import java.util.Collection;
import java.util.List;
import org.jreserve.grscript.gui.eventbus.EventBusListener;
import org.jreserve.grscript.gui.eventbus.EventBusRegistry;
import org.jreserve.grscript.gui.script.registry.ScriptEvent;
import org.jreserve.grscript.gui.script.registry.ScriptFile;
import org.jreserve.grscript.gui.script.registry.ScriptFolder;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ScriptFolderChildren extends ChildFactory<Object> {
    
    private ScriptFolder folder;
    private ScriptEventListener listener;
    
    public ScriptFolderChildren(ScriptFolder folder) {
        this.folder = folder;
        listener = new ScriptEventListener();
        EventBusRegistry.getDefault().subscribe(ScriptEvent.class, listener);
    }
    
    @Override
    protected boolean createKeys(List<Object> list) {
        list.addAll(folder.getFolders());
        list.addAll(folder.getFiles());
        return true;
    }

    @Override
    protected Node createNodeForKey(Object key) {
        if(key instanceof ScriptFolder) {
            return new ScriptFolderNode((ScriptFolder)key);
        } else if(key instanceof ScriptFile) {
            return createFileNode((ScriptFile) key);
        } else {
            return super.createNodeForKey(key);
        }
    }

    private Node createFileNode(ScriptFile file) {
        try {
            FileObject fo = FileUtil.toFileObject(new File(file.getPath()));
            return DataObject.find(fo).getNodeDelegate();
        } catch (Exception ex) {
            return new ScriptFileNode(file);
        }
    }
    
    private class ScriptEventListener implements EventBusListener<ScriptEvent> {

        @Override
        public void published(Collection<ScriptEvent> events) {
            if(shouldUpdate(events))
                refresh(true);
            else if(shouldUnsubscribe(events))
                EventBusRegistry.getDefault().unsubscribe(this);
        }
    
        private boolean shouldUpdate(Collection<ScriptEvent> events) {
            for(ScriptEvent evt : events)
                if(isUpdateType(evt) && folder == evt.getParentFolder())
                    return true;
            return false;
        }
        
        private boolean isUpdateType(ScriptEvent event) {
            switch(event.getType()) {
                case ADD: return true;
                case DELETE: return true;
                default: return false;
            }
        }
    
        private boolean shouldUnsubscribe(Collection<ScriptEvent> events) {
            for(ScriptEvent evt : events)
                if(isDeleteEvent(evt))
                    return true;
            return false;
        }
        
        private boolean isDeleteEvent(ScriptEvent event) {
            switch(event.getType()) {
                case DELETE: break;
                default: return false;
            }
            
            if(!(event instanceof ScriptEvent.ScriptFolderEvent))
                return false;
            
            return folder == ((ScriptEvent.ScriptFolderEvent)event).getFolder();
        }
    }
}
