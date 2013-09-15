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

package org.jreserve.gui.data.dataobject;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.misc.audit.db.AuditDbManager;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.loaders.FileEntry;
import org.openide.loaders.MultiDataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataSourcePrimaryEntry extends FileEntry {

    private final static Logger logger = Logger.getLogger(DataSourcePrimaryEntry.class.getName());
    
    DataSourcePrimaryEntry(MultiDataObject obj, FileObject fo) {
        super(obj, fo);
    }

    @Override
    public FileObject copy(FileObject f, String suffix) throws IOException {
        FileObject fo = getFile();
        String newName = fo.getName() + suffix;
        return copyRename(f, newName, fo.getExt());
    }

    @Override
    public FileObject copyRename(FileObject f, String name, String ext) throws IOException {
        FileObject newFile = getFile().copy(f, name, ext);
        
        Properties props = DataSourceUtil.loadProperties(newFile);
        long auditId = createNewAuditId();
        props.setProperty(DataSourceDataObject.PROP_AUDIT_ID, ""+auditId);
        
        DataSourceUtil.saveProperties(newFile, props);
        return newFile;
    }
    
    private long createNewAuditId() {
        Project p = FileOwnerQuery.getOwner(getFile());
        if(p == null) {
            logger.log(Level.WARNING, "Unable to find project for file: {0}", getFile().getPath());
            return -1L;
        }
        return AuditDbManager.getInstance().getNextObjectId(p);
    }
}
