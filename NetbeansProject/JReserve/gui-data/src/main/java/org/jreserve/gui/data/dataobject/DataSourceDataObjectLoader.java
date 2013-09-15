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
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.FileEntry;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@MIMEResolver.ExtensionRegistration(
    displayName = "Data Source",
    mimeType = DataSourceDataObject.MIME_TYPE,
    extension = "jds",
    position = 1000
)
@DataObject.Registration(
    mimeType = DataSourceDataObject.MIME_TYPE,
    iconBase = "org/jreserve/gui/data/icons/database.png",
    displayName = "DataSourceLoader",
    position = 300
)
public class DataSourceDataObjectLoader extends MultiFileLoader {

    public DataSourceDataObjectLoader() {
        super(DataSourceDataObject.class.getName());
    }
    
    @Override
    protected FileObject findPrimaryFile(FileObject fo) {
        if(fo.isFolder())
            return null;
        if(fo.getExt().equals(DataSourceDataObject.EXTENSION))
            return fo;
        return FileUtil.findBrother(fo, DataSourceDataObject.EXTENSION);
    }

    @Override
    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new DataSourceDataObject(primaryFile, this);
    }

    @Override
    protected MultiDataObject.Entry createPrimaryEntry(MultiDataObject obj, FileObject primaryFile) {
        return new DataSourcePrimaryEntry(obj, primaryFile);
    }

    @Override
    protected MultiDataObject.Entry createSecondaryEntry(MultiDataObject obj, FileObject secondaryFile) {
        return new FileEntry(obj, secondaryFile);
    }
}
