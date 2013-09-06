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
package org.jreserve.gui.data.binary;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.data.spi.AbstractFileDataProvider;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class BinaryLoader implements AbstractFileDataProvider.Loader {
    
    private final static Logger logger = Logger.getLogger(BinaryLoader.class.getName());
    private final static int BUFFER_SIZE = 1024;
    
    
    private FileLock lock;
    private ObjectInputStream reader;
        
    @Override
    public Set<DataEntry> loadEntries(FileObject file) throws IOException {
        try {
            if(FileUtil.toFile(file).length() == 0)
                return Collections.EMPTY_SET;
            init(file);
            return readEntries();
        } finally {
            close(file);
        }
    }
        
    private void init(FileObject file) throws IOException {
        lock = file.lock();
        InputStream is = file.getInputStream();
        reader = new ObjectInputStream(new BufferedInputStream(is, BUFFER_SIZE));
    }
        
    private Set<DataEntry> readEntries() throws IOException {
        int count = getEntryCount();
        Set<DataEntry> entries = new HashSet<DataEntry>();
        for(int i=0; i<count; i++)
            entries.add(readEntry());
        return entries;
    }
        
    private int getEntryCount() {
        try {
            return reader.readInt();
        } catch (IOException ex) {
            return 0;
        }
    }
        
    private DataEntry readEntry() throws IOException {
        MonthDate accident = readMonthDate();
        MonthDate development = readMonthDate();
        double value = reader.readDouble();
        return new DataEntry(accident, development, value);
    }
        
    private MonthDate readMonthDate() throws IOException {
        int year = reader.readInt();
        int month = reader.readInt();
        return new MonthDate(year, month);
    }
        
    private void close(FileObject file) {
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
