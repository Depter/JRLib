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

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractFileDataProvider extends AbstractDataProvider {

    private final static Logger logger = Logger.getLogger(AbstractFileDataProvider.class.getName());
    private FileObject secondary;
    
    protected AbstractFileDataProvider(FileObject primaryFile, String extension) {
        FileObject parent = primaryFile.getParent();
        secondary = parent.getFileObject(primaryFile.getName(), extension);
        if(secondary == null) {
            String msg = "Secondary file '%s/'%s.%s does not exists!";
            msg = String.format(msg, parent.getPath(), primaryFile.getName(), extension);
            logger.log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    
    protected AbstractFileDataProvider(FileObject secondaryFile) {
        if(secondaryFile == null)
            throw new NullPointerException("Seconday file is null!");
        this.secondary = secondaryFile;
    }
    
    @Override
    public Set<FileObject> getSecondryFiles(FileObject primary) {
        Set<FileObject> entries = new LinkedHashSet<FileObject>();
        
        for(FileObject child : primary.getParent().getChildren())
            if(isSecondaryFile(primary, child))
                entries.add(child);
        
        return entries;
    }
    
    private boolean isSecondaryFile(FileObject primary, FileObject fo) {
        return fo.isData() &&
               fo.getName().equals(primary.getName()) &&
               !fo.getExt().equals(primary.getExt());
    }
        
    @Override
    protected Set<DataEntry> loadEntries() throws Exception {
        if(secondary == null) {
            String msg = "DataFile not initialized!";
            logger.log(Level.SEVERE, msg);
            throw new IllegalStateException(msg);
        }
        return getLoader().loadEntries(secondary);
    }
    
    protected abstract Loader getLoader();

    @Override
    protected void saveEntries(Set<DataEntry> entries) throws Exception {
        if(secondary == null) {
            String msg = "DataFile not initialized!";
            logger.log(Level.SEVERE, msg);
            throw new IllegalStateException(msg);
        }
        getWriter().writeEntries(secondary, entries);
    }
    
    protected abstract Writer getWriter();
    
    public static interface Loader {
        public Set<DataEntry> loadEntries(FileObject file) throws IOException;
    }
    
    public static interface Writer {
        public void writeEntries(FileObject file, Set<DataEntry> entries) throws IOException;
    }
}
