package org.jreserve.grscript.gui.script.explorer.data;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.jreserve.grscript.gui.notificationutil.BubbleUtil;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.ScriptRegistry.LoadError.Bubble=Unable to load scripts!",
    "MSG.ScriptRegistry.SaveError.Bubble=Unable to save scripts!"
})
public class ScriptRegistry {

    private final static Logger logger = Logger.getLogger(ScriptRegistry.class.getName());
    
    private static ScriptFolder root;
    
    public synchronized static ScriptFolder getRoot() {
        if(root == null)
            loadRoot();
        return root;
    }
    
    private static void loadRoot() {
        try {
            File file = getRegistryFile();
            root = file.exists()? 
                    umarshallRegistry(file) : new ScriptFolder("root");
            root.setParentsAfterUnmarshal();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to load script registry!", ex);
            BubbleUtil.showException(Bundle.MSG_ScriptRegistry_LoadError_Bubble(), ex);
            root = new ScriptFolder("root");
        }
    }
        
    private static File getRegistryFile() throws IOException {
        FileObject configRoot = FileUtil.getConfigRoot();
        FileObject fs = FileUtil.createFolder(configRoot, "Scripts");
        return new File(FileUtil.toFile(fs), "registry.xml");
    }
    
    
    private static ScriptFolder umarshallRegistry(File file) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(ScriptFolder.class);
        Unmarshaller m = ctx.createUnmarshaller();
        return (ScriptFolder) m.unmarshal(file);
    }
    
    
    static void saveRegistry() {
        try {
            File file = getRegistryFile();
            JAXBContext ctx = JAXBContext.newInstance(ScriptFolder.class);
            Marshaller m = ctx.createMarshaller();
            m.marshal(root, file);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to save classpath registry!", ex);
            BubbleUtil.showException(Bundle.MSG_ScriptRegistry_SaveError_Bubble(), ex);
        }
    }
    
    private ScriptRegistry() {}
}
