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
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.openide.filesystems.FileObject;
import org.openide.loaders.FileEntry;
import org.openide.loaders.MultiDataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractFileDataProvider extends AbstractDataProvider {

    private final static Logger logger = Logger.getLogger(AbstractFileDataProvider.class.getName());
    private FileObject secondary;
    
    @Override
    public Set<MultiDataObject.Entry> getSecondaryEntries(MultiDataObject mdo) throws IOException {
        FileObject primary = mdo.getPrimaryFile();
        FileObject parent = primary.getParent();
        secondary = parent.getFileObject(primary.getName(), getExtension());
        if(secondary == null)
            secondary = createSecondaryFile(parent, primary.getName());
        return Collections.singleton((MultiDataObject.Entry)new FileEntry(mdo, secondary));
    }

    protected abstract String getExtension();

    protected FileObject createSecondaryFile(FileObject parent, String name) throws IOException {
        try {
            return parent.createData(name, getExtension());
        } catch (IOException ex) {
            String msg = "Unable to create file %s/%s.%s!";
            msg = String.format(msg, parent.getPath(), name, getExtension());
            logger.log(Level.SEVERE, msg, ex);
            throw ex;
        }
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
