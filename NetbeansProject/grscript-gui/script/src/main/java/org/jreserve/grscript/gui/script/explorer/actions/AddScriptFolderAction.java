package org.jreserve.grscript.gui.script.explorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.grscript.gui.script.explorer.data.ScriptFolder;
import org.jreserve.grscript.gui.script.explorer.data.ScriptRegistry;
import org.jreserve.grscript.gui.script.explorer.nodes.ScriptFolderNode;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Script",
    id = "org.jreserve.grscript.gui.script.explorer.actions.AddScriptFolderAction")
@ActionRegistration(
    displayName = "#CTL_AddScriptFolderAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Script", position = 100),
    @ActionReference(path = ScriptFolderNode.ACTION_PATH, position = 100)
})
@Messages("CTL_AddScriptFolderAction=Add folder")
public final class AddScriptFolderAction implements ActionListener {

    private ScriptFolder folder;

    public AddScriptFolderAction(ScriptFolder context) {
        this.folder = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        initContext();
        new AddScriptFolderForm(folder).showDialog();
    }
    
    private void initContext() {
        if(folder.getName() == null)
            folder = ScriptRegistry.getRoot();
    }
}