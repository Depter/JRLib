package org.jreserve.grscript.gui.script.explorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.grscript.gui.script.explorer.data.ScriptFolder;
import org.jreserve.grscript.gui.script.explorer.nodes.ScriptFolderNode;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Script",
    id = "org.jreserve.grscript.gui.script.explorer.actions.RenameScriptFolderAction")
@ActionRegistration(
    displayName = "#CTL_RenameScriptFolderAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Script", position = 200),
    @ActionReference(path = ScriptFolderNode.ACTION_PATH, position = 200)
})
@Messages("CTL_RenameScriptFolderAction=Rename folder")
public final class RenameScriptFolderAction implements ActionListener {

    private final ScriptFolder folder;

    public RenameScriptFolderAction(ScriptFolder context) {
        this.folder = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        RenameScriptFolderForm form = new RenameScriptFolderForm(folder);
        form.showDialog();
    }
}
