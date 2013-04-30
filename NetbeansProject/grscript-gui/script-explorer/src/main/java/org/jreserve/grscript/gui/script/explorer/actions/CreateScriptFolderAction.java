package org.jreserve.grscript.gui.script.explorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.grscript.gui.script.explorer.nodes.ScriptFolderNode;
import org.jreserve.grscript.gui.script.registry.ScriptFolder;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "File",
    id = "org.jreserve.grscript.gui.script.explorer.actions.CreateScriptFolderAction")
@ActionRegistration(
    displayName = "#CTL_CreateScriptFolderAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1300),
    @ActionReference(path = ScriptFolderNode.ACTION_PATH, position = 100)
})
@Messages("CTL_CreateScriptFolderAction=Create folder")
public final class CreateScriptFolderAction implements ActionListener {

    private final ScriptFolder parent;

    public CreateScriptFolderAction(ScriptFolder context) {
        this.parent = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        CreateScriptFolderDialog dialog = new CreateScriptFolderDialog(parent);
        dialog.showDialog();
    }
}
