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

package org.jreserve.gui.data.dataobject;

import org.jreserve.gui.data.api.DataSource;
import org.jreserve.jrlib.gui.data.DataType;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataSourceNode extends DataNode {

    @StaticResource final static String IMG_TRIANGLE = "org/jreserve/gui/data/icons/database_triangle.png";
    @StaticResource final static String IMG_VECTOR = "org/jreserve/gui/data/icons/database_vector.png";
    
    public DataSourceNode(DataSourceDataObject obj) {
        super(obj, Children.LEAF, obj.getLookup());
        super.setDisplayName(obj.getName());
        if(getLookup().lookup(DataSource.class).getDataType() == DataType.TRIANGLE) {
            super.setIconBaseWithExtension(IMG_TRIANGLE);
        } else {
            super.setIconBaseWithExtension(IMG_VECTOR);
        }
    }
}
