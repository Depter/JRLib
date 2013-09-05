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
package org.jreserve.gui.data.dataobject.nodes;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.gui.misc.utils.dataobject.DataObjectProvider;
import org.openide.loaders.DataFolder;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataFolderChildren extends FilterNode.Children {
    
    private final DataObjectProvider doProvider;
    
    public DataFolderChildren(Node or, DataObjectProvider doProvider) {
        super(or);
        this.doProvider = doProvider;
    }

    @Override
    protected Node[] createNodes(Node key) {
        List<Node> result = new ArrayList<Node>();
        for(Node node : super.createNodes(key))
            if(accepts(node))
                result.add(node);
        return result.toArray(new Node[result.size()]);
    }
    
    private boolean accepts(Node node) {
        return true;
    }

    @Override
    protected Node copyNode(Node node) {
        DataFolder folder = node.getLookup().lookup(DataFolder.class);
        return folder==null? node : new DataFolderNode(node, doProvider, false);
    }
}
