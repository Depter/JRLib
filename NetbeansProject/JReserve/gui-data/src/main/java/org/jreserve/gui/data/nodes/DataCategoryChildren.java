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
package org.jreserve.gui.data.nodes;

import java.util.List;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataSource;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataCategoryChildren extends ChildFactory<Object>{
    
    private final DataCategory category;

    DataCategoryChildren(DataCategory category) {
        this.category = category;
    }
    
    @Override
    protected boolean createKeys(List<Object> list) {
        list.addAll(category.getChildCategories());
        list.addAll(category.getDataSources());
        return true;
    }

    @Override
    protected Node createNodeForKey(Object key) {
        if(key instanceof DataCategory) {
            return new DataCategoryNode((DataCategory)key);
        } else if(key instanceof DataSource) {
            return new DataSourceNode((DataSource)key);
        } else {
            throw new IllegalArgumentException("Unknown key: "+key);
        }
    }
    
}
