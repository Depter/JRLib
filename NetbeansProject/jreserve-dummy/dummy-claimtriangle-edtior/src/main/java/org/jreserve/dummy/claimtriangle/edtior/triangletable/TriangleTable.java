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

package org.jreserve.dummy.claimtriangle.edtior.triangletable;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.jreserve.dummy.claimtriangle.edtior.trianglemodel.DefaultTriangleModel;
import org.jreserve.dummy.claimtriangle.edtior.trianglemodel.TriangleModel;
import org.jreserve.jrlib.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleTable extends JTable {

    private TriangleTableModel tableModel;

    public TriangleTable() {
        this(new DefaultTriangleModel());
    }
    
    public TriangleTable(Triangle triangle) {
        this(new DefaultTriangleModel(triangle));
    }
    
    public TriangleTable(TriangleModel triangleModel) {
        tableModel = new TriangleTableModel(triangleModel);
    } 
    
    @Override
    public void setModel(TableModel model) {
        throw new UnsupportedOperationException("Do not set TableModel for TriangleTable! Use TriangleModel!");
    }
    
    @Override
    public TableModel getModel() {
        return null;
    }
    
    public void setTriangleModel(TriangleModel model) {
        this.tableModel.setTriangleModel(model);
    }
    
    public TriangleModel getTriangleModel() {
        return this.tableModel.getTriangleModel();
    }
    
    public void setTriangle(Triangle triangle) {
        TriangleModel tModel = getTriangleModel();
        if(tModel == null) {
            tModel = new DefaultTriangleModel(triangle);
            tableModel.setTriangleModel(tModel);
        } else {
            tModel.setTriangle(triangle);
        }
    }
    
    public Triangle getTriangle() {
        TriangleModel tModel = getTriangleModel();
        return tModel==null? null : tModel.getTriangle();
    }
}
