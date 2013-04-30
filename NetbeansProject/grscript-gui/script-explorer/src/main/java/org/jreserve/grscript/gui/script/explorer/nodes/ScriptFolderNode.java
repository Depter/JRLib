package org.jreserve.grscript.gui.script.explorer.nodes;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.jreserve.grscript.gui.notificationutil.DialogUtil;
import org.jreserve.grscript.gui.script.registry.ScriptFolder;
import org.openide.actions.DeleteAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.ScriptFolderNode.Name.Empty=Empty name not allowed!",
    "# {0} - name",
    "MSG.ScriptFolderNode.Name.Exists=Name \"{0}\" already exists!",
    "MSG.ScriptFolderNode.Name.Same=Name not changed!"
})
public class ScriptFolderNode extends AbstractNode {

    public final static String ACTION_PATH = "Scripts/Actions/Nodes/ScriptFolderNode";
    private final static String IMG_PATH = "org/jreserve/grscript/gui/script/explorer/nodes/";
    private final static Image OPENED_IMG = ImageUtilities.loadImage(IMG_PATH+"folderOpen.gif");
    private final static Image CLOSED_IMG = ImageUtilities.loadImage(IMG_PATH+"folder.gif");
    
    private ScriptFolder folder;
    
    public ScriptFolderNode(ScriptFolder folder) {
        super(Children.create(new ScriptFolderChildren(folder), true), Lookups.singleton(folder));
        this.folder = folder;
        super.setDisplayName(folder.getName());
        super.setName(folder.getName());
    }

    @Override
    public Image getIcon(int type) {
        return CLOSED_IMG;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return OPENED_IMG;
    }

    @Override
    public boolean canDestroy() {
        return folder.getParent() != null;
    }

    @Override
    public void destroy() throws IOException {
        ScriptFolder parent = folder.getParent();
        if(parent != null)
            parent.removeFolder(folder);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = new ArrayList<Action>(Utilities.actionsForPath(ACTION_PATH));
        Action delete = SystemAction.get(DeleteAction.class);
        if(!actions.contains(delete))
            actions.add(delete);
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public boolean canRename() {
        return true;
    }

    @Override
    public void setName(String s) {
        if(nameIsValid(s)) {
            super.setDisplayName(s);
            super.setName(s);
            folder.setName(s);
        }
    }
    
    private boolean nameIsValid(String name) {
        return nameNotNull(name) && 
               nameNotExists(name) &&
               nameIsNew(name);
    }
    
    private boolean nameNotNull(String name) {
        if(name==null || name.trim().length()==0) {
            DialogUtil.showError(Bundle.MSG_ScriptFolderNode_Name_Empty());
            return false;
        }
        return true;
    }
    
    private boolean nameNotExists(String name) {
        for(ScriptFolder child : folder.getParent().getFolders()) {
            if(child != folder && child.getName().equalsIgnoreCase(name)) {
                DialogUtil.showError(Bundle.MSG_ScriptFolderNode_Name_Exists(child.getName()));
                return false;
            }
        }
        return true;
    }
    
    private boolean nameIsNew(String name) {
        if(folder.getName().equals(name)) {
            return false;
        }
        return true;
    }
}
