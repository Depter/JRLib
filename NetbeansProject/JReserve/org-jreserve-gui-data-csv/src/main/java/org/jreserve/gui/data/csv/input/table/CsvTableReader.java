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
package org.jreserve.gui.data.csv.input.table;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jreserve.gui.data.api.DataType;
import org.jreserve.gui.data.api.DataEntry;
import org.jreserve.gui.data.api.MonthDate;
import org.jreserve.gui.data.csv.input.AbstractCsvReader;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CsvTableReader extends AbstractCsvReader<List<DataEntry>, CsvTableImportWizardPanel.ValidationData>{
    
    private final List<DataEntry> result = new ArrayList<DataEntry>();
    
    private int columnIndex;
    private SimpleDateFormat sdf;
    private MonthDate.Factory mdf;
    private boolean isTriangle;
    
    CsvTableReader(CsvTableImportWizardPanel.ValidationData config) {
        super(config);
        this.sdf = new SimpleDateFormat(config.dateFormat);
        this.mdf = new MonthDate.Factory();
        isTriangle = (DataType.TRIANGLE == config.dt);
    }
    

    @Override
    protected List<DataEntry> getResult() throws IOException {
        return result;
    }

    @Override
    protected void readRow(String[] cells) throws IOException {
        Date accident = null;
        Date development = null;
        double value;
        columnIndex = 0;
        
        try {
            accident = getDate(cells[columnIndex]);
            columnIndex++;
        } catch (Exception ex) {
            throw accidentException(ex, cells[columnIndex]);
        }
        
        try {
            development = getDate(cells[columnIndex]);
            columnIndex++;
        } catch (Exception ex) {
            if(isTriangle)
                throw developmentException(ex, cells[columnIndex]);
        }
        
        try {
            value = getDouble(cells[columnIndex]);
        } catch (Exception ex) {
            throw valueException(ex, cells[columnIndex]);
        }
        
        DataEntry entry;
        if(isTriangle)
            entry = new DataEntry(mdf.toMonthDate(accident), mdf.toMonthDate(development), value);
        else
            entry = new DataEntry(mdf.toMonthDate(accident), value);
        
        int index = result.indexOf(entry);
        if(index >= 0)
            throw duplicateEntryException(index, entry);
        result.add(entry);
    }
    
    private Date getDate(String str) throws ParseException {
        return sdf.parse(str);
    }
    
    private IOException accidentException(Exception ex, String cell) {
        String msg = "Unable to parse accident date from '%s' at line '%d', column '%d'!";
        int index = columnIndex + (config.hasRowHeader()? 2 : 1);
        msg = String.format(msg, cell, lineIndex+1, index);
        return new IOException(msg, ex);
    }
    
    private IOException developmentException(Exception ex, String cell) {
        String msg = "Unable to parse development date from '%s' at line '%d', column '%d'!";
        int index = columnIndex + (config.hasRowHeader()? 2 : 1);
        msg = String.format(msg, cell, lineIndex+1, index);
        return new IOException(msg, ex);
    }
    
    private double getDouble(String str) {
        str = str.replace(config.getDecimalSep(), '.');
        return Double.parseDouble(str);
    }
    
    private IOException valueException(Exception ex, String cell) {
        String msg = "Unable to parse value from '%s' at line '%d', column '%d'!";
        int index = columnIndex + (config.hasRowHeader()? 2 : 1);
        msg = String.format(msg, cell, lineIndex+1, index);
        return new IOException(msg, ex);
    }
    
    private IOException duplicateEntryException(int firstIndex, DataEntry entry) {
        if(config.hasColumnHeader())
            firstIndex++;
        String msg = "Record in line '%d' is a duplicate of line '%d'!";
        msg = String.format(msg, lineIndex+1, firstIndex);
        return new IOException(msg);
    }
}
