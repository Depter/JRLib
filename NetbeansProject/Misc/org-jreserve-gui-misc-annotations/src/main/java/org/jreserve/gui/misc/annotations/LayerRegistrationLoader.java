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
package org.jreserve.gui.misc.annotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class LayerRegistrationLoader<T> {
    
    protected final static String POSITION = "position";
    
    private final static Comparator<FileObject> POSITION_COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject o1, FileObject o2) {
            int p1 = getPosition(o1);
            int p2 = getPosition(o2);
            int dif = p1 - p2;
            return dif!=0? dif : o1.getName().compareToIgnoreCase(o2.getName());
        }
        
        private int getPosition(FileObject fo) {
            Object position = fo.getAttribute(POSITION);
            if(position instanceof Number)
                return ((Number) position).intValue();
            return Integer.MAX_VALUE;
        }
    };
    
    public List<T> getValues() {
        String path = getDirectory();
        
        getLogger().log(Level.FINE, "Loading values from \"{0}\"...", path);
        List<T> values = new ArrayList<T>();
        for(FileObject file : getFiles(path)) {
            try {
                values.add(getValue(file));
            } catch (Exception ex) {
                getLogger().log(Level.SEVERE, "Unable to load value from: "+file.getPath(), ex);
            }
        }
        
        return values;
    }
    
    protected abstract Logger getLogger();
    
    protected abstract String getDirectory();
    
    private FileObject[] getFiles(String path) {
        FileObject home = FileUtil.getConfigFile(path);
        if(home == null)
            return new FileObject[0];
        FileObject[] children = home.getChildren();
        Arrays.sort(children, getFileComparator());
        return children;
    }
    
    protected Comparator<FileObject> getFileComparator() {
        return POSITION_COMPARATOR;
    }
    
    protected abstract T getValue(FileObject file) throws Exception;
    
    protected Object loadInstance(FileObject file) throws DataObjectNotFoundException, IOException, ClassNotFoundException {
        DataObject data = DataObject.find(file);
        InstanceCookie cookie = data.getLookup().lookup(InstanceCookie.class);
        Object value = cookie.instanceCreate();
        getLogger().log(Level.FINE, "Loaded instance: {0}", value.getClass().getName());
        return value;
    }
}
