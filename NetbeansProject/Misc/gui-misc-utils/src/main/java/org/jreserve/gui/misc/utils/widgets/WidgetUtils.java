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
import java.awt.Toolkit;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WidgetUtils {
    
    
    public static ListCellRenderer displayableListRenderer() {
        return new DisplayableListRenderer();
    }
    
    public static void oneCharacterInput(JTextComponent txt) {
        Document doc = txt.getDocument();
        if(doc instanceof AbstractDocument) {
            ((AbstractDocument) doc).setDocumentFilter(new CharacterDocumentFilter());
        } else {
            throw new IllegalArgumentException("Component "+txt+" does not have an AbstractDocument as Document! "+doc.getClass().getName());
        }
    }
    
    private static class DisplayableListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if(value instanceof Displayable) {
                Displayable d = (Displayable) value;
                setIcon(d.getIcon());
                setText(d.getDisplayName());
            } else {
                setIcon(null);
            }
            return this;
        }
    }
    
    private static class CharacterDocumentFilter extends DocumentFilter {
        
        @Override
        public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
            if(fb.getDocument().getLength() > 0) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            
            if(str.length() > 1) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            
            if(Character.isDigit(str.charAt(0))) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            super.insertString(fb, offset, str, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
            if(str.length() > 1) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            
            if(Character.isDigit(str.charAt(0))) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            super.replace(fb, offset, length, str, attrs); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
}
