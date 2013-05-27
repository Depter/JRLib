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
package org.jreserve.grscript.gui.script.examples.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingWorker;
import org.jreserve.grscript.gui.notificationutil.BubbleUtil;
import org.jreserve.grscript.gui.notificationutil.DialogUtil;
import org.jreserve.grscript.gui.script.GRScriptDataObject;
import org.jreserve.grscript.gui.script.GRScriptFileFilter;
import org.jreserve.grscript.gui.script.registry.ScriptFile;
import org.jreserve.grscript.gui.script.registry.ScriptFolder;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ExampleAction.Dialog.Approve=Save",
    "LBL.ExampleAction.Dialog.Title=Create script",
    "MSG.ExampleAction.File.Exists=Script already added!",
    "MSG.ExampleAction.File.Create.Error=Unable to create file..."
})
class ExampleAction extends AbstractAction {
    
    private final static Logger logger = Logger.getLogger(ExampleAction.class.getName());
    
    private final ScriptFolder folder;
    private final FileObject file;
    
    ExampleAction(ScriptFolder folder, FileObject file) {
        this.folder = folder;
        this.file = file;
        super.putValue(Action.NAME, file.getName());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        File target = selectFile();
        if (target == null) {
            return;
        }

        String path = escapeName(target);
        if (isNewName(path))
            new FileCreator(new File(path)).execute();
    }

    private File selectFile() {
        FileChooserBuilder fcb = new FileChooserBuilder(GRScriptDataObject.class);
        fcb.setTitle(Bundle.LBL_ExampleAction_Dialog_Title());
        fcb.setApproveText(Bundle.LBL_ExampleAction_Dialog_Approve());
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
        for (ScriptFile sf : folder.getFiles()) {
            if (sf.getPath().equalsIgnoreCase(path)) {
                DialogUtil.showWarning(Bundle.MSG_ExampleAction_File_Exists());
                return false;
            }
        }
        return true;
    }

    private class FileCreator extends SwingWorker<Void, Object> {

        private final File target;
        private InputStream in;
        private OutputStream out;
        
        private FileCreator(File target) {
            this.target = target;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                openStrems();
                copyFile();
                return null;
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Unable to create script: " + file, ex);
                throw ex;
            } finally {
                closeStreams();
            }
        }
        
        private void openStrems() throws Exception {
            in = ExampleAction.this.file.getInputStream();
            out = new FileOutputStream(target);
        }
        
        private void copyFile() throws Exception {
            byte[] buffer = new byte[1024];
            int count;
            while((count = in.read(buffer)) != -1)
                out.write(buffer, 0, count);
            out.flush();
        }
        
        private void closeStreams() {
            if(out != null) {
                try{out.close();} catch (Exception ex) {
                    logger.log(Level.WARNING, "Unable to close OutputStream to: {0}", target);
                }
            }
            if(in != null) {
                try{in.close();} catch (Exception ex) {
                    logger.log(Level.WARNING, "Unable to close OutputStream to: {0}", file.getPath());
                }
            }
        }

        @Override
        protected void done() {
            try {
                Void result = get();
                folder.addFile(new ScriptFile(target));
            } catch (Exception ex) {
                BubbleUtil.showException(Bundle.MSG_ExampleAction_File_Create_Error(), ex);
            }
        }
    }
    
}
