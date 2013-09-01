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

import java.util.List;
import org.jreserve.gui.calculations.api.CalculationProvider;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.ChildFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CalculationsFolderNodeChildren extends ChildFactory<DataObject> {
    
    private DataFolder folder;

    CalculationsFolderNodeChildren(DataFolder folder) {
        this.folder = folder;
    }
    
    @Override
    protected boolean createKeys(List<DataObject> toPopulate) {
        if(folder != null)
            loadDataObjects(toPopulate);
        return true;
    }
    
    private void loadDataObjects(List<DataObject> toPopulate) {
        for(DataObject child : folder.getChildren()) {
            if(child.getLookup().lookup(CalculationProvider.class)!=null)
                toPopulate.add(child);
        }
    }
}
