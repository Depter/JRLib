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

package org.jreserve.gui.triangletable;

import java.awt.Component;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.jreserve.gui.triangletable.trianglemodel.TriangleModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleTableRenderer implements TableCellRenderer {

    private TableCellRenderer headerRenderer = new HeaderRenderer();
    private TableCellRenderer baseRenderer = new DefaultTriangleTableRenderer();
    private TriangleModel triangleModel;
    private List<TriangleLayer> layers;
    
    void setHeaderRenderer(TableCellRenderer renderer) {
        this.headerRenderer = renderer;
    }
    
    void setTriangleModel(TriangleModel triangleModel) {
        this.triangleModel = triangleModel;
    }
    
    void setLayers(List<TriangleLayer> layers) {
        this.layers = layers;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(column--==0)
            return headerRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        int accident = getAccidentIndex(row, column);
        int development = getDevelopmentIndex(row, column);
        
        TableCellRenderer renderer = getLayerRenderer(accident, development);
        return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, accident, development);
    }
    
    private int getAccidentIndex(int row, int column) {
        return triangleModel==null? -1 : triangleModel.getAccidentIndex(row, column);
    }
    
    private int getDevelopmentIndex(int row, int column) {
        return triangleModel==null? -1 : triangleModel.getDevelopmentIndex(row, column);
    }

    private TableCellRenderer getLayerRenderer(int accident, int development) {
        int size = layers==null? 0 : layers.size();
        for(int i=size-1; i>=0; i--) {
            TriangleLayer layer = layers.get(i);
            if(layer.rendersCell(accident, development))
                return layer.getCellRenderer();
        }
        return baseRenderer;
    }
}
