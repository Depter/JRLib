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

import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.support.LookupProviderSupport;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class JReserveProject implements Project {
    
    private final static String LOOKUP_PATH = "Projects/"+JReserveProjectFactory.LAYER_NAME+"/Lookup";
    
    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;
    
    JReserveProject(FileObject folder, ProjectState state) {
        this.projectDir = folder;
        this.state = state;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if(lkp == null)
            lkp = createLookup();
        return lkp;
    }
    
    private Lookup createLookup() {
        Object[] baseInfo = {
            this,
            new JReserveProjectInformation(this),
            new JReserveLogicalView(this),
            new JReserveProjectCustomizer(this)
        };
        return LookupProviderSupport.createCompositeLookup(
                Lookups.fixed(baseInfo), LOOKUP_PATH);
    }
    
//    private Object[] createLookupContent() {
//        return new Object[] {
//            this,
//            new JReserveProjectInformation(this),
//            new JReserveLogicalView(this),
//            new JReserveProjectCustomizer(this)
//        };
//    }
}
