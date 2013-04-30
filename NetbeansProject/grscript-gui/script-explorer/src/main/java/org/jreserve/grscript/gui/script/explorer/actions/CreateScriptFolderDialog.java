package org.jreserve.grscript.gui.script.explorer.actions;

import org.jreserve.grscript.gui.script.registry.ScriptFolder;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.CreateScriptDialog.Title=Create folder",
    "# {0} - name",
    "MSG.CreateScriptDialog.Name.Exists=Name \"{0}\" already used!"
})
class CreateScriptFolderDialog extends ScriptFolderPanel {
    
    CreateScriptFolderDialog(ScriptFolder parent) {
        super(parent);
    }
    
    @Override
    protected String getDialogTitle() {
        return Bundle.LBL_ScriptFolderPanel_Name();
    }

    @Override
    protected boolean isInputValid(String name) {
        return super.isInputValid(name) && isNewName(name);
    }
    
    private boolean isNewName(String name) {
        for(ScriptFolder child : folder.getFolders()) {
            if(child.getName().equalsIgnoreCase(name)) {
                setErrorMsg(Bundle.MSG_CreateScriptDialog_Name_Exists(child.getName()));
                return false;
            }
        }
        return true;
    }

    @Override
    protected void dialogOk() {
        folder.addFolder(new ScriptFolder(getNameText()));
    }
}
