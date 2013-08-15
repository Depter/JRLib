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
package org.jreserve.gui.excel.template.dataimport;

import java.io.IOException;
import java.util.List;
import org.jreserve.gui.excel.template.ExcelTemplate;
import org.jreserve.gui.excel.template.ExcelTemplateManager;
import org.jreserve.gui.excel.template.dataimport.editor.DataImportTemplateEditor;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.notifications.DialogUtil;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - name",
    "MSG.DataImportTemplate.Rename.NameExists=Name ''{0}'' already exists!!",
    "MSG.DataImportTemplate.Rename.Error=Can not rename template!"
})
public class DataImportTemplate implements 
        ExcelTemplate, ExcelTemplate.Editor, 
        ExcelTemplate.Renameable, Comparable<ExcelTemplate> {

    private final DataImportTemplates manager;
    private final FileObject file;
    private String name;
    private List<DataImportTemplateItem> items;

    public DataImportTemplate(FileObject file, DataImportTemplates manager, List<DataImportTemplateItem> items) {
        if(manager == null)
            throw new NullPointerException("Manager can not be null!");
        this.manager = manager;
        if(file == null)
            throw new NullPointerException("File can not be null!");
        this.file = file;
        this.name = file.getName();
        if(items == null)
            throw new NullPointerException("Items can not be null!");
        this.items = items;
    }
    
    public synchronized void rename(String newName) {
        try {
            if(isNewName(newName))  {
                manager.renameTemplate(this, newName);
            } else {
                DialogUtil.showError(Bundle.MSG_DataImportTemplate_Rename_NameExists(newName));
            }
        } catch (Exception ex) {
            BubbleUtil.showException(Bundle.MSG_DataImportTemplate_Rename_Error(), ex);
        }
    }
    
    private boolean isNewName(String newName) {
        for(DataImportTemplate template : manager.getTemplates()) 
            if(template.getName().equals(newName))
                return false;
        return true;
    }
    
    void renameFile(String newName) throws IOException {
        FileLock lock = null;
        try {
            lock = file.lock();
            file.rename(lock, newName, file.getExt());
            this.name = newName;
        } finally {
            if(lock != null)
                lock.releaseLock();
        }
    }
    
    @Override
    public synchronized String getName() {
        return name;
    }

    @Override
    public ExcelTemplateManager<DataImportTemplate> getManager() {
        return manager;
    }
    
    public synchronized List<DataImportTemplateItem> getItems() {
        return items;
    }
    
    public synchronized void setItmes(List<DataImportTemplateItem> items) {
        this.items.clear();
        if(items != null)
            this.items.addAll(items);
        TemplateLoader.save(this);
    }
    
    FileObject getFile() {
        return file;
    }

    @Override
    public synchronized int compareTo(ExcelTemplate o) {
        if(o == null)
            return -1;
        return getName().compareToIgnoreCase(o.getName());
    }

    @Override
    public void edit() {
        DataImportTemplateEditor.editTemplate(this);
    }
}
