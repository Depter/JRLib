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
package org.jreserve.gui.misc.systemproperties;

import java.beans.IntrospectionException;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PropertyNode extends BeanNode<PropertyItem> {

    PropertyNode(PropertyItem item) throws IntrospectionException {
        super(item, 
              Children.create(new PropertyNodeChildren(item), false));
        setDisplayName(item.getName());
        setShortDescription(item.getRoot());
    }
    
    private static class PropertyNodeChildren extends ChildFactory<PropertyItem> {
        
        private PropertyItem item;

        PropertyNodeChildren(PropertyItem item) {
            this.item = item;
        }
        
        @Override
        protected boolean createKeys(List<PropertyItem> list) {
            list.addAll(item.getChildren());
            return true;
        }

        @Override
        protected Node createNodeForKey(PropertyItem key) {
            try {
                return new PropertyNode(key);
            } catch (Exception ex) {
                return new AbstractNode(Children.LEAF);
            }
        }
    }
}
