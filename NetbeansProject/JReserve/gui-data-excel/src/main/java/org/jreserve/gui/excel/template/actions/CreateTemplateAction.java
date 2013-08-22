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
import java.awt.event.ActionListener;
import org.jreserve.gui.excel.template.ExcelTemplateBuilder;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Edit",
    id = "org.jreserve.gui.excel.template.actions.CreateTemplateAction"
)
@ActionRegistration(
    iconBase = "org/jreserve/gui/excel/excel_template_add.png",
    displayName = "#CTL_CreateTemplateAction"
)
@ActionReferences({
    @ActionReference(path = "Ribbon/TaskPanes/Edit/Edit/New", position = 400),
    @ActionReference(path = "Node/ExcelTemplateManagerNode/Actions", position = 100),
    @ActionReference(path = "Node/ExcelTemplateNode/Actions", position = 100),
    @ActionReference(path = "Menu/File", position = 333)
})
@Messages({
    "CTL_CreateTemplateAction=New Template"
})
public final class CreateTemplateAction implements ActionListener {

    private final ExcelTemplateBuilder context;

    public CreateTemplateAction(ExcelTemplateBuilder context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.buildTemplate();
    }
}
