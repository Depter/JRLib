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

package org.jreserve.gui.misc.namedcontent.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.AbstractListModel;
import org.jreserve.gui.misc.namedcontent.NamedContentChooserController;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class NamedContentListModel extends AbstractListModel {

    private Map<String, TreeItem> cache;
    private List<TreeItem> list = new ArrayList<TreeItem>();

    NamedContentListModel(TreeFolder root, NamedContentChooserController controller, boolean isFolder) {
        cache = new TreeMap<String, TreeItem>();
        for(TreeItem item : root.getChildren())
            cacheContent(item, controller, isFolder);
    }
    
    private void cacheContent(TreeItem item, NamedContentChooserController controller, boolean isFolder) {
        
        if(isFolder) {
            if(item instanceof TreeFolder) {
                cache.put(item.getPath().toLowerCase(), item);
                for(TreeItem child : ((TreeFolder)item).getChildren())
                    cacheContent(child, controller, isFolder);
            }
        } else {
            if(item instanceof TreeFolder) {
                for(TreeItem child : ((TreeFolder)item).getChildren())
                    cacheContent(child, controller, isFolder);
            } else {
                TreeFile file = (TreeFile) item;
                if(controller.acceptsContent(file.getContent()))
                    cache.put(file.getPath().toLowerCase(), file);
            
                for(TreeItem child : file.getChildren())
                    cacheContent(child, controller, isFolder);
            }
        }
    }
    
    void setPath(String path) {
        int prevSize = list.size();
        list.clear();
        if(path != null && path.length() > 0) {
            path = path.toLowerCase();
            for(String key : cache.keySet())
                if(containsPath(key, path))
                    list.add(cache.get(key));
        }
        int newSize = list.size();
        
        if(prevSize > 0)
            super.fireIntervalRemoved(this, 0, prevSize-1);
        if(newSize > 0)
            super.fireIntervalAdded(this, 0, newSize-1);
    }
    
    private boolean containsPath(String key, String path) {
        return key.indexOf(path) >= 0;
    }
    
    @Override
    public int getSize() {
        return list==null? 0 : list.size();
    }
    
    @Override
    public Object getElementAt(int index) {
        return list==null? null : list.get(index);
    }
}
