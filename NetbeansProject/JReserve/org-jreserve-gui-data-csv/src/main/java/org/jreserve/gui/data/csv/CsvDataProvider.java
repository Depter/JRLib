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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.spi.AbstractDataProvider;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.jreserve.gui.data.spi.DataProvider;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CsvDataProvider extends AbstractDataProvider {
    
    private final static String CSV_EXTENSION = "csv";
    private final static String CELL_SEPARATOR = ",";
    private final static int ACCIDENT_CELL = 0;
    private final static int DEVELOPMENT_CELL = 1;
    private final static int VALUE_CELL = 2;
    private final static Logger logger = Logger.getLogger(CsvDataProvider.class.getName());
    
    private final Writer writer;
    private final Loader loader;
    private FileObject csvFile;
    
    public CsvDataProvider(DataProvider.Factory factory) {
        super(factory);
        writer = new Writer();
        loader = new Loader();
    }
    
    @Override
    public synchronized void delete() throws IOException {
        if(csvFile != null)
            deleteCsvFile();
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
    public synchronized void rename(String newName) throws IOException {
        if(csvFile != null)
            renameCsvFile(newName);
    }
    
    private void renameCsvFile(String newName) throws IOException {
        FileLock lock = null;
        try {
            lock = csvFile.lock();
            String oldPath = csvFile.getPath();
            csvFile.rename(lock, newName, CSV_EXTENSION);
            logger.info(String.format("Renamed CsvFile: '%s' -> '%s'", oldPath, csvFile.getPath()));
        } catch (Exception ex) {
            String msg = "Unable to rename CsvFile: " + csvFile.getPath();
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        } finally {
            if(lock != null)
                lock.releaseLock();
        }
    }
    
    @Override
    public synchronized void move(DataCategory newParent) throws Exception {
        if(csvFile != null)
            moveCsvFile(newParent.getFile());
    }
    
    private void moveCsvFile(FileObject newParent) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        
        try {
            String oldPath = csvFile.getPath();
            File dest = FileUtil.toFile(newParent);
            File source = FileUtil.toFile(csvFile);
            File target = new File(dest, source.getName());
            
            is = new FileInputStream(source);
            os = new FileOutputStream(target);
            
            byte[] buffer = new byte[1024];
            int length;
            while((length = is.read(buffer)) > 0)
                os.write(buffer, 0, length);
            
            os.close();
            os = null;
            is.close();
            is = null;
            
            source.delete();
            csvFile = FileUtil.toFileObject(target);
            logger.info(String.format("Moved CsvFile: '%s' -> '%s'", oldPath, csvFile.getPath()));
        } catch (Exception ex) {
            if(os != null)
                os.close();
            if(is != null)
                is.close();
            
            String msg = "Unable to move CsvFile: " + csvFile.getPath();
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }
    
    @Override
    protected Set<DataEntry> loadEntries() throws Exception {
        try {
            initFile();
            return loader.loadEntries();
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
            File file = createFile(parent, dsFile.getName());
            csvFile = FileUtil.toFileObject(file);
        }
    }
    
    private File createFile(FileObject parent, String name) throws IOException {
        File file = new File(FileUtil.toFile(parent), name+"."+CSV_EXTENSION);
        if(!file.exists() && !file.createNewFile())
            throw new IOException("Unable to create file: "+file.getAbsolutePath());
        return file;
    }

    @Override
    protected void saveEntries(Set<DataEntry> entries) throws Exception {
        try {
            initFile();
            writer.write(entries);
        } catch (Exception ex) {
            String msg = String.format("Unable to write data for data source '%s'!", getDataSource().getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
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
