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
package org.jreserve.gui.calculations.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public abstract class AbstractCalculationSourceClient<T, S> {
    
    private final static Logger logger = Logger.getLogger(AbstractCalculationSourceClient.class.getName());
    
    protected List<T> createTasks(DataObject obj) {
        S source = getSource(obj);
        if(source == null)
            return Collections.EMPTY_LIST;
        
        DataFolder root = getCalculationRoot(obj);
        if(root == null)
            return Collections.EMPTY_LIST;
        
        List<T> result = new ArrayList<T>();
        Enumeration<DataObject> children = root.children(true);
        while(children.hasMoreElements()) {
            DataObject child = children.nextElement();
            if(hasSource(child, source)) {
                T task = createTask(child, source);
                if(task != null)
                    result.add(task);
            }
        }
        
        return result;
    }
    
    protected abstract S getSource(DataObject obj);
    
    private DataFolder getCalculationRoot(DataObject obj) {
        FileObject file = obj.getPrimaryFile();
        Project project = FileOwnerQuery.getOwner(file);
        if(project == null)
            return null;
        
        NamedCalculationProvider cop = project.getLookup().lookup(NamedCalculationProvider.class);
        if(cop == null)
            return null;
        
        FileObject root = cop.getRootFolder();
        if(root == null)
            return null;
        
        try {
            return (DataFolder) DataObject.find(root);
        } catch (Exception ex) {
            String msg = "Unable to load root folder for calculations!";
            logger.log(Level.WARNING, msg, ex);
            return null;
        }
    }
    
    protected abstract boolean hasSource(DataObject calcObj, S source);

    protected abstract T createTask(DataObject calcObj, S source);
}
