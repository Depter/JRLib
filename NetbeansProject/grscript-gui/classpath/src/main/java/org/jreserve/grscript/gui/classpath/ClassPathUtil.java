package org.jreserve.grscript.gui.classpath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.ModuleInfo;
import org.openide.util.Lookup;
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

    private final static String PATH_SEPARATOR = "path.separator";
    private final static String PATH = "java.class.path";
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
    
    public synchronized static List<ClassPathItem> getRegistryItems() {
        return getRegistry().getItems();
    }
    
    public synchronized static List<ClassPathItem> getRegistryItems(ClassPathItemType type) {
        return getRegistry().getItems(type);
    }
    
    public synchronized static void addRegistryItems(Collection<ClassPathItem> items) {
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
    
    public synchronized static void addRegistryItem(ClassPathItem item) {
        if(getRegistry().addItem(item)) {
            fireAddItemEvent(item);
            saveRegistry();
        }
    }
    
    private static void fireAddItemEvent(ClassPathItem item) {
        ClassPathEvent.publishAdd(item);
    }
    
    public synchronized static void removeRegistryItems(Collection<ClassPathItem> items) {
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
    
    public synchronized static void removeRegistryItem(ClassPathItem item) {
        if(getRegistry().removeItem(item)) {
            fireRemoveItemEvent(item);
            saveRegistry();
        }
    }
    
    private static void fireRemoveItemEvent(ClassPathItem item) {
        ClassPathEvent.publishDelete(item);
    }
    
    public static List<ClassPathItem> getPlatformClassPath() {
        List<ClassPathItem> items = new ArrayList<ClassPathItem>();
        for(String path : getPlatformPathes()) {
            ClassPathItemType type = getCpItemType(path);
            if(type != null)
                items.add(new ClassPathItem(type, path));
        }
        return items;
    }
    
    private static String[] getPlatformPathes() {
        String sep = System.getProperty(PATH_SEPARATOR);
        String path = System.getProperty(PATH);
        return path.split(sep);
    }
    
    private static ClassPathItemType getCpItemType(String item) {
        item = item.toLowerCase();
        if(item.endsWith(".jar"))
            return ClassPathItemType.JAR;
        else if(item.endsWith(".class"))
            return ClassPathItemType.CLASS;
        else
            return null;
    }
    
    public static List<ClassPathItem> getPlatformClassPath(ClassPathItemType type) {
        List<ClassPathItem> items = new ArrayList<ClassPathItem>();
        for(String path : getPlatformPathes()) {
            ClassPathItemType t = getCpItemType(path);
            if(type == t)
                items.add(new ClassPathItem(type, path));
        }
        return items;
    }
    
    public static List<ClassPathItem> getModulesClassPath() {
        InstalledFileLocator ifl = InstalledFileLocator.getDefault();
        
        Collection<? extends ModuleInfo> infos = Lookup.getDefault().lookupAll(ModuleInfo.class);
        for(ModuleInfo info : infos) {
            String cnb = info.getCodeNameBase();
            System.out.println("\tCodeNameBase: "+cnb);
            System.out.println("\tCodeName    : "+info.getCodeName());
            String relPath = "*.jar";
            for(File f : ifl.locateAll(relPath, cnb, false))
                System.out.println("\tjar    : "+f.getAbsolutePath());
        }
        return Collections.EMPTY_LIST;
    }
    
    public static boolean isJavaBinary(File file) {
        return file != null && 
               file.exists() && 
               isJavaBinary(file.getAbsolutePath());
    }
    
    public static boolean isJavaBinary(String path) {
        return getCpItemType(path) != null;
    }
    
    public static ClassPathItem createItem(File file) {
        return createItem(file.getAbsolutePath());
    }
    
    public static ClassPathItem createItem(String path) {
        ClassPathItemType type = getCpItemType(path);
        if(type == null)
            throw new IllegalArgumentException("Unrecognizable source type: "+path);
        return new ClassPathItem(type, path);
    }
}
