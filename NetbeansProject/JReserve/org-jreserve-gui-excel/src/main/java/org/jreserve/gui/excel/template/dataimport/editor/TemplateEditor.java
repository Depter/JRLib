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
package org.jreserve.gui.excel.template.dataimport.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplate;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplateItem;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplates;
import org.jreserve.gui.excel.template.dataimport.createwizard.TemplateRow;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.TemplateEditor.Rename.Error=Unable to rename template!"
})
class TemplateEditor implements Runnable {
    
    private final DataImportTemplate template;
    private final String newName;
    private final List<TemplateRow> newRows;

    TemplateEditor(DataImportTemplate template, String newName, List<TemplateRow> newRows) {
        this.template = template;
        this.newName = newName;
        this.newRows = newRows;
    }
    
    @Override
    public void run() {
        if(newRows != null)
            template.setItmes(createTemplateItems());
        
        if(!newName.equals(template.getName()))
            rename();
    }
    
    private List<DataImportTemplateItem> createTemplateItems() {
        List<DataImportTemplateItem> items = new ArrayList<DataImportTemplateItem>();
        for(TemplateRow row : newRows)
            items.add(row.createTempalteItem());
        return items;
    }
    
    private void rename() {
        try {
            DataImportTemplates m = (DataImportTemplates) template.getManager();
            m.renameTemplate(template, newName);
        } catch (IOException ex) {
            BubbleUtil.showException(Bundle.MSG_TemplateEditor_Rename_Error(), ex);
        }
    }
    
}
