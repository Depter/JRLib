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

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
class BinaryWriter implements AbstractFileDataProvider.Writer {
    private final static Logger logger = Logger.getLogger(BinaryWriter.class.getName());
    private final static int BUFFER_SIZE = 1024;
    
    private FileLock lock;
    private ObjectOutputStream writer;

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
        OutputStream os = new FileOutputStream(FileUtil.toFile(file), false);
        writer = new ObjectOutputStream(new BufferedOutputStream(os, BUFFER_SIZE));
    }

    private void writeEntries(Set<DataEntry> entries) throws IOException {
        writer.writeInt(entries.size());
        for (DataEntry entry : entries) {
            writeEntry(entry);
        }
        writer.flush();
    }

    private void writeEntry(DataEntry entry) throws IOException {
        writeDate(entry.getAccidentDate());
        writeDate(entry.getDevelopmentDate());
        writer.writeDouble(entry.getValue());
    }

    private void writeDate(MonthDate date) throws IOException {
        writer.writeInt(date.getYear());
        writer.writeInt(date.getMonth());
    }

    private void close(FileObject file) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Unable to close OutputStream for DataSource: " + file.getPath(), ex);
            }
        }

        if (lock != null) {
            lock.releaseLock();
            lock = null;
        }
    }
}
