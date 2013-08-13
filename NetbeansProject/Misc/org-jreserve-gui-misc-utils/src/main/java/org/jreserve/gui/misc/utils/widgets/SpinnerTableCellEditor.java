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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SpinnerTableCellEditor extends DefaultCellEditor {

    private JSpinner spinner;
    private JSpinner.DefaultEditor editor;
    private JTextField textField;
    boolean valueSet;

    public SpinnerTableCellEditor(JSpinner spinner) {
        super(new JTextField());
        this.spinner = spinner;
        editor = ((JSpinner.DefaultEditor)spinner.getEditor());
        textField = editor.getTextField();
        textField.addFocusListener(new TextFocusListener());
        textField.addActionListener(new TextActionListener());
        spinner.addChangeListener(new SpinnerChangeListener());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if(valueSet)
            spinner.setValue(value);
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                textField.requestFocus();
            }
        });
        return spinner;
    }

    @Override
    public boolean isCellEditable(EventObject evt ) {
        if(evt instanceof KeyEvent ) {
            KeyEvent ke = (KeyEvent)evt;
            System.err.println("key event: "+ke.getKeyChar());
            textField.setText(String.valueOf(ke.getKeyChar()));
            valueSet = true;
        } else {
            valueSet = false;
        }
        return true;
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public boolean stopCellEditing() {
        try {
            editor.commitEdit();
            spinner.commitEdit();
        } catch ( java.text.ParseException e ) {
//            BubbleUtil
//            JOptionPane.showMessageDialog(null,
//                    "Invalid value, discarding.");
        }
        return super.stopCellEditing();
    }

    private class TextFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run() {
                    if(valueSet)
                        textField.setCaretPosition(1);
                }
            });
        }

        @Override public void focusLost(FocusEvent e) {}
    }
    
    private class TextActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            stopCellEditing();
        }
    }
    
    private class SpinnerChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            stopCellEditing();
        }
    
    }
}
