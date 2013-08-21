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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCsvReader<T, C extends AbstractCsvReader.CsvFormatData> {
    private final static int BUFFER_SIZE = 1024;
    private final static Logger logger = Logger.getLogger(AbstractCsvReader.class.getName());

    protected final C config;
    private BufferedReader reader;
    protected int lineIndex = 0;
    
    protected AbstractCsvReader(C config) {
        this.config = config;
    }
    
    public T read() throws IOException {
        try {
            openReader();
            
            String line;
            while((line = reader.readLine()) != null) {
                if(shouldReadLine(line))
                    readLine(line);
                lineIndex++;
            }
            return getResult();
        } catch (Exception ex) {
            String msg = "Unable to read CSV table from file '%s'!";
            msg = String.format(msg, config.getCsv().getAbsolutePath());
            logger.log(Level.WARNING, msg, ex);
            throw new IOException(msg, ex);
        } finally {
            closeReader();
        }
    }
    
    private void openReader() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(config.getCsv()), BUFFER_SIZE);
    }
    
    private void closeReader() {
        if(reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
                String msg = "Unable to close reader for file '%s'!";
                msg = String.format(msg, config.getCsv().getAbsolutePath());
                logger.log(Level.WARNING, msg, ex);
            }
        }
    }
    
    private boolean shouldReadLine(String line) {
        if(line.length() == 0)
            return false;
        if(lineIndex == 0)
            return !config.hasColumnHeader();
        return true;
    }
    
    private void readLine(String line) throws IOException {
        String[] cells = line.split(config.getCellSep());
        if(config.hasRowHeader()) {
            if(cells.length > 1) {
                cells = Arrays.copyOfRange(cells, 1, cells.length);
            } else {
                cells = new String[0];
            }
        }
        readRow(cells);
    }
    
    protected abstract void readRow(String[] cells) throws IOException;
    
    protected abstract T getResult() throws IOException;
    
    public static class CsvFormatData {
        private File csv;
        private boolean columnHeader;
        private boolean rowHeaders;
        private boolean cellsQuoted;
        private String cellSep;
        private char decimalSep;

        public CsvFormatData setCsvFile(File csv) {
            this.csv = csv;
            return this;
        }

        public CsvFormatData setColumnHeader(boolean columnHeader) {
            this.columnHeader = columnHeader;
            return this;
        }

        public File getCsv() {
            return csv;
        }

        public boolean hasColumnHeader() {
            return columnHeader;
        }

        public boolean hasRowHeader() {
            return rowHeaders;
        }

        public boolean isCellsQuoted() {
            return cellsQuoted;
        }

        public String getCellSep() {
            return cellSep;
        }

        public char getDecimalSep() {
            return decimalSep;
        }

        public CsvFormatData setRowHeaders(boolean rowHeaders) {
            this.rowHeaders = rowHeaders;
            return this;
        }

        public CsvFormatData setCellsQuoted(boolean cellsQuoted) {
            this.cellsQuoted = cellsQuoted;
            return this;
        }

        public CsvFormatData setCellSep(String cellSep) {
            this.cellSep = cellSep;
            return this;
        }

        public CsvFormatData setDecimalSep(char decimalSep) {
            this.decimalSep = decimalSep;
            return this;
        }
    }
    
}
