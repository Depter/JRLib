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
package org.jreserve.gui.misc.layerexplorer;

import java.util.Enumeration;
import java.util.TreeMap;
import javax.swing.table.DefaultTableModel;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 */
public class FileAttributeModel extends DefaultTableModel {
    
    private TreeMap<String, Object> attributes = new TreeMap<String, Object>();
    private String[] attributeNames;
    
    public void setFileObject(FileObject file) {
        attributes.clear();
        Enumeration<String> names = file.getAttributes();
        while(names.hasMoreElements()) {
            String name = names.nextElement();
            attributes.put(name, file.getAttribute(name));
        }
        attributeNames = attributes.navigableKeySet().toArray(new String[0]);
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return attributes==null? 0 : attributes.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        if(column==0)
            return "Attribute";
        return "Value";
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        String name = attributeNames[row];
        if(column==0)
            return name;
        return attributes.get(name);
    }
}
