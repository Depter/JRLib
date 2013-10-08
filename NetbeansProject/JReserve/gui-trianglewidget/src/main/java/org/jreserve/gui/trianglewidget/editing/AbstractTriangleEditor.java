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
package org.jreserve.gui.trianglewidget.editing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import org.jreserve.gui.localesettings.LocaleSettings;
import org.jreserve.gui.trianglewidget.TriangleWidget;
import org.jreserve.gui.trianglewidget.model.TriangleModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractTriangleEditor implements TriangleEditor {

    private List<TriangleEditorListener> listeners = new ArrayList<TriangleEditorListener>();
    private char decimal = LocaleSettings.getDecimalSeparator();
    private String nan = LocaleSettings.getNaN();
    private String inf = LocaleSettings.getInfinity();
    
    private EditorFocusListener focusListener = new EditorFocusListener();
    private EditorKeyListener keyListener = new EditorKeyListener();
    private JTextField editor = new JTextField();
    private boolean isEditing;
    
    public AbstractTriangleEditor() {
        editor.addFocusListener(focusListener);
        editor.addKeyListener(keyListener);
        editor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        editor.setHorizontalAlignment(JTextField.RIGHT);
    }
    
    @Override
    public Double getEditorValue() {
        String str = editor.getText();
        return LocaleSettings.toDouble(str);
    }

    @Override
    public boolean stopEditing() {
        if(!isEditing)
            return true;
        
        if(getEditorValue() != null) {
            isEditing = false;
            fireEditingStopped();
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public void cancelEditing() {
        isEditing = false;
        fireEditingCancelled();
    }

    @Override
    public void addTriangleEditorListener(TriangleEditorListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeTriangleEditorListener(TriangleEditorListener listener) {
        listeners.remove(listener);
    }
    
    protected void fireEditingStopped() {
        ChangeEvent evt = null;
        for(TriangleEditorListener listener : getListenerArray()) {
            if(evt == null)
                evt = new ChangeEvent(this);
            listener.editingStopped(evt);
        }
    }

    private TriangleEditorListener[] getListenerArray() {
        int size = listeners.size();
        return listeners.toArray(new TriangleEditorListener[size]);
    }
    
    protected void fireEditingCancelled() {
        ChangeEvent evt = null;
        for(TriangleEditorListener listener : getListenerArray()) {
            if(evt == null)
                evt = new ChangeEvent(this);
            listener.editingCancelled(evt);
        }
    }
    
    @Override
    public Component getEditorComponent(TriangleWidget widget, double value, int accident, int development, boolean isSelected) {
        value = escapeValue(widget, value, accident, development);
        editor.setText(toString(value));
        editor.selectAll();
        isEditing = true;
        return editor;
    }
    
    private double escapeValue(TriangleWidget widget, double value, int accident, int development) {
        if(widget.isCummulated() || development == 0)
            return value;
        
        TriangleModel model = widget.getModel();
        int row = model.getRowIndex(accident, development-1);
        int column = model.getColumnIndex(accident, development-1);
        double prev = widget.getModel().getValueAt(row, column);
        return value - prev;
    }
    
    protected String toString(double value) {
        return LocaleSettings.getExactString(value);
    }
    
    private class EditorFocusListener extends FocusAdapter {
        @Override
        public void focusLost(FocusEvent e) {
            if(getEditorValue() != null)
                stopEditing();
        }
    }
    
    private class EditorKeyListener extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            int code = e.getKeyCode();
            if(KeyEvent.VK_ENTER == code) {
                if(getEditorValue() != null)
                    stopEditing();
            } else if(KeyEvent.VK_ESCAPE == code) {
                cancelEditing();
            }
        }
    }
}
