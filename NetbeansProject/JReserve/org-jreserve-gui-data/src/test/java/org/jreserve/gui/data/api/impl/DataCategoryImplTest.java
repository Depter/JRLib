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

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataCategoryImplTest {

    private FileSystem fileSys;
    private DataCategoryImpl root;
    private DataCategoryImpl child1;
    private DataCategoryImpl child2;
    private DataCategoryImpl child1_1;
    
    public DataCategoryImplTest() {
    }
    
    @Before
    public void setUp() throws IOException {
        fileSys = FileUtil.createMemoryFileSystem();
        FileObject sysRoot = fileSys.getRoot();
        
        FileObject data = sysRoot.createFolder("Data");
        root = new DataCategoryImpl(null, data, null);
        
        FileObject folder = data.createFolder("child2");
        child2 = new DataCategoryImpl(null, folder, root);
        
        folder = data.createFolder("child1");
        child1 = new DataCategoryImpl(null, folder, root);
        folder.createData("source_1", "jds");
        folder.createData("source_2", "jds");
        
        folder = folder.createFolder("child1");
        child1_1 = new DataCategoryImpl(null, folder, child1);
    }

    @Test
    public void testGetDataItem() {
        assertEquals(child1, root.getDataItem("child1"));
        assertEquals(child2, root.getDataItem("child2"));
        assertEquals(child1_1, root.getDataItem("child1/child1"));
        
        assertNotNull(root.getDataItem("child1/source_1"));
        assertNotNull(root.getDataItem("child1/source_2"));
        assertNull(root.getDataItem("child2/source_2"));
    }

    @Test
    public void testGetChildCategories() {
        Object[] found = root.getChildCategories().toArray();
        assertArrayEquals(new Object[]{child1, child2}, found);
        
        found = child1.getChildCategories().toArray();
        assertArrayEquals(new Object[]{child1_1}, found);
        
        assertTrue(child2.getChildCategories().isEmpty());
    }

    @Test
    public void testGetDataSources() {
        assertEquals(0, root.getDataSources().size());
        assertEquals(2, child1.getDataSources().size());
        assertEquals(0, child2.getDataSources().size());
        assertEquals(0, child1_1.getDataSources().size());
    }

    @Test
    public void testToString() {
        assertEquals("DataCategory [Data]", root.toString());
        assertEquals("DataCategory [Data/child1]", child1.toString());
        assertEquals("DataCategory [Data/child2]", child2.toString());
        assertEquals("DataCategory [Data/child1/child1]", child1_1.toString());
    }
}