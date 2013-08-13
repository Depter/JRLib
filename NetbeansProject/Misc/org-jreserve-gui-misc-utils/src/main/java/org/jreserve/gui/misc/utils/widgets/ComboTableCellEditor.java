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
package org.jreserve.gui.misc.utils.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ComboTableCellEditor extends DefaultCellEditor {

    private final JComboBox combo;

    public ComboTableCellEditor(JComboBox combo) {
        super(combo);
        this.combo = combo;
        combo.removeActionListener(this.delegate);
        this.delegate = new ComboEditorDelegate();
        combo.addActionListener(this.delegate);
    }

    private class ComboEditorDelegate extends EditorDelegate {

        @Override
        public void setValue(Object value) {
            combo.setSelectedItem(value);
        }

        @Override
        public Object getCellEditorValue() {
            return combo.getSelectedItem();
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                MouseEvent e = (MouseEvent) anEvent;
                return e.getID() != MouseEvent.MOUSE_DRAGGED;
            }
            return true;
        }

        @Override
        public boolean stopCellEditing() {
            if(combo.isEditable())
                combo.actionPerformed(new ActionEvent(ComboTableCellEditor.this, 0, ""));
            return super.stopCellEditing();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ComboTableCellEditor.this.stopCellEditing();
        }
    }
}
