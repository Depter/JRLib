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
package org.jreserve.gui.trianglewidget.geometry;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PeriodStepSpinner extends JSpinner {
    
    private final static int DEFAULT_VALUE = 1;
    private final static int MIN_VALUE = 1;
    private final static int MAX_VALUE = Integer.MAX_VALUE;
    private final static int STEP_SIZE = 1;
    private final static int COLUMM_COUNT = 4;
    
    public PeriodStepSpinner() {
        this(DEFAULT_VALUE);
    }
    
    public PeriodStepSpinner(int start) {
        super(new SpinnerNumberModel(
                start<MIN_VALUE? MIN_VALUE : start, 
                MIN_VALUE, MAX_VALUE, STEP_SIZE));
        JFormattedTextField text = ((JSpinner.DefaultEditor)getEditor()).getTextField();
        text.setColumns(COLUMM_COUNT);
        text.setEditable(false);
        text.setHorizontalAlignment(JTextField.RIGHT);
    }
    
    public int getMonthCount() {
        return (Integer) getValue();
    }
    
    public void setMonthCount(int monthCount) {
        super.setValue(monthCount);
    }
}
