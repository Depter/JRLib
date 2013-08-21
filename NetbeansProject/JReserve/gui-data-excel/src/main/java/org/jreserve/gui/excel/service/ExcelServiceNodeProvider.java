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
package org.jreserve.gui.excel.service;

import java.util.Collections;
import java.util.Set;
import org.jreserve.gui.misc.services.ServiceNodeProvider;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceNodeProvider.Registration(
    position = 1000
)
@Messages({
    "LBL.ExcelServiceNodeProvider.RootNode.Name=Excel Templates"
})
public class ExcelServiceNodeProvider implements ServiceNodeProvider {
    
    @StaticResource
    private final static String IMG = "org/jreserve/gui/excel/excel.png";   //NOI18
    
    @Override
    public Set<Node> createNodes() {
        AbstractNode node = new AbstractNode(Children.create(new ExcelServicesChildren(), true), Lookup.EMPTY);
        node.setDisplayName(Bundle.LBL_ExcelServiceNodeProvider_RootNode_Name());
        node.setIconBaseWithExtension(IMG);
        return Collections.singleton((Node) node);
    }
}
