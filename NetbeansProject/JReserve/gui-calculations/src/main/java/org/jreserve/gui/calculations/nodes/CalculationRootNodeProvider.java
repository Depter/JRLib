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

import org.jreserve.gui.calculations.CalculationObjectProvider;
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
    position = 200,
    projectType = "org-jreserve-project-jreserve"
)
public class CalculationRootNodeProvider implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project p) {
        CalculationObjectProvider dsop = p.getLookup().lookup(CalculationObjectProvider.class);
        DataFolder df = dsop.getRootFolder();
        if(df == null)
            return NodeFactorySupport.fixedNodeList();
        CalculationFolderNode root = new CalculationFolderNode(df.getNodeDelegate(), dsop, true);
        return NodeFactorySupport.fixedNodeList(root);
    }
}
