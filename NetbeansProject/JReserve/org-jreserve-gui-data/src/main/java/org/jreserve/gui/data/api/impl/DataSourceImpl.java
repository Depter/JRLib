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
package org.jreserve.gui.data.api.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.gui.data.api.SaveType;
import org.jreserve.gui.data.spi.DataProvider;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataSourceImpl extends AbstractDataItem implements DataSource {
    
    private final static Logger logger = Logger.getLogger(DataSourceImpl.class.getName());
    final static String FILE_EXT = "jds";
    
    static boolean isSourceFile(FileObject file) {
        return FILE_EXT.equalsIgnoreCase(file.getExt());
    }
    
    private DataProvider dataProvider = EmptyDataProvider.INSTANCE;
    private DataType dataType = DataType.VECTOR;
    
    DataSourceImpl(FileObject file, DataCategoryImpl parent) {
        super(parent.getDataManager(), file, parent);
    }
    
    void setDataType(DataType dt) {
        this.dataType = dt;
    }
    
    @Override
    public DataType getDataType() {
        return dataType;
    }
    
    void setProvider(DataProvider provider) {
        provider.setDataSource(this);
        this.dataProvider = provider;
    }

    @Override
    public DataProvider getDataProvider() {
        return dataProvider;
    }
    
    @Override
    void rename(String name) throws IOException {
        try {
            dataProvider.rename(name);
        } catch (Exception ex) {
            throw new IOException("Unable to rename DataProvider for: "+getPath(), ex);
        }
        super.rename(name);
    }
    
    @Override
    void delete() throws IOException {
        try {
            dataProvider.delete();
        } catch (Exception ex) {
            throw new IOException("Unable to delete DataProvider for: "+getPath(), ex);
        } finally {
            dataProvider = EmptyDataProvider.FACTORY.createProvider(Collections.EMPTY_MAP);
            super.delete();
        }
    }
    
    @Override
    synchronized void move(DataCategoryImpl newParent) throws IOException {
        String oldPath = getPath();
        try {
            dataProvider.move(newParent);
        } catch (Exception ex) {
            throw new IOException("Unable to move DataProvider!", ex);
        }
        moveFile(newParent);
        super.move(newParent);
        logger.info(String.format("Moved DataItem: %s -> %s", oldPath, getPath()));
    }
    
    private void moveFile(DataCategoryImpl newParent) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        
        try {
            File dest = FileUtil.toFile(newParent.file);
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
        } catch (Exception ex) {
            if(os != null)
                os.close();
            if(is != null)
                is.close();
            
            String msg = String.format("Unable to move file: "+file.getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }
    
    @Override
    public String toString() {
        return String.format("DataSource [%s]", getPath());
    }    

    @Override
    public synchronized List<DataEntry> getEntries(DataEntryFilter filter) throws Exception {
        return dataProvider.getEntries(filter);
    }
    
    @Override
    public synchronized void addEntries(Set<DataEntry> entries, SaveType saveType) throws Exception {
        dataProvider.addEntries(entries, saveType);
    }
    
    @Override
    public synchronized void deleteEntries(Set<DataEntry> entries) throws Exception {
        dataProvider.deleteEntries(entries);
    }
}
