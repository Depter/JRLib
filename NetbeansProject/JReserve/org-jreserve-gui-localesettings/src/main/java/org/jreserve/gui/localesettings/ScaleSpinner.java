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

package org.jreserve.gui.localesettings;

import java.awt.Dimension;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ScaleSpinner extends JSpinner {
        
    public ScaleSpinner() {
        super(new SpinnerNumberModel(LocaleSettings.getDecimalCount(), Integer.MIN_VALUE, Integer.MAX_VALUE, -1));
        initEditorField();
    }
    
    private void initEditorField() {
        JFormattedTextField text = ((JSpinner.DefaultEditor)super.getEditor()).getTextField();
        text.setEditable(false);
        text.setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {
            @Override
            public AbstractFormatter getFormatter(JFormattedTextField tf) {
                return new ScaleFormatter();
            }
        });
        text.setColumns(10);
        //text.setPreferredSize(new Dimension(50, 24));

    }
    
    private static class ScaleFormatter extends AbstractFormatter {

        private final StringBuilder sb = new StringBuilder();
        
        @Override
        public Object stringToValue(String text) throws ParseException {
            if(text==null || text.length()<2)
                return Integer.valueOf(0);
            char sep = LocaleSettings.getDecimalSeparator();
            int sepIndex = text.indexOf(sep);
            if(sepIndex >= 0)
                return text.length()-sepIndex-1;
            
            int length = text.length();
            int scale = 0;
            for(int i=0; i<length; i++)
                if(text.charAt(i) == '0')
                    scale--;
            return scale;
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if(value == null)
                return "1"; //NOI18
            if(!(value instanceof Integer))
                throw new IllegalArgumentException("Integer expected but found: "+value);   //NOI18
            
            int scale = (Integer) value;
            if(scale == 0)
                return "1";
            
            sb.setLength(0);
            sb.append('0');
            if(scale < 0)
                appendNegative(scale);
            else if(scale > 0)
                appendPositive(scale);
            
            return sb.toString();
        }
        
        private void appendNegative(int scale) {
            char tSep = LocaleSettings.getThousandSeparator();
            int count = 1;
            while(scale++ < -1) {
                sb.insert(0, '0');
                if(++count == 3) {
                    sb.insert(0, tSep);
                    count = 0;
                }
            }
            sb.insert(0, '1');
        }
        
        private void appendPositive(int scale) {
            sb.append(LocaleSettings.getDecimalSeparator());
            while(scale-- > 1)
                sb.append('0');
            sb.append('1');
        }
    }
}
