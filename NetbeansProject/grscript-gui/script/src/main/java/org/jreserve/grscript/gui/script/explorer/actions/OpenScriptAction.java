package org.jreserve.grscript.gui.script.explorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.jreserve.grscript.gui.notificationutil.DialogUtil;
import org.jreserve.grscript.gui.script.GRscriptDataObject;
import org.jreserve.grscript.gui.script.explorer.data.ScriptFile;
import org.jreserve.grscript.gui.script.explorer.data.ScriptFolder;
import org.jreserve.grscript.gui.script.explorer.nodes.ScriptFolderNode;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Script",
    id = "org.jreserve.grscript.gui.script.explorer.actions.OpenScriptAction"
)
@ActionRegistration(
        displayName = "#CTL_OpenScriptAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Script", position = 1100),
    @ActionReference(path = ScriptFolderNode.ACTION_PATH, position = 1100)
})
@Messages({
    "CTL_OpenScriptAction=Open script",
    "CTL.OpenScriptAction.Dialog.Title=Open script",
    "CTL.OpenScriptAction.Dialog.Approve=Open",
    "# {0} - file",
    "MSG.OpenScriptAction.WrongFile=File \"{0}\" is not a GRScript file!"
})
public final class OpenScriptAction implements ActionListener {

    private final ScriptFolder folder;

    public OpenScriptAction(ScriptFolder context) {
        this.folder = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        File[] files = selectFiles();
        if(checkFiles(files)) {
            for(File file : files)
                folder.addFile(new ScriptFile(file));
        }
    }
    
    private File[] selectFiles() {
        FileChooserBuilder fcb = new FileChooserBuilder(GRscriptDataObject.class);
        fcb.setTitle(Bundle.CTL_OpenScriptAction_Dialog_Title());
        fcb.setApproveText(Bundle.CTL_OpenScriptAction_Dialog_Approve());
        fcb.addFileFilter(GRScriptFilter.getInstance());
        fcb.setFileFilter(GRScriptFilter.getInstance());
        return fcb.showMultiOpenDialog();
    }
    
    private boolean checkFiles(File[] files) {
        for(File file : files) {
            if(!file.isFile() || file.getAbsolutePath().equalsIgnoreCase(".grs")) {
                DialogUtil.showWarning(Bundle.MSG_OpenScriptAction_WrongFile(file));
                return false;
            }
        }
        return true;
    }
}
