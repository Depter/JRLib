package org.jreserve.grscript.gui.classpath;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.jreserve.grscript.gui.classpath.registry.ClassPathItem;
import org.jreserve.grscript.gui.classpath.registry.ClassPathItemType;
import org.jreserve.grscript.gui.classpath.registry.ClassPathRegistry;
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
    "MSG.Registry.LoadError.Bubble=Unable to load classpath elements!",
    "MSG.Registry.SaveError.Bubble=Unable to save classpath elements!"
})
public class ClassPathUtil {
    
    
    private final static Logger logger = Logger.getLogger(ClassPathUtil.class.getName());
    
    private static ClassPathRegistry registry;
    
    private synchronized static ClassPathRegistry getRegistry() {
        if(registry == null)
            loadRegistry();
        return registry;
    }
    
    private static void loadRegistry() {
        try {
            File file = getRegistryFile();
            registry = file.exists()? 
                    umarshallRegistry(file) : new ClassPathRegistry();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to load classpath registry!", ex);
            BubbleUtil.showException(Bundle.MSG_Registry_LoadError_Bubble(), ex);
            registry = new ClassPathRegistry();
        }
    }
        
    private static File getRegistryFile() throws IOException {
        FileObject root = FileUtil.getConfigRoot();
        FileObject fs = FileUtil.createFolder(root, "ClassPath");
        return new File(FileUtil.toFile(fs), "registry.xml");
    }
    
    private static ClassPathRegistry umarshallRegistry(File file) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(ClassPathRegistry.class);
        Unmarshaller m = ctx.createUnmarshaller();
        return (ClassPathRegistry) m.unmarshal(file);
    }
    
    private static void saveRegistry() {
        try {
            File file = getRegistryFile();
            JAXBContext ctx = JAXBContext.newInstance(ClassPathRegistry.class);
            Marshaller m = ctx.createMarshaller();
            m.marshal(registry, file);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to save classpath registry!", ex);
            BubbleUtil.showException(Bundle.MSG_Registry_SaveError_Bubble(), ex);
        }
    }
    
    public synchronized static List<ClassPathItem> getClassPathItems() {
        return getRegistry().getItems();
    }
    
    public synchronized static List<ClassPathItem> getClassPathItems(ClassPathItemType type) {
        return getRegistry().getItems(type);
    }
    
    public synchronized static void addClassPathItems(Collection<ClassPathItem> items) {
        ClassPathRegistry reg = getRegistry();
        boolean changed = false;
        for(ClassPathItem item : items) {
            if(reg.addItem(item)) {
                changed = true;
                fireAddItemEvent(item);
            }
        }
        
        if(changed)
            saveRegistry();
    }
    
    public synchronized static void addClassPathItem(ClassPathItem item) {
        if(getRegistry().addItem(item)) {
            fireAddItemEvent(item);
            saveRegistry();
        }
    }
    
    private static void fireAddItemEvent(ClassPathItem item) {
        //TODO implement method
    }
    
    public synchronized static void removeClassPathItems(Collection<ClassPathItem> items) {
        ClassPathRegistry reg = getRegistry();
        boolean changed = false;
        for(ClassPathItem item : items) {
            if(reg.removeItem(item)) {
                changed = true;
                fireRemoveItemEvent(item);
            }
        }
        
        if(changed)
            saveRegistry();
    }
    
    public synchronized static void removeClassPathItem(ClassPathItem item) {
        if(getRegistry().removeItem(item)) {
            fireRemoveItemEvent(item);
            saveRegistry();
        }
    }
    
    private static void fireRemoveItemEvent(ClassPathItem item) {
        //TODO implement method
    }
}
