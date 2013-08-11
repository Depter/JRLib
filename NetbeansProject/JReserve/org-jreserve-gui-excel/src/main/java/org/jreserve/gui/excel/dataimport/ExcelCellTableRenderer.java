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
package org.jreserve.gui.excel.dataimport;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ExcelCellTableRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setForeground(Color.BLACK);
        setBackground(table.getBackground());
        setHorizontalAlignment(SwingConstants.RIGHT);

        ExcelCell cell = (ExcelCell) value;
        if(cell != null) {
            if(ExcelCell.Type.STRING == cell.getType()) {
                setForeground(Color.RED);
            } else if(ExcelCell.Type.BLANK == cell.getType()) {
                setBackground(Color.DARK_GRAY);
            }
            value = cell.getValue();
            setText(value == null ? null : "" + value);
        }

        return this;
    }
}
