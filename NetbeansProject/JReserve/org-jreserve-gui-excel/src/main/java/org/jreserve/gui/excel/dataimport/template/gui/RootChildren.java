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

package org.jreserve.gui.excel.dataimport.template.gui;

import java.util.Arrays;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class RootChildren extends ChildFactory<RootChildren.Type>{
    
    static enum Type {
        INPUT;
    }

    @Override
    protected boolean createKeys(List<Type> toPopulate) {
        toPopulate.addAll(Arrays.asList(Type.values()));
        return true;
    }

    @Override
    protected Node createNodeForKey(Type key) {
        AbstractNode node = new AbstractNode(Children.LEAF, Lookups.singleton(key));
        
        return node;
    }
    
    
}
