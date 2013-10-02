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
package org.jreserve.gui.data.dataobject.node;

import org.jreserve.gui.data.dataobject.DataRootFileProvider;
import org.jreserve.gui.data.api.DataSourceObjectProvider;
import org.jreserve.gui.misc.utils.dataobject.ProjectObjectLookup;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.loaders.DataFolder;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NodeFactory.Registration(
    position = 100,
    projectType = "org-jreserve-project-jreserve"
)
public class DataRootNodeProvider implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project p) {
        DataSourceObjectProvider dsop = p.getLookup().lookup(DataSourceObjectProvider.class);
        DataFolder df = getRootFolder(p);
        if(df == null)
            return NodeFactorySupport.fixedNodeList();
        DataFolderNode root = new DataFolderNode(df, dsop, true);
        return NodeFactorySupport.fixedNodeList(root);
    }
    
    private DataFolder getRootFolder(Project p)  {
        ProjectObjectLookup pol = p.getLookup().lookup(ProjectObjectLookup.class);
        if(pol == null)
            return null;
        return pol.lookupOne(DataRootFileProvider.DATA_FOLDER, DataFolder.class);
    }
}
