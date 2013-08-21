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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import org.jreserve.jrlib.gui.data.MonthDate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MonthDateSpinner extends JSpinner {
    
    private final static int STEP_UNIT = Calendar.MONTH;
    private final static String PATTERN = "yyyy-MM";
    
    private final static MonthDate.Factory MDF = new MonthDate.Factory();
    
    private static Date toDate(MonthDate md) {
        if(md == null)
            return null;
        return MDF.toDate(md);
    }
    
    private static MonthDate toMonthDate(Date date) {
        return date==null? null : MDF.toMonthDate(date);
    }
    
    public MonthDateSpinner() {
        this(new MonthDate());
    }
    
    public MonthDateSpinner(MonthDate value) {
        this(value, null, null);
    }
    
    private Date minDate;
    private Date maxDate;
    
    public MonthDateSpinner(MonthDate value, MonthDate min, MonthDate max) {
        super(new SpinnerDateModel(MDF.toDate(value), toDate(min), toDate(max), STEP_UNIT));
        SpinnerDateModel model = (SpinnerDateModel) super.getModel();
        minDate = (Date) model.getStart();
        maxDate = (Date) model.getEnd();
        
        JFormattedTextField text = ((JSpinner.DateEditor) getEditor()).getTextField();
        DateFormatter df = new DateFormatter(new SimpleDateFormat(PATTERN));
        text.setFormatterFactory(new DefaultFormatterFactory(df));
        text.setColumns(PATTERN.length());
        text.setEditable(false);
        text.setHorizontalAlignment(JTextField.RIGHT);

        revalidate();
        repaint();
    }
    
    public MonthDate getMonthDate() {
        return toMonthDate((Date) getValue());
    }
    
    public void setMonthDate(MonthDate md) {
        setValue(convertToDate(md));
    }
    
    private Date convertToDate(MonthDate md) {
        if(md != null)
            return toDate(md);
        Date date = new Date();
        if(minDate != null && date.before(minDate))
            date = minDate;
        if(maxDate != null && date.after(maxDate))
            date = maxDate;
        return date;
    }
}
