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

package org.jreserve.gui.project.api.util;

import java.util.logging.Logger;
import org.jreserve.gui.misc.annotations.LayerRegistrationLoader;
import org.jreserve.gui.project.api.ProjectFileProvider;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectFileProviderLoader extends LayerRegistrationLoader<ProjectFileProvider> {

    private final static Logger logger = Logger.getLogger(ProjectFileProviderLoader.class.getName());
    
    private final String dir;
    
    public ProjectFileProviderLoader(String projectType) {
        dir = String.format(ProjectFileProviderRegistrationProcessor.LAYER_PATH, projectType);
    }
    
    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getDirectory() {
        return dir;
    }

    @Override
    protected ProjectFileProvider getValue(FileObject file) throws Exception {
        Object instance = super.loadInstance(file);
        if(instance instanceof ProjectFileProvider)
            return (ProjectFileProvider) instance;
        throw new Exception("File "+file.getPath()+" is not an instance of: "+ProjectFileProvider.class);
    }

}
