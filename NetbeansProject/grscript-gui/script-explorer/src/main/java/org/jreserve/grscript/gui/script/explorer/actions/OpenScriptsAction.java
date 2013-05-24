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
package org.jreserve.grscript.gui.script.explorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.grscript.gui.notificationutil.DialogUtil;
import org.jreserve.grscript.gui.script.GRScriptDataObject;
import org.jreserve.grscript.gui.script.explorer.nodes.ScriptFolderNode;
import org.jreserve.grscript.gui.script.registry.ScriptFile;
import org.jreserve.grscript.gui.script.registry.ScriptFolder;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "org.jreserve.grscript.gui.script.explorer.actions.OpenScriptsAction")
@ActionRegistration(
        displayName = "#CTL_OpenScriptsAction")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1350),
    @ActionReference(path = ScriptFolderNode.ACTION_PATH, position = 200)
})
@Messages({
    "CTL_OpenScriptsAction=Open GRScript",
    "LBL.OpenScriptsAction.Dialog.Approve=Select",
    "LBL.OpenScriptsAction.Dialog.Title=Select scripts",
    "# {0} - files",
    "MSG.OpenScriptsAction.InvalidFiles=The following files are invalid!{0}"
})
public final class OpenScriptsAction implements ActionListener {

    private final ScriptFolder parent;

    public OpenScriptsAction(ScriptFolder context) {
        this.parent = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        File[] files = selectFiles();
        if(files==null || files.length == 0)
            return;
        
        List<File> invalids = getInvalidFiles(files);
        if(!invalids.isEmpty())
            showErrorDialog(invalids);
        
        addFiles(files);
    }
    
    private File[] selectFiles() {
        FileChooserBuilder fcb = new FileChooserBuilder(GRScriptDataObject.class);
        fcb.setTitle(Bundle.LBL_OpenScriptsAction_Dialog_Title());
        fcb.setApproveText(Bundle.LBL_OpenScriptsAction_Dialog_Approve());
        fcb.addFileFilter(GRScriptFileFilter.getDefault());
        fcb.setFileFilter(GRScriptFileFilter.getDefault());
        return fcb.showMultiOpenDialog();
    }
    
    private List<File> getInvalidFiles(File[] files) {
        List<File> result = new ArrayList<File>(files.length);
        for(File file : files)
            if(!isValidFile(file))
                result.add(file);
        return result;
    }
    
    private boolean isValidFile(File file) {
        return file.exists() &&
               file.getAbsolutePath().toLowerCase().endsWith(".grs");
    }
    
    private void showErrorDialog(List<File> files) {
        String fileNames = getFileList(files);
        String msg = Bundle.MSG_OpenScriptsAction_InvalidFiles(fileNames);
        DialogUtil.showWarning(msg);
    }
    
    private String getFileList(List<File> files) {
        StringBuilder sb = new StringBuilder();
        for(File file : files)
            sb.append("\n\t").append(file.getAbsolutePath());
        return sb.toString();
    }

    private void addFiles(File[] files) {
        List<ScriptFile> sfs = new ArrayList<ScriptFile>(files.length);
        for(File file : files)
            sfs.add(new ScriptFile(file));
        
        parent.addFiles(sfs);
    }
}
