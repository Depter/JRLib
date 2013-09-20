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
package org.jreserve.gui.misc.utils.dataobject;

import java.util.Collection;
import java.util.Collections;
import java.util.StringTokenizer;
import org.netbeans.api.project.Project;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultProjectObjectLookup implements ProjectObjectLookup {
    
    private final Project project;

    public DefaultProjectObjectLookup(Project project) {
        this.project = project;
    }
    
    @Override
    public <T> Collection<? extends T> lookup(String path, Class<T> clazz) {
        DataObject obj = getObject(path);
        if(obj == null)
            return Collections.EMPTY_LIST;
        return obj.getLookup().lookupAll(clazz);
    }
    
    private DataObject getObject(String path) {
        if(path==null || path.length()==0)
            return null;
        StringTokenizer elements = new StringTokenizer(path, "/", false);
        if(!elements.hasMoreTokens())
            return null;
        
        DataFolder root = getRoot(elements.nextToken());
        if(root == null || !elements.hasMoreTokens())
            return root;
        return getObject(root, elements);
    }
    
    private DataFolder getRoot(String rootName) {
        for(DataObjectProvider provider : project.getLookup().lookupAll(DataObjectProvider.class)) {
            DataFolder root = provider.getRootFolder();
            if(root != null && root.getName().equals(rootName))
                return root;
        }
        return null;
    }
    
    private DataObject getObject(DataFolder parent, StringTokenizer path) {
        String name = path.nextToken();
        for(DataObject child : parent.getChildren()) {
            if(child.getName().equals(name)) {
                if(path.hasMoreTokens()) {
                    if(child instanceof DataFolder)
                        return getObject((DataFolder)child, path);
                } else {
                    return child;
                }
            }
        }
        
        return null;
    }
    
    @Override
    public <T> T lookupOne(String path, Class<T> clazz) {
        Collection<? extends T> all = lookup(path, clazz);
        if(all.isEmpty())
            return null;
        return all.iterator().next();
    }
}
