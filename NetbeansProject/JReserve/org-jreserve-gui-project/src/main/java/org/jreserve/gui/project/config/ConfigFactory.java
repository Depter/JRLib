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
package org.jreserve.gui.project.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.jreserve.gui.project.JReserveProjectFactory;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 */
public class ConfigFactory {
    
    private final static Logger logger = Logger.getLogger(ConfigFactory.class.getName());
    private static JAXBContext CTX;
    private static Unmarshaller UM;
    
    public static ProjectConfiguration readConfig(Project project) throws JAXBException, FileNotFoundException {
        try {
            Unmarshaller um = getUnmarshaller();
            File file = getConfigFile(project);
            return (ProjectConfiguration) um.unmarshal(file);
        } catch (JAXBException ex) {
            logger.log(Level.SEVERE, "Config file can not be parsed!", ex);
            throw ex;
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "Config file does not exists!", ex);
            throw ex;
        }
    }
    
    private static File getConfigFile(Project project) throws FileNotFoundException {
        FileObject dir = project.getProjectDirectory();
        FileObject config = dir.getFileObject(JReserveProjectFactory.CONFIG_NAME, JReserveProjectFactory.CONFIG_EXTENSION);
        if(config == null)
            throw new FileNotFoundException("File not found: "+dir.getPath()+"/"+JReserveProjectFactory.CONFIG_NAME+"."+JReserveProjectFactory.CONFIG_EXTENSION);
        return FileUtil.toFile(config);
    }
    
    private static Unmarshaller getUnmarshaller() throws JAXBException {
        if(CTX == null)
            CTX = JAXBContext.newInstance(ProjectConfiguration.class);
        if(UM == null)
            UM = CTX.createUnmarshaller();
        return UM;
    }
}
