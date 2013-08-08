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
package org.jreserve.gui.data.api;

import org.jreserve.jrlib.gui.data.DataType;
import java.io.IOException;
import org.jreserve.gui.data.spi.DataProvider;
import org.netbeans.api.project.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataManager {
    
    public Project getProject();
    
    public DataCategory getCategory(String path);
    
    public DataCategory createDataCategory(DataCategory parent, String name) throws IOException;
    
    public void deleteDataItem(DataItem item) throws IOException;
    
    public DataSource getDataSource(String path);
    
    public DataSource createDataSource(DataCategory parent, String name, DataType dataType, DataProvider dataProvider) throws IOException;
    
    public void renameDataItem(DataItem item, String newName) throws IOException;

    public void moveDataItem(DataCategory target, DataItem item) throws IOException;
}
