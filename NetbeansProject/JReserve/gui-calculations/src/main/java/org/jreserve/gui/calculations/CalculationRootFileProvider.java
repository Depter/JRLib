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

package org.jreserve.gui.calculations;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.project.api.ProjectFileProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectFileProvider.Registration(
    project="org-jreserve-project-jreserve"
)
public class CalculationRootFileProvider implements ProjectFileProvider {
    
    public final static String CALCULATIONS_FOLDER = "Calculations";   //NOI18
    
    @Override
    public void createFiles(File projectFolder) throws IOException {
        File dataFolder = new File(projectFolder, CALCULATIONS_FOLDER);
        if(!dataFolder.mkdir()) {
            throw new IOException("Unable to create directory: "+dataFolder.getAbsolutePath());
        } else {
            Logger logger = Logger.getLogger(CalculationRootFileProvider.class.getName());
            logger.log(Level.INFO,"Created folder \"{0}\" in directory: {1}", new Object[]{CALCULATIONS_FOLDER, projectFolder.getAbsolutePath()});
        }
    }   
}
