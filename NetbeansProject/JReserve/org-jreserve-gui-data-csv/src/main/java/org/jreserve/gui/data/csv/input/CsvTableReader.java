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
package org.jreserve.gui.data.csv.input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.data.api.DataType;
import org.jreserve.gui.data.spi.DataEntry;
import org.jreserve.gui.data.spi.MonthDate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CsvTableReader {
    
    private final static int BUFFER_SIZE = 1024;
    private final static Logger logger = Logger.getLogger(CsvTableReader.class.getName());
    
    private final CsvTableImportWizardPanel.ValidationData config;
    private final List<DataEntry> result = new ArrayList<DataEntry>();
    
    private BufferedReader reader;
    private int lineIndex = 0;
    private int columnIndex;
    private SimpleDateFormat sdf;
    private MonthDate.Factory mdf;
    private boolean isTriangle;
    
    CsvTableReader(CsvTableImportWizardPanel.ValidationData config) {
        this.config = config;
        isTriangle = (DataType.TRIANGLE == config.dt);
    }
    
    List<DataEntry> readEntries() throws IOException {
        try {
            openReader();
            this.sdf = new SimpleDateFormat(config.dateFormat);
            this.mdf = new MonthDate.Factory();
            
            String line;
            while((line = reader.readLine()) != null) {
                if(shouldReadLine(line))
                    readLine(line);
                lineIndex++;
            }
            return result;
        } catch (Exception ex) {
            String msg = "Unable to read CSV table from file '%s'!";
            msg = String.format(msg, config.csv.getAbsolutePath());
            logger.log(Level.WARNING, msg, ex);
            throw new IOException(msg, ex);
        } finally {
            closeReader();
        }
    }
    
    private boolean shouldReadLine(String line) {
        if(line.length() == 0)
            return false;
        if(lineIndex == 0)
            return !config.columnHeader;
        return true;
    }
    
    private void openReader() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(config.csv), BUFFER_SIZE);
    }
    
    private void closeReader() {
        if(reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
                String msg = "Unable to close reader for file '%s'!";
                msg = String.format(msg, config.csv.getAbsolutePath());
                logger.log(Level.WARNING, msg, ex);
            }
        }
    }
    
    private void readLine(String line) throws IOException {
        String[] cells = line.split(config.cellSep);
        columnIndex = config.rowHeaders? 0 : 1;
        
        Date accident = null;
        Date development = null;
        double value;
        
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
        msg = String.format(msg, cell, lineIndex+1, columnIndex+1);
        return new IOException(msg, ex);
    }
    
    private IOException developmentException(Exception ex, String cell) {
        String msg = "Unable to parse development date from '%s' at line '%d', column '%d'!";
        msg = String.format(msg, cell, lineIndex+1, columnIndex+1);
        return new IOException(msg, ex);
    }
    
    private double getDouble(String str) {
        str = str.replace(config.decimalSep, '.');
        return Double.parseDouble(str);
    }
    
    private IOException valueException(Exception ex, String cell) {
        String msg = "Unable to parse value from '%s' at line '%d', column '%d'!";
        msg = String.format(msg, cell, lineIndex+1, columnIndex+1);
        return new IOException(msg, ex);
    }
    
    private IOException duplicateEntryException(int firstIndex, DataEntry entry) {
        if(config.columnHeader)
            firstIndex++;
        String msg = "Record in line '%d' is a duplicate of line '%d'!";
        msg = String.format(msg, lineIndex+1, firstIndex);
        return new IOException(msg);
    }
}
