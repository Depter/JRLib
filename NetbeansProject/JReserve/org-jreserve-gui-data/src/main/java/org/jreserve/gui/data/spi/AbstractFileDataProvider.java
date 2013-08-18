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

package org.jreserve.gui.data.spi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractFileDataProvider extends AbstractDataProvider {
    private final static Logger logger = Logger.getLogger(AbstractFileDataProvider.class.getName());

    private FileObject file;

    protected AbstractFileDataProvider(DataProvider.Factory factory) {
        super(factory);
    }
    
    
    @Override
    public synchronized void delete() throws IOException {
        if(file != null)
            deleteFile();
    }
    
    private void deleteFile() throws IOException {
        try {
            file.delete();
            logger.log(Level.FINE, "Deleted file: {0}", file.getPath());
            file = null;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to delete file: "+file.getPath(), ex);
            throw ex;
        }
    }

    @Override
    public synchronized void rename(String newName) throws IOException {
        if(file != null)
            renameFile(newName);
    }
    
    private void renameFile(String newName) throws IOException {
        FileLock lock = null;
        try {
            lock = file.lock();
            String oldPath = file.getPath();
            file.rename(lock, newName, getFileExtension());
            logger.info(String.format("Renamed File: '%s' -> '%s'", oldPath, file.getPath()));
        } catch (Exception ex) {
            String msg = "Unable to rename File: " + file.getPath();
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        } finally {
            if(lock != null)
                lock.releaseLock();
        }
    }
    
    protected abstract String getFileExtension();

    @Override
    public synchronized void move(DataCategory newParent) throws Exception {
        if(file != null)
            moveFile(newParent.getFile());
    }
    
    private void moveFile(FileObject newParent) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        
        try {
            String oldPath = file.getPath();
            File dest = FileUtil.toFile(newParent);
            File source = FileUtil.toFile(file);
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
            file = FileUtil.toFileObject(target);
            logger.info(String.format("Moved File: '%s' -> '%s'", oldPath, file.getPath()));
        } catch (Exception ex) {
            if(os != null)
                os.close();
            if(is != null)
                is.close();
            
            String msg = "Unable to move File: " + file.getPath();
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }
    
    @Override
    protected Set<DataEntry> loadEntries() throws Exception {
        try {
            initFile();
            return getLoader().loadEntries(file);
        } catch (Exception ex) {
            String msg = String.format("Unable to load data for data source '%s'!", getDataSource().getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }
    
    private void initFile() throws IOException {
        if(file == null) {
            FileObject dsFile = getDataSource().getFile();
            FileObject parent = dsFile.getParent();
            File f = createFile(parent, dsFile.getName());
            file = FileUtil.toFileObject(f);
        }
    }
    
    private File createFile(FileObject parent, String name) throws IOException {
        File f = new File(FileUtil.toFile(parent), name+"."+getFileExtension());
        if(!f.exists() && !f.createNewFile())
            throw new IOException("Unable to create file: "+f.getAbsolutePath());
        return f;
    }
    
    protected abstract Loader getLoader();

    @Override
    protected void saveEntries(Set<DataEntry> entries) throws Exception {
        try {
            initFile();
            getWriter().writeEntries(file, entries);
        } catch (Exception ex) {
            String msg = String.format("Unable to write data for data source '%s'!", getDataSource().getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }
    
    protected abstract Writer getWriter();
    
    public static interface Loader {
        Set<DataEntry> loadEntries(FileObject file) throws IOException;
    }
    
    public static interface Writer {
        void writeEntries(FileObject file, Set<DataEntry> entries) throws IOException;
    }
}
