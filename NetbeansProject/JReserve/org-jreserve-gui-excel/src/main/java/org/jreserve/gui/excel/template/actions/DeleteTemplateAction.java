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
package org.jreserve.gui.excel.template.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.jreserve.gui.excel.template.ExcelTemplate;
import org.jreserve.gui.excel.template.ExcelTemplateManager;
import org.jreserve.gui.misc.utils.widgets.AbstractContextAwareAction;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "Edit",
    id = "org.jreserve.gui.excel.template.actions.DeleteTemplateAction"
)
@ActionRegistration(
    displayName = "#CTL_DeleteTemplateAction",
    lazy = false
)
@ActionReferences({
    @ActionReference(path = "Ribbon/TaskPanes/Edit/Excel Templates", position = 300),
    @ActionReference(path = "Node/ExcelTemplateNode/Actions", position = 300, separatorBefore = 290),
    @ActionReference(path = "Menu/File", position = 333)
})
@Messages({
    "CTL_DeleteTemplateAction=Delete Template"
})
public class DeleteTemplateAction extends AbstractContextAwareAction {
    
    @StaticResource private final static String SMALL_IMG = "org/jreserve/gui/excel/excel_template_delete.png";   //NOI18
    @StaticResource private final static String LARGE_IMG = "org/jreserve/gui/excel/excel_template_delete32.png"; //NOI18
    
    private List<ExcelTemplate> templates = new ArrayList<ExcelTemplate>();
    
    public DeleteTemplateAction() {
        this(Utilities.actionsGlobalContext());
    }
    
    public DeleteTemplateAction(Lookup lkp) {
        super(lkp);
        putValue(Action.NAME, Bundle.CTL_DeleteTemplateAction());
        super.putValue(Action.LARGE_ICON_KEY, ImageUtilities.loadImageIcon(LARGE_IMG, false));
        super.putValue(Action.SMALL_ICON, ImageUtilities.loadImageIcon(SMALL_IMG, false));
    }

    @Override
    protected boolean shouldEnable(Lookup context) {
        templates.clear();
        templates.addAll(context.lookupAll(ExcelTemplate.class));
        
        if(templates.isEmpty())
            return false;
        
        ExcelTemplateManager manager = null;
        for(ExcelTemplate template : templates) {
            if(manager == null) {
                manager = template.getManager();
                if(manager == null) 
                    return false;
            } else if(manager != template.getManager()) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    protected void performAction(ActionEvent evt) {
        DeleteTemplateDialog.showDialog(templates);
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new DeleteTemplateAction(actionContext);
    }
}
