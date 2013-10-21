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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.data.api.NamedDataSourceProvider;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

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

    private final static Logger logger = Logger.getLogger(DataRootNodeProvider.class.getName());
    
    @Override
    public NodeList<?> createNodes(Project p) {
        NamedDataSourceProvider dsop = p.getLookup().lookup(NamedDataSourceProvider.class);
        if(dsop == null) {
            String msg = "Project '%s' does not contain an instance of '%s'!";
            logger.log(Level.WARNING, String.format(msg, p.getProjectDirectory().getPath(), NamedDataSourceProvider.class));
            return NodeFactorySupport.fixedNodeList();
        }
        
        FileObject root = dsop.getRoot();
        if(root == null) {
            String msg = "Project '%s' does not contain an data directory!";
            logger.log(Level.WARNING, String.format(msg, p.getProjectDirectory().getPath()));
            return NodeFactorySupport.fixedNodeList();
        }
        
        DataFolder df;
        try {
            df = (DataFolder) DataObject.find(root);
        } catch(DataObjectNotFoundException ex) {
            String msg = "DataFolder can not be loaded from '%s'!";
            logger.log(Level.WARNING, String.format(msg, root.getPath()));
            return NodeFactorySupport.fixedNodeList();
        }

        DataFolderNode rootNode = new DataFolderNode(df, dsop, true);
        return NodeFactorySupport.fixedNodeList(rootNode);
    }
}
