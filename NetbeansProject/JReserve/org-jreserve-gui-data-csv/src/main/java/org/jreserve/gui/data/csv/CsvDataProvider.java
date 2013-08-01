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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.data.api.DataType;
import org.jreserve.gui.data.spi.AbstractDataProvider;
import org.jreserve.gui.data.spi.DataEntry;
import org.jreserve.gui.data.spi.DataProviderFactoryType;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CsvDataProvider extends AbstractDataProvider {
    
    private final static String CSV_EXTENSION = "csv";
    private final static String CELL_SEPARATOR = ",";
    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final static int ACCIDENT_CELL = 0;
    private final static int DEVELOPMENT_CELL = 1;
    private final static int VALUE_CELL = 2;
    private final static Logger logger = Logger.getLogger(CsvDataProvider.class.getName());
    
    private final Writer writer;
    private final Loader loader;
    private FileObject csvFile;
    
    public CsvDataProvider(DataType dataType) {
        super(dataType);
        writer = new Writer();
        loader = new Loader();
    }
    
    @Override
    public void delete() throws IOException {
        synchronized(lock) {
            if(csvFile != null)
                deleteCsvFile();
        }
    }
    
    private void deleteCsvFile() throws IOException {
        try {
            csvFile.delete();
            logger.log(Level.FINE, "Deleted CSV file: {0}", csvFile.getPath());
            csvFile = null;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to delete CSV file: "+csvFile.getPath(), ex);
            throw ex;
        }
    }
    
    @Override
    public void rename(String newName) throws IOException {
        synchronized(lock) {
            if(csvFile != null)
                renameCsvFile(newName);
        }
    }
    
    private void renameCsvFile(String newName) throws IOException {
        try {
            FileLock fileLock = csvFile.lock();
            String oldPath = csvFile.getPath();
            csvFile.rename(fileLock, newName, csvFile.getExt());
            logger.info(String.format("Renamed CsvFile: '%s' -> '%s'", oldPath, csvFile.getPath()));
        } catch (Exception ex) {
            String msg = "Unable to rename CsvFile: " + csvFile.getPath();
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }
    
    @Override
    protected Set<DataEntry> loadEntries() throws Exception {
        try {
            synchronized(lock) {
                initFile();
                return loader.loadEntries();
            }
        } catch (Exception ex) {
            String msg = String.format("Unable to load data for data source '%s'!", getDataSource().getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }
    
    private void initFile() throws IOException {
        if(csvFile == null) {
            FileObject dsFile = getDataSource().getFile();
            FileObject parent = dsFile.getParent();
            csvFile = parent.createData(dsFile.getName(), CSV_EXTENSION);
        }
    }

    @Override
    protected void saveEntries(Set<DataEntry> entries) throws Exception {
        try {
            synchronized(lock) {
                initFile();
                writer.write(entries);
            }
        } catch (Exception ex) {
            String msg = String.format("Unable to write data for data source '%s'!", getDataSource().getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }

    @Override
    public DataProviderFactoryType getFactoryType() {
        return DataProviderFactoryType.INSTANCE;
    }

    @Override
    public String getInstancePath() {
        return CsvDataProvider.class.getName();
    }
    
    private class Loader {
        
        private FileLock lock;
        private BufferedReader reader;
        private int lineNumber = 0;
        
        Set<DataEntry> loadEntries() throws IOException {
            try {
                init();
                return readEntries();
            } finally {
                close();
            }
        }
        
        private void init() throws IOException {
            lock = csvFile.lock();
            InputStream is = csvFile.getInputStream();
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
                Date accident = DATE_FORMAT.parse(cells[ACCIDENT_CELL]);
                Date development = DATE_FORMAT.parse(cells[DEVELOPMENT_CELL]);
                double value = Double.valueOf(cells[VALUE_CELL]);
                return new DataEntry(accident, development, value);
            } catch (Exception ex) {
                String msg = String.format("Unable to parse line %d", lineNumber);
                throw new IOException(msg, ex);
            }
        }
        
        private void close() {
            lineNumber = 0;
            if(reader != null) {
                try{reader.close();} catch (IOException ex) {
                    logger.log(Level.WARNING, "Unable to close InputStream for DataSource: "+csvFile.getPath(), ex);
                }
            }
            if(lock != null) {
                lock.releaseLock();
                lock = null;
            }
        }
    }
    
    private class Writer {
        
        private FileLock lock;
        private BufferedWriter writer;
        private StringBuffer lineBuffer;
        
        void write(Set<DataEntry> entries) throws IOException {
            try {
                init();
                writeEntries(entries);
            } finally {
                close();
            }
        }
        
        private void init() throws IOException {
            lock = csvFile.lock();
            OutputStream os = csvFile.getOutputStream(lock);
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
            lineBuffer.append(DATE_FORMAT.format(entry.getAccidentDate()))
                .append(CELL_SEPARATOR)
                .append(DATE_FORMAT.format(entry.getDevelopmentDate()))
                .append(CELL_SEPARATOR)
                .append(Double.toString(entry.getValue()));
            writer.write(lineBuffer.toString());
        }
        
        private void close() {
            lineBuffer = null;
            
            if(writer != null) {
                try{writer.close();} catch (IOException ex) {
                    logger.log(Level.WARNING, "Unable to close OutputStream for DataSource: "+csvFile.getPath(), ex);
                }
            }
            
            if(lock != null) {
                lock.releaseLock();
                lock = null;
            }
        }
    }
}
