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
import javax.swing.Action;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.impl.DataEvent;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataCategoryNode extends AbstractNode {
    
    private final static String ROOT_ICON = "org/jreserve/gui/data/icons/database.png";
    private final static String ICON = "org/jreserve/gui/data/icons/folder_db.png";
    private final static String ACTION_PATH = "Node/DataCategory/Actions";
    
    private final DataCategory category;
    
    DataCategoryNode(DataCategory category) {
        super(
            Children.create(new DataCategoryChildren(category),true),
            Lookups.singleton(category)
        );
        this.category = category;
        setDisplayName(category.getName());
        setIconBaseWithExtension(category.getParent()==null? ROOT_ICON : ICON);
        EventBusManager.getDefault().subscribe(this);
    }
    
    @Override
    public Action[] getActions(boolean arg0) {
        List<? extends Action> actions = Utilities.actionsForPath(ACTION_PATH);
        int size = actions.size();
        return actions.toArray(new Action[size]);
    }    
    
    @EventBusListener
    public void categoryCreated(DataEvent.DataCategoryCreatedEvent evt) {
        DataCategory child = evt.getDataCategory();
        if(this.category == child.getParent())
            setChildren(Children.create(new DataCategoryChildren(category), true));
    }
}
