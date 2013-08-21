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

package org.jreserve.gui.data.csv.input;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.jreserve.gui.data.csv.settings.CsvImportSettings;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PreviewRenderer  extends DefaultTableCellRenderer {
    
    private boolean hasRowHeaders = CsvImportSettings.hasRowHeaders();
    
    public void setHasRowHeaders(boolean hasRowHeaders) {
        this.hasRowHeaders = hasRowHeaders;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(column == 0 && hasRowHeaders) {
            setBackground(Color.GRAY);
        } else {
            setBackground(table.getBackground());
        }
        return this;
    }
}
