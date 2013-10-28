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
package org.jreserve.gui.calculations.util;

import java.awt.Color;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.jreserve.gui.calculations.api.DefaultColor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ColorTableModel.Name=Name",
    "LBL.ColorTableModel.Color=Color"
})
class ColorTableModel extends AbstractTableModel {
    
    private List<DefaultColorAdapter> colors;

    ColorTableModel() {
    }
    
    void setColors(List<DefaultColorAdapter> colors) {
        this.colors = colors;
        super.fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return colors==null? 0 : colors.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return Bundle.LBL_ColorTableModel_Name();
        return Bundle.LBL_ColorTableModel_Color();
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 0)
            return String.class;
        return Color.class;
    }

    @Override
    public Object getValueAt(int row, int column) {
        DefaultColorAdapter adapter = colors.get(row);
        if(column == 0)
            return adapter.getDisplayName();
        return DefaultColor.getColor(adapter.getId());
    }
    
    DefaultColorAdapter getColorAt(int row) {
        return colors.get(row);
    }
}
