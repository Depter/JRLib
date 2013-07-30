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

import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.api.DataType;
import org.jreserve.gui.data.spi.DataProvider;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataSourceImpl extends AbstractDataItem implements DataSource {
    
    final static String FILE_EXT = "jds";
    
    static boolean isSourceFile(FileObject file) {
        return FILE_EXT.equalsIgnoreCase(file.getExt());
    }
    
    private DataProvider dataProvider;
    
    DataSourceImpl(FileObject file, DataCategoryImpl parent) {
        super(parent.getDataManager(), file, parent);
        dataProvider = DataSourceUtil.load(file);
    }
    
    DataSourceImpl(FileObject file, DataCategoryImpl parent, DataProvider provider) {
        super(parent.getDataManager(), file, parent);
        this.dataProvider = provider;
    }
    
    @Override
    public String toString() {
        return String.format("DataSource [%s]", getPath());
    }    

    @Override
    public DataType getDataType() {
        return dataProvider.getDataType();
    }

    @Override
    public DataProvider getDataProvider() {
        return dataProvider;
    }
}
