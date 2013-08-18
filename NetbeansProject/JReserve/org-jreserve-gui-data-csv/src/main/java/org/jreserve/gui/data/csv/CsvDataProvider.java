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
package org.jreserve.gui.data.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.data.spi.AbstractFileDataProvider;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.jreserve.gui.data.spi.DataProvider;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CsvDataProvider extends AbstractFileDataProvider {
    
    private final static String CSV_EXTENSION = "csv";
    private final static String CELL_SEPARATOR = ",";
    private final static int ACCIDENT_CELL = 0;
    private final static int DEVELOPMENT_CELL = 1;
    private final static int VALUE_CELL = 2;
    private final static Logger logger = Logger.getLogger(CsvDataProvider.class.getName());
    
    private Writer writer;
    private Loader loader;
    
    public CsvDataProvider(DataProvider.Factory factory) {
        super(factory);
    }

    @Override
    protected String getFileExtension() {
        return CSV_EXTENSION;
    }

    @Override
    protected Loader getLoader() {
        if(loader == null)
            loader = new CsvLoader();
        return loader;
    }

    @Override
    protected Writer getWriter() {
        if(writer == null)
            writer = new CsvWriter();
        return writer;
    }
    
    private class CsvLoader implements AbstractFileDataProvider.Loader {
        
        private FileLock lock;
        private BufferedReader reader;
        private int lineNumber = 0;
        
        @Override
        public Set<DataEntry> loadEntries(FileObject file) throws IOException {
            try {
                init(file);
                return readEntries();
            } finally {
                close(file);
            }
        }
        
        private void init(FileObject file) throws IOException {
            lock = file.lock();
            InputStream is = file.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
        }
        
        private Set<DataEntry> readEntries() throws IOException {
            Set<DataEntry> entries = new HashSet<DataEntry>();
            String line;
            while((line = reader.readLine()) != null) {
                lineNumber++;
                entries.add(readEntry(line));
            }
            return entries;
        }
        
        private DataEntry readEntry(String line) throws IOException {
            try {
                String[] cells = line.split(CELL_SEPARATOR);
                MonthDate accident = toDate(cells[ACCIDENT_CELL]);
                MonthDate development = toDate(cells[DEVELOPMENT_CELL]);
                double value = Double.valueOf(cells[VALUE_CELL]);
                return new DataEntry(accident, development, value);
            } catch (Exception ex) {
                String msg = String.format("Unable to parse line %d", lineNumber);
                throw new IOException(msg, ex);
            }
        }
        
        private MonthDate toDate(String cell) {
            int index = cell.indexOf('-');
            int year = Integer.parseInt(cell.substring(0, index));
            int month = Integer.parseInt(cell.substring(index+1));
            return new MonthDate(year, month);
        }
        
        private void close(FileObject file) {
            lineNumber = 0;
            if(reader != null) {
                try{reader.close();} catch (IOException ex) {
                    logger.log(Level.WARNING, "Unable to close InputStream for DataSource: "+file.getPath(), ex);
                }
            }
            if(lock != null) {
                lock.releaseLock();
                lock = null;
            }
        }
    }
    
    private class CsvWriter implements AbstractFileDataProvider.Writer {
        
        private FileLock lock;
        private BufferedWriter writer;
        private StringBuffer lineBuffer;
        
        @Override
        public void writeEntries(FileObject file, Set<DataEntry> entries) throws IOException {
            try {
                init(file);
                writeEntries(entries);
            } finally {
                close(file);
            }
        }
        
        private void init(FileObject file) throws IOException {
            lock = file.lock();
            OutputStream os = file.getOutputStream(lock);
            writer = new BufferedWriter(new OutputStreamWriter(os));
            lineBuffer = new StringBuffer();
        }
        
        private void writeEntries(Set<DataEntry> entries) throws IOException {
            boolean isFirst = true;
            for(DataEntry entry : entries) {
                if(isFirst) {
                    isFirst = false;
                } else {
                    lineBuffer.setLength(0);
                    writer.newLine();
                }
                writeEntry(entry);
            }
        }
        
        private void writeEntry(DataEntry entry) throws IOException {
            appendDate(entry.getAccidentDate());
            lineBuffer.append(CELL_SEPARATOR);
            appendDate(entry.getDevelopmentDate());
            lineBuffer.append(CELL_SEPARATOR);
            lineBuffer.append(Double.toString(entry.getValue()));
            writer.write(lineBuffer.toString());
        }
        
        private void appendDate(MonthDate date) {
            int month = date.getMonth();
            lineBuffer.append(date.getYear());
            lineBuffer.append('-');
            if(month < 10)
                lineBuffer.append('0');
            lineBuffer.append(month);
        }
        
        private void close(FileObject file) {
            lineBuffer = null;
            
            if(writer != null) {
                try{writer.close();} catch (IOException ex) {
                    logger.log(Level.WARNING, "Unable to close OutputStream for DataSource: "+file.getPath(), ex);
                }
            }
            
            if(lock != null) {
                lock.releaseLock();
                lock = null;
            }
        }
    }
}
