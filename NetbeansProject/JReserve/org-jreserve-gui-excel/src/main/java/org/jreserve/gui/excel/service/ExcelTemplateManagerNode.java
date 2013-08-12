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
package org.jreserve.gui.excel.service;

import java.awt.Image;
import java.util.List;
import javax.swing.Action;
import org.jreserve.gui.excel.template.registry.ExcelTemplateManagerAdapter;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ExcelTemplateManagerNode extends AbstractNode {
    private final static String ACTION_PATH = "Node/ExcelTemplateManagerNode/Actions";  //NOI18
    
    private Action[] actions;

    private final Image img;

    ExcelTemplateManagerNode(ExcelTemplateManagerAdapter adapter) {
        super(
                Children.create(new ExcelTemplateChildren(adapter.getDelegate()), true),
                Lookups.proxy(adapter.getDelegate()));
        img = ImageUtilities.icon2Image(adapter.getIcon());
        setDisplayName(adapter.getDisplayName());
    }

    @Override
    public Image getIcon(int type) {
        return img;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public Action[] getActions(boolean context) {
        if(actions == null)
            actions = initActions();
        return actions;
    }
    
    private Action[] initActions() {
        List<? extends Action> list = Utilities.actionsForPath(ACTION_PATH);
        return list.toArray(new Action[list.size()]);
    }
}
