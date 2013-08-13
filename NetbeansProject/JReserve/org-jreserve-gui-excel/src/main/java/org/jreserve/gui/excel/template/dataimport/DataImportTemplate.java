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

import java.util.List;
import org.jreserve.gui.excel.template.ExcelTemplate;
import org.jreserve.gui.excel.template.ExcelTemplateManager;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataImportTemplate implements ExcelTemplate, Comparable<ExcelTemplate> {

    private final DataImportTemplates manager;
    private final FileObject file;
    private List<DataImportTemplateItem> items;

    public DataImportTemplate(FileObject file, DataImportTemplates manager, List<DataImportTemplateItem> items) {
        if(manager == null)
            throw new NullPointerException("Manager can not be null!");
        this.manager = manager;
        if(file == null)
            throw new NullPointerException("File can not be null!");
        this.file = file;
        if(items == null)
            throw new NullPointerException("Items can not be null!");
        this.items = items;
    }
    
    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public ExcelTemplateManager getManager() {
        return manager;
    }
    
    List<DataImportTemplateItem> getItems() {
        return items;
    }
    
    FileObject getFile() {
        return file;
    }

    @Override
    public int compareTo(ExcelTemplate o) {
        if(o == null)
            return -1;
        return getName().compareToIgnoreCase(o.getName());
    }
}
