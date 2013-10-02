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
package org.jreserve.gui.data.dataobject.node;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.utils.dataobject.DataObjectProvider;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.WeakListeners;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DFChildren extends ChildFactory<DataObject>{

    private final DataFolder folder;
    private final DataObjectProvider doProvider;
    private final ChildListener cl = new ChildListener();
    
    DFChildren(DataFolder folder, DataObjectProvider doProvider) {
        this.folder = folder;
        this.doProvider = doProvider;
        folder.addPropertyChangeListener(WeakListeners.propertyChange(cl, folder));
    }
    
    @Override
    protected boolean createKeys(List<DataObject> toPopulate) {
        for(DataObject child : folder.getChildren())
           if(accepts(child))
               toPopulate.add(child);
       return true;
    }
    
    private boolean accepts(DataObject child) {
        return (child instanceof DataFolder) ||
               child.getLookup().lookup(DataSource.class) != null;
    }
    
    @Override
    protected Node createNodeForKey(DataObject obj) {
        if(obj instanceof DataFolder) {
            return new DataFolderNode((DataFolder) obj, doProvider, false);
        } else {
            return obj.getNodeDelegate().cloneNode();
        }
    } 
    
    private class ChildListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(DataFolder.PROP_CHILDREN.equals(evt.getPropertyName()))
                refresh(false);
        }
    }
}
