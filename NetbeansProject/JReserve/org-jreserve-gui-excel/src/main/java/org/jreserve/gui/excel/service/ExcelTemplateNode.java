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

import java.util.List;
import javax.swing.Action;
import org.jreserve.gui.excel.template.ExcelTemplate;
import org.jreserve.gui.excel.template.TemplateEvent;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ExcelTemplateNode extends AbstractNode {

    @StaticResource
    private final static String IMG = "org/jreserve/gui/excel/excel_template.png";  //NOI18
    private final static String ACTION_PATH = "Node/ExcelTemplateNode/Actions";  //NOI18
    
    private Action[] actions;
    private final ExcelTemplate template;
    
    private static Lookup createLookup(ExcelTemplate template) {
        return new ProxyLookup(
                Lookups.singleton(template),
                Lookups.proxy(template.getManager())
            );
    }
    
    ExcelTemplateNode(ExcelTemplate template) {
        super(Children.LEAF, createLookup(template));
        this.template = template;
        setDisplayName(template.getName());
        setIconBaseWithExtension(IMG);
        EventBusManager.getDefault().subscribe(this);
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
    
    @EventBusListener
    public void renameEvent(TemplateEvent.TemplateRenamedEvent evt) {
        if(template == evt.getTemplate())
            setDisplayName(template.getName());
    }

    @Override
    public void setName(String s) {
        ExcelTemplate.Renameable cookie = getLookup().lookup(ExcelTemplate.Renameable.class);
        if(cookie != null) {
            cookie.rename(s);
        }
    }

    @Override
    public boolean canRename() {
        return getLookup().lookup(ExcelTemplate.Renameable.class) != null;
    }
    
    
}
