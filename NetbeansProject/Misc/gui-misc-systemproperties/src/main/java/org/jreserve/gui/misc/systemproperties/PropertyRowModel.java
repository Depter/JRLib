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
package org.jreserve.gui.misc.systemproperties;

import org.netbeans.swing.outline.RowModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PropertyRowModel implements RowModel {

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueFor(Object o, int i) {
        PropertyItem item = (PropertyItem) o;
        return item.getValue();
    }

    @Override
    public Class getColumnClass(int i) {
        return Object.class;
    }

    @Override
    public boolean isCellEditable(Object o, int i) {
        return false;
    }

    @Override
    public void setValueFor(Object o, int i, Object o1) {
    }

    @Override
    public String getColumnName(int i) {
        return "Value";
    }
}
