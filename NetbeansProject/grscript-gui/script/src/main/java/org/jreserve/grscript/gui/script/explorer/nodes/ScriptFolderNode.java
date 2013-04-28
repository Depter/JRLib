package org.jreserve.grscript.gui.script.explorer.nodes;

import java.awt.Image;
import java.util.Collection;
import javax.swing.Action;
import org.jreserve.grscript.gui.eventbus.EventBusListener;
import org.jreserve.grscript.gui.eventbus.EventBusRegistry;
import org.jreserve.grscript.gui.script.explorer.data.ScriptEvent;
import org.jreserve.grscript.gui.script.explorer.data.ScriptFolder;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ScriptFolderNode extends AbstractNode {
    
    private final static String IMG_HOME = "org/jreserve/grscript/gui/script/";
    private final static Image OPENED_IMG = ImageUtilities.loadImage(IMG_HOME+"folder_opened.png");
    private final static Image CLOSED_IMG = ImageUtilities.loadImage(IMG_HOME+"folder_closed.png");

    public final static String ACTION_PATH = "Scripts/Actions/Node/Folder";
    
    private ScriptFolder folder;
    private NameListener listener;
    
    public ScriptFolderNode() {
        this(null);
    }
    
    public ScriptFolderNode(ScriptFolder folder) {
        super(
                Children.create(new ScriptFolderChildren(folder), true), 
                Lookups.singleton(folder==null? new ScriptFolder() : folder)
                );
        setDisplayName(folder==null? "root" : folder.getName());
        
        this.folder = folder;
        listener = new NameListener();
        EventBusRegistry.getDefault().subscribe(ScriptEvent.class, listener);
    }

    @Override
    public Action[] getActions(boolean context) {
        Collection<? extends Action> actions = Utilities.actionsForPath(ACTION_PATH);
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Image getIcon(int type) {
        return CLOSED_IMG;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return OPENED_IMG;
    }
    
    private class NameListener implements EventBusListener<ScriptEvent> {

        @Override
        public void published(Collection<ScriptEvent> events) {
            for(ScriptEvent evt : events) {
                if(shouldUpdate(evt))
                    setDisplayName(folder.getName());
                
                if(shouldUnsubscribe(evt))
                    EventBusRegistry.getDefault().unsubscribe(this);
            }
        }
        
        private boolean shouldUpdate(ScriptEvent evt) {
            if(ScriptEvent.Type.RENAMED != evt.getType()) 
                return false;
            if(!(evt instanceof ScriptEvent.ScriptFolderEvent)) 
                return false;
            return ((ScriptEvent.ScriptFolderEvent) evt).getFolder() == folder;
        }
        
        private boolean shouldUnsubscribe(ScriptEvent evt) {
            return ScriptEvent.Type.DELETED == evt.getType() &&
                   (evt instanceof ScriptEvent.ScriptFolderEvent) &&
                   ((ScriptEvent.ScriptFolderEvent)evt).getFolder() == folder;
        }
    }
}
