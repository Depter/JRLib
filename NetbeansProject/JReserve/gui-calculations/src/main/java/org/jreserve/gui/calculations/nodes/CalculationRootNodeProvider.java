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

package org.jreserve.gui.calculations.nodes;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.calculations.api.NamedCalculationProvider;
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
    position = 200,
    projectType = "org-jreserve-project-jreserve"
)
public class CalculationRootNodeProvider implements NodeFactory {

    private final static Logger logger = Logger.getLogger(CalculationRootNodeProvider.class.getName());
    
    @Override
    public NodeList<?> createNodes(Project p) {
        NamedCalculationProvider dsop = p.getLookup().lookup(NamedCalculationProvider.class);
        if(dsop == null) {
            String msg = "Project '%s' does not contain an instance of '%s'!";
            logger.log(Level.WARNING, String.format(msg, p.getProjectDirectory().getPath(), NamedCalculationProvider.class));
            return NodeFactorySupport.fixedNodeList();
        }
        
        FileObject rootFile = dsop.getRoot();
        if(rootFile == null) {
            String msg = "Project '%s' does not contain an data directory!";
            logger.log(Level.WARNING, String.format(msg, p.getProjectDirectory().getPath()));
            return NodeFactorySupport.fixedNodeList();
        }
        
        DataFolder df;
        try {
            df = (DataFolder) DataObject.find(rootFile);
        } catch(DataObjectNotFoundException ex) {
            String msg = "DataFolder can not be loaded from '%s'!";
            logger.log(Level.WARNING, String.format(msg, rootFile.getPath()));
            return NodeFactorySupport.fixedNodeList();
        }
        
        CalculationFolderNode root = new CalculationFolderNode(df, dsop, true);
        return NodeFactorySupport.fixedNodeList(root);
    }
}
