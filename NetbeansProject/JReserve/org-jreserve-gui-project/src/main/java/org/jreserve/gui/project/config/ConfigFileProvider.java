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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.jreserve.gui.project.JReserveProjectFactory;
import org.jreserve.gui.project.api.ProjectFileProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ConfigFileProvider implements ProjectFileProvider {
    
    private final static Logger logger = Logger.getLogger(ConfigFileProvider.class.getName());
    
    @Override
    public void createFiles(File projectFolder) throws IOException {
        ProjectConfiguration config = new ProjectConfiguration();
        config.setName(projectFolder.getName());
        try {
            ConfigFactory.writeConfig(config, new File(projectFolder, JReserveProjectFactory.CONFIG_FILE));
        } catch (JAXBException ex) {
            String msg = String.format("Unable to create config file in folder: %s", projectFolder.getAbsolutePath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(ex);
        }
    }
}
