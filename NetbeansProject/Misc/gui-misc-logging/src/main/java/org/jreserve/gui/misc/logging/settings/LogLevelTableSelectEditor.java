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
package org.jreserve.gui.misc.logging.settings;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LogLevelTableSelectEditor extends JComboBox implements TableCellEditor {
    private final static int EDIT_CLICK_COUNT = 1;

    private final static Level[] LEVELS = {
        Level.OFF,
        Level.SEVERE,
        Level.WARNING,
        Level.INFO,
        Level.CONFIG,
        Level.FINE,
        Level.FINER,
        Level.FINEST,
        Level.ALL
    };
    
    private List<CellEditorListener> listeners = new ArrayList<CellEditorListener>();
    
    LogLevelTableSelectEditor() {
        super(LEVELS);
        super.setRenderer(new LevelComboRenderer());

        EditStopListener l = new EditStopListener();
        super.addFocusListener(l);
        super.addKeyListener(l);
        super.addPopupMenuListener(l);
        super.addActionListener(l);
    }

    @Override
    public boolean isCellEditable(EventObject evt) {
        return evt == null || 
               !(evt instanceof MouseEvent) ||
               ((MouseEvent) evt).getClickCount() >= EDIT_CLICK_COUNT;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }
    
    private void fireEditingStopped() {
        ChangeEvent evt = new ChangeEvent(this);
        for(CellEditorListener listener : new ArrayList<CellEditorListener>(listeners))
            listener.editingStopped(evt);
    }
    
    private void fireEditingCanceled() {
        ChangeEvent evt = new ChangeEvent(this);
        for(CellEditorListener listener : new ArrayList<CellEditorListener>(listeners))
            listener.editingCanceled(evt);
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    @Override
    public void addCellEditorListener(CellEditorListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Object getCellEditorValue() {
        return getSelectedItem();
    }
   
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        setSelectedItem(value);
        return this;
    }
    
    private class EditStopListener implements FocusListener, KeyListener, PopupMenuListener, ActionListener {

        //FOCUS LISTENER
        @Override 
        public void focusGained(FocusEvent e) {
        }
        
        @Override
        public void focusLost(FocusEvent e) {
            cancelCellEditing();
        }
        
        //KEY LISTENER
        @Override 
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(KeyEvent.VK_ESCAPE == e.getKeyCode())
                cancelCellEditing();
        }
        
        //POPUP LISTENER
        @Override 
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        }
        
        @Override 
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            cancelCellEditing();
        }
        
        //ACTION LISTENER
        @Override
        public void actionPerformed(ActionEvent e) {
            stopCellEditing();
        }
    }
}
