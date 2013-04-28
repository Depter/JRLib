package org.jreserve.grscript.gui.script.explorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.jreserve.grscript.gui.notificationutil.BubbleUtil;
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
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Script",
    id = "org.jreserve.grscript.gui.script.explorer.actions.CreateScriptAction"
)
@ActionRegistration(
    displayName = "#CTL_CreateScriptAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Script", position = 800),
    @ActionReference(path = ScriptFolderNode.ACTION_PATH, position = 800)
})
@Messages({
    "CTL_CreateScriptAction=Create script",
    "CTL.CreateScriptAction.Select=Create",
    "CTL.CreateScriptAction.Title=Create script",
    "# {0} - file",
    "MSG.CreateScriptAction.Exists=File \"{0}\" already exists!",
    "# {0} - file",
    "MSG.CreateScriptAction.Added=File \"{0}\" already added!",
    "MSG.CreateScriptAction.CreationError=Script creation failed..."
})
public final class CreateScriptAction implements ActionListener {
    
    private final static Logger logger = Logger.getLogger(CreateScriptAction.class.getName());
    private final static String TEMPLATE = "org/jreserve/grscript/gui/script/GRscriptTemplate.grs";
    
    private final ScriptFolder folder;

    public CreateScriptAction(ScriptFolder context) {
        this.folder = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        File file = selectFile();
        if(file == null || !checkFile(file))
            return;
        new FileCreator(file).execute();
    }
    
    private File selectFile() {
        FileChooserBuilder fcb = new FileChooserBuilder(GRscriptDataObject.class);
        fcb.setTitle(Bundle.CTL_CreateScriptAction_Title());
        fcb.setApproveText(Bundle.CTL_CreateScriptAction_Select());
        fcb.addFileFilter(GRScriptFilter.getInstance());
        fcb.setFileFilter(GRScriptFilter.getInstance());
        return fcb.showSaveDialog();
    }
    
    private boolean checkFile(File file) {
        if(file.exists()) {
            DialogUtil.showWarning(Bundle.MSG_CreateScriptAction_Exists(file.getAbsolutePath()));
            return false;
        }
        
        String path = file.getAbsolutePath();
        for(ScriptFile sf : folder.getFiles()) {
            if(sf.getPath().equalsIgnoreCase(path)) {
                DialogUtil.showWarning(Bundle.MSG_CreateScriptAction_Added(file));
                return false;
            }
        }
        return true;
    }
    
    private class FileCreator extends SwingWorker<ScriptFile, Void> {
        
        private BufferedReader reader;
        private PrintWriter writer;
        private final File file;
        
        private FileCreator(File file) {
            this.file = file;
        }
        
        @Override
        protected ScriptFile doInBackground() throws Exception {
            try {
                openStreams();
                copyTemplate();
                return new ScriptFile(file);
            } finally {
                closeStreams();
            }
        }
        
        private void openStreams() throws IOException {
            ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
            InputStream template = cl.getResourceAsStream(TEMPLATE);
            reader = new BufferedReader(new InputStreamReader(template));
            writer = new PrintWriter(file);
        }
        
        private void copyTemplate() throws IOException {
            String line;
            while((line = reader.readLine()) != null)
                writer.println(line);
        }
        
        private void closeStreams() {
            if(reader != null) {
                try {reader.close();} catch(IOException ex) {
                    logger.warning("Unable to close tempalte reader: "+TEMPLATE);
                }
            }
            
            if(writer != null) {
                writer.close();
            }
        }

        @Override
        protected void done() {
            try {
                folder.addFile(get());
            } catch (InterruptedException ex) {
                logger.log(Level.FINE, "Creating file interrupted! {0}", file.getAbsolutePath());
            } catch (ExecutionException ex) {
                logger.log(Level.SEVERE, "Creating file failed! " + file.getAbsolutePath(), ex);
                BubbleUtil.showException(Bundle.MSG_CreateScriptAction_CreationError(), ex);
            }
        }
    }
}