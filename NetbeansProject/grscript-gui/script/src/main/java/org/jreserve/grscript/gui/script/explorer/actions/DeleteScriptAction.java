package org.jreserve.grscript.gui.script.explorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.jreserve.grscript.gui.script.explorer.data.AbstractScriptFile;
import org.jreserve.grscript.gui.script.explorer.data.ScriptFile;
import org.jreserve.grscript.gui.script.explorer.data.ScriptFolder;
import org.jreserve.grscript.gui.script.explorer.nodes.ScriptFileNode;
import org.jreserve.grscript.gui.script.explorer.nodes.ScriptFolderNode;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Script",
    id = "org.jreserve.grscript.gui.script.explorer.actions.DeleteScriptAction")
@ActionRegistration(
    displayName = "#CTL_DeleteScriptAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Script", position = 1500, separatorBefore = 1400),
    @ActionReference(path = ScriptFolderNode.ACTION_PATH, position = 1500, separatorBefore = 1400),
    @ActionReference(path = ScriptFileNode.ACTION_PATH, position = 1500, separatorBefore = 1400)
})
@Messages("CTL_DeleteScriptAction=Delete")
public final class DeleteScriptAction implements ActionListener {

    private final List<AbstractScriptFile> context;

    public DeleteScriptAction(List<AbstractScriptFile> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        for (AbstractScriptFile file : context) {
            ScriptFolder parent = file.getParent();
            if(file instanceof ScriptFolder) {
                parent.removeFolder((ScriptFolder)file);
            } else {
                parent.removeFile((ScriptFile)file);
            }
        }
    }
}
