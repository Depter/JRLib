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
package org.jreserve.grscript.gui.script.registry;

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
    "MSG.ScriptRegistryUtil.LoadError.Bubble=Unable to load scripts!",
    "MSG.ScriptRegistryUtil.SaveError.Bubble=Unable to save scripts!"
})
class ScriptRegistryUtil {

    private final static Logger logger = Logger.getLogger(ScriptRegistryUtil.class.getName());
    
    public static ScriptFolder loadRoot() {
        try {
            File file = getRegistryFile();
            return file.exists()? umarshallRegistry(file) : new ScriptFolder("root");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to load script registry!", ex);
            BubbleUtil.showException(Bundle.MSG_ScriptRegistryUtil_LoadError_Bubble(), ex);
            return new ScriptFolder("root");
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
        ScriptFolder folder = (ScriptFolder) m.unmarshal(file);
        folder.initParents();
        return folder;
    }
    
    
    public static void saveRegistry(ScriptFolder root) {
        try {
            File file = getRegistryFile();
            JAXBContext ctx = JAXBContext.newInstance(ScriptFolder.class);
            Marshaller m = ctx.createMarshaller();
            m.marshal(root, file);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to save classpath registry!", ex);
            BubbleUtil.showException(Bundle.MSG_ScriptRegistryUtil_SaveError_Bubble(), ex);
        }
    }
     
    private ScriptRegistryUtil() {}
}