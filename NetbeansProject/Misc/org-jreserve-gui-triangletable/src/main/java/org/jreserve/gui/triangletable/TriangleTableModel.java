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

import javax.swing.table.AbstractTableModel;
import org.jreserve.gui.triangletable.trianglemodel.DevelopmentTriangleModel;
import org.jreserve.gui.triangletable.trianglemodel.TriangleModel;
import org.jreserve.jrlib.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleTableModel extends AbstractTableModel {
    
    private TitleModel xAxisModel;
    private TitleModel yAxisModel;
    private TriangleModel triangleModel;
    
    void setTriangle(Triangle triangle) {
        triangleModel.setTriangle(triangle);
    }
    
    void setTriangleModel(TriangleModel triangleModel) {
        this.triangleModel = escaleTriangleModel(triangleModel);
        fireTableStructureChanged();
    }
    
    private TriangleModel escaleTriangleModel(TriangleModel triangleModel) {
        if(triangleModel != null)
            return triangleModel;
        Triangle triangle = this.triangleModel==null? null : this.triangleModel.getTriangle();
        return triangle==null? new DevelopmentTriangleModel() : new DevelopmentTriangleModel(triangle);
    }
    
    void setXAxisModel(TitleModel axisModel) {
        this.xAxisModel = axisModel;
        fireTableStructureChanged();
    }
    
    void setYAxisModel(TitleModel axisModel) {
        this.yAxisModel = axisModel;
        fireTableStructureChanged();
    }
    
    @Override
    public int getRowCount() {
        return triangleModel==null? 0 : triangleModel.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return triangleModel==null? 0 : triangleModel.getColumnCount()+1;
    }

    @Override
    public String getColumnName(int column) {
        return xAxisModel==null? null : xAxisModel.getName(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex==0?
                String.class :
                Double.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(column-- == 0)
            return yAxisModel.getName(row);
        return triangleModel.hasValueAt(row, column)?
                triangleModel.getValueAt(row, column) :
                null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }
}
