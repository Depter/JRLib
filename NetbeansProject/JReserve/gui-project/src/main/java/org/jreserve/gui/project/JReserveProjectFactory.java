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
package org.jreserve.gui.project;

import java.io.IOException;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectFactory2;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=ProjectFactory.class)
public class JReserveProjectFactory implements ProjectFactory2 {
    
    public final static String LAYER_NAME = "org-jreserve-project-jreserve"; //NOI18
    public final static String CONFIG_FILE = "jrp.xml"; //NOI18
    
    @StaticResource
    final static String ICON_PATH = "org/jreserve/gui/project/project.png"; //NOI18
    private static ImageIcon ICON = null;
    
    @Override
    public boolean isProject(FileObject folder) {
        return folder.getFileObject(CONFIG_FILE) != null;
    }

    @Override
    public Project loadProject(FileObject folder, ProjectState state) throws IOException {
        return isProject(folder)? new JReserveProject(folder, state) : null;
    }

    @Override
    public void saveProject(Project prjct) throws IOException, ClassCastException {
        //TODO implement
    }

    @Override
    public ProjectManager.Result isProject2(FileObject folder) {
        return isProject(folder)?
                new ProjectManager.Result(getProjectIcon()) :
                null;
    }
    
    static ImageIcon getProjectIcon() {
        if(ICON == null)
            ICON = ImageUtilities.loadImageIcon(ICON_PATH, false);
        return ICON;
    }
}
