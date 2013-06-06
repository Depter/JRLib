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

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.jreserve.dummy.claimtriangle.edtior.trianglemodel.TriangleModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleTableModel implements TableModel {

    private TriangleModel model;
    private ModelListener modelListener = new ModelListener();
    private List<TableModelListener> listeners = new ArrayList<TableModelListener>();

    TriangleTableModel() {
    }
    
    TriangleTableModel(TriangleModel model) {
        this.model = model;
        if(model != null)
            model.addChangeListener(modelListener);
    }
    
    TriangleModel getTriangleModel() {
        return model;
    }
    
    void setTriangleModel(TriangleModel model) {
        releaseOldModel();
        initNewModel(model);
        fireChange();
    }
    
    private void releaseOldModel() {
        if(this.model != null)
            this.model.removeChangeListener(modelListener);
    }
    
    private void initNewModel(TriangleModel model) {
        this.model = model;
        if(model != null)
            model.addChangeListener(modelListener);
    }
    
    @Override
    public int getRowCount() {
        return model==null? 0 : model.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return model==null? 0 : model.getColumnCount();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return model.getColumnTitle(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return (columnIndex == 0)?
                String.class :
                Double.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return model.hasCellAt(rowIndex, columnIndex)?
                model.getValueAt(rowIndex, columnIndex) :
                null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public void addTableModelListener(TableModelListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChange() {
        TableModelEvent evt = new TableModelEvent(this);
        for(TableModelListener listener : listenerArray())
            listener.tableChanged(evt);
    }
    
    private TableModelListener[] listenerArray() {
        int size = listeners.size();
        TableModelListener[] result = new TableModelListener[size];
        return listeners.toArray(result);
    }
    
    private class ModelListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }
    } 

}
