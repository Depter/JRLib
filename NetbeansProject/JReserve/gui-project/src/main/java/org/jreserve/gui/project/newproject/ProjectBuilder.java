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

package org.jreserve.gui.project.newproject;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jreserve.gui.project.JReserveProjectFactory;
import org.jreserve.gui.project.api.ProjectFileProvider;
import org.jreserve.gui.project.api.util.ProjectFileProviderLoader;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProjectBuilder {
    
    static Set<FileObject> buildProject(File folder) throws IOException {
        createProjectFolder(folder);
        createSubFiles(folder);
        return Collections.singleton(FileUtil.toFileObject(folder));
    }

    private static void createProjectFolder(File folder) throws IOException {
        if(!folder.mkdir())
            throw new IOException("Unable to create folder: "+folder.getAbsolutePath());
    }
    
    private static void createSubFiles(File folder) throws IOException {
        for(ProjectFileProvider provider : getFileProviders()) {
            provider.createFiles(folder);
        }
    }
    
    private static List<ProjectFileProvider> getFileProviders() {
        return new ProjectFileProviderLoader(JReserveProjectFactory.LAYER_NAME).getValues();
    }
}
