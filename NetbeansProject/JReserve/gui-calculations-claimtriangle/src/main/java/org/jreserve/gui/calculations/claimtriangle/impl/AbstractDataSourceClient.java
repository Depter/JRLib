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

package org.jreserve.gui.calculations.claimtriangle.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.jreserve.gui.calculations.api.CalculationObjectProvider;
import org.jreserve.gui.data.api.DataSource;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractDataSourceClient<T> {
    
    protected List<T> createTasks(DataObject obj) {
        DataSource ds = obj.getLookup().lookup(DataSource.class);
        if(ds == null)
            return Collections.EMPTY_LIST;
        
        DataFolder root = getCalculationRoot(obj);
        if(root == null)
            return Collections.EMPTY_LIST;
        
        List<T> result = new ArrayList<T>();
        Enumeration<DataObject> children = root.children(true);
        while(children.hasMoreElements()) {
            DataObject child = children.nextElement();
            if(hasDataSource(obj, ds)) {
                T task = createTask(child, ds);
                if(task != null)
                    result.add(task);
            }
        }
        
        return result;
    }
    
    private DataFolder getCalculationRoot(DataObject obj) {
        FileObject file = obj.getPrimaryFile();
        Project project = FileOwnerQuery.getOwner(file);
        if(project == null)
            return null;
        
        CalculationObjectProvider cop = project.getLookup().lookup(CalculationObjectProvider.class);
        if(cop == null)
            return null;
        
        return cop.getRootFolder();
    }
    
    private boolean hasDataSource(DataObject obj, DataSource ds) {
        ClaimTriangleCalculationImpl calc = obj.getLookup().lookup(ClaimTriangleCalculationImpl.class);
        if(calc == null)
            return false;
        return ds == calc.getDataSource();
    }
    
    protected abstract T createTask(DataObject calcObj, DataSource ds);
}
