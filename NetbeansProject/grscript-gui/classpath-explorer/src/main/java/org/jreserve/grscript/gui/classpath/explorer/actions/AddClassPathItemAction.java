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
package org.jreserve.grscript.gui.classpath.explorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.grscript.gui.classpath.ClassPathUtil;
import org.jreserve.grscript.gui.classpath.explorer.nodes.CpCategoryRootNode;
import org.jreserve.grscript.gui.classpath.explorer.nodes.CpRootNode;
import org.jreserve.grscript.gui.classpath.registry.ClassPathItem;
import org.jreserve.grscript.gui.notificationutil.DialogUtil;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Edit",
    id = "org.jreserve.grscript.gui.classpath.explorer.actions.AddClassPathItemAction"
)
@ActionRegistration(
    displayName = "#CTL_AddClassPathItemAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1300),
    @ActionReference(path = CpRootNode.ACTION_PATH, position = 100),
    @ActionReference(path = CpCategoryRootNode.ACTION_PATH, position = 100)
})
@Messages({
    "CTL_AddClassPathItemAction=Add Classpath item",
    "CTL.AddClassPathItemAction.Dialog.Title=Select items",
    "CTL.AddClassPathItemAction.Dialog.Approve=Select",
    "# {0} - files",
    "CTL.AddClassPathItemAction.Error.Dialog.Msg=The following files are not valid java binaries:{0}"
})
public final class AddClassPathItemAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        File[] files = getFiles();
        if(files == null || files.length == 0)
            return;
        
        List<File> invalidFiles = getInvalidFiles(files);
        if(invalidFiles.isEmpty()) {
            registerFiles(files);
        } else {
            showErrorDialog(invalidFiles);
        }
    }
    
    private File[] getFiles() {
        FileChooserBuilder fcb = new FileChooserBuilder(AddClassPathItemAction.class);
        fcb.setTitle(Bundle.CTL_AddClassPathItemAction_Dialog_Title());
        fcb.setApproveText(Bundle.CTL_AddClassPathItemAction_Dialog_Approve());
        fcb.addFileFilter(JavaSourceFilter.getInstance());
        fcb.setFileFilter(JavaSourceFilter.getInstance());
        return fcb.showMultiOpenDialog();
    }
    
    private List<File> getInvalidFiles(File[] files) {
        List<File> result = new ArrayList<File>();
        for(File file : files)
            if(!ClassPathUtil.isJavaBinary(file))
                result.add(file);;
        return result;
    }
    
    private void registerFiles(File[] files) {
        List<ClassPathItem> items = new ArrayList<ClassPathItem>(files.length);
        for(File file : files)
            items.add(ClassPathUtil.createItem(file));
        ClassPathUtil.addRegistryItems(items);
    }
    
    private void showErrorDialog(List<File> invalidFiles) {
        String list = getFileList(invalidFiles);
        String msg = Bundle.CTL_AddClassPathItemAction_Error_Dialog_Msg(list);
        DialogUtil.showWarning(msg);
    }
    
    private String getFileList(List<File> files) {
        StringBuilder sb = new StringBuilder();
        for(File f : files)
            sb.append("\n\t").append(f.getAbsolutePath());
        return sb.toString();
    }
}
