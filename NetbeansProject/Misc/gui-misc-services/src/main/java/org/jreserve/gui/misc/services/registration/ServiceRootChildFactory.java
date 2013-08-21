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
package org.jreserve.gui.misc.services.registration;

import java.util.List;
import java.util.Set;
import org.jreserve.gui.misc.services.ServiceNodeProvider;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ServiceRootChildFactory extends ChildFactory<ServiceNodeProvider>{

    @Override
    protected boolean createKeys(List<ServiceNodeProvider> toPopulate) {
        toPopulate.addAll(ServiceNodeProviderRegistry.getInstances());
        return true;
    }

    @Override
    protected Node[] createNodesForKey(ServiceNodeProvider key) {
        Set<Node> nodes = key.createNodes();
        if(nodes==null || nodes.isEmpty())
            return null;
        return nodes.toArray(new Node[nodes.size()]);
    }
}
