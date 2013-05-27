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

import org.jreserve.grscript.gui.script.GRScriptFileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.jreserve.grscript.gui.notificationutil.BubbleUtil;
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
    id = "org.jreserve.grscript.gui.script.explorer.actions.CreateScriptAction"
)
@ActionRegistration(
    displayName = "#CTL_CreateScriptAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1375),
    @ActionReference(path = ScriptFolderNode.ACTION_PATH, position = 300, separatorAfter = 350)
})
@Messages({
    "CTL_CreateScriptAction=Create GRScript",
    "LBL.CreateScriptAction.Dialog.Approve=Save",
    "LBL.CreateScriptAction.Dialog.Title=Create script",
    "MSG.CreateScriptAction.File.Exists=Script already added!",
    "MSG.CreateScriptAction.File.Create.Error=Unable to create file..."
})
public final class CreateScriptAction implements ActionListener {

    private final static Logger logger = Logger.getLogger(CreateScriptAction.class.getName());
    private final ScriptFolder parent;

    public CreateScriptAction(ScriptFolder context) {
        this.parent = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        File file = selectFile();
        if (file == null) {
            return;
        }

        String path = escapeName(file);
        if (isNewName(path))
            new FileCreator(new File(path)).execute();
    }

    private File selectFile() {
        FileChooserBuilder fcb = new FileChooserBuilder(GRScriptDataObject.class);
        fcb.setTitle(Bundle.LBL_CreateScriptAction_Dialog_Title());
        fcb.setApproveText(Bundle.LBL_CreateScriptAction_Dialog_Approve());
        fcb.addFileFilter(GRScriptFileFilter.getDefault());
        fcb.setFileFilter(GRScriptFileFilter.getDefault());
        return fcb.showSaveDialog();
    }

    private String escapeName(File file) {
        String path = file.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".grs")) {
            path += ".grs";
        }
        return path;
    }

    private boolean isNewName(String path) {
        for (ScriptFile sf : parent.getFiles()) {
            if (sf.getPath().equalsIgnoreCase(path)) {
                DialogUtil.showWarning(Bundle.MSG_CreateScriptAction_File_Exists());
                return false;
            }
        }
        return true;
    }

    private class FileCreator extends SwingWorker<Void, Object> {

        private final File file;

        private FileCreator(File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground() throws Exception {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(file);
                writer.println("double[][] data = [");
                writer.println("    [4426765, 992330 , 88952 , 13240 , 38622 , 26720, 36818, 10750],");
                writer.println("    [4388958, 984169 , 60162 , 35004 , 75768 , 23890, 572],");
                writer.println("    [5280130, 1239396, 76122 , 110189, 112895, 11751],");
                writer.println("    [5445384, 1164234, 171583, 16427 , 6451],");
                writer.println("    [5612138, 1837950, 155863, 127146],");
                writer.println("    [6593299, 1592418, 74189],");
                writer.println("    [6603091, 1659748],");
                writer.println("    [7194587]");
                writer.println("]");
                writer.println("cummulate(data)");
                writer.println("t = triangle(data)");
                writer.println("printData \"Claims:\", t");
                return null;
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Unable to create script: " + file, ex);
                throw ex;
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }

        @Override
        protected void done() {
            try {
                Void result = get();
                parent.addFile(new ScriptFile(file));
            } catch (Exception ex) {
                BubbleUtil.showException(Bundle.MSG_CreateScriptAction_File_Create_Error(), ex);
            }
        }
        
        
    }
}
