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

import org.jreserve.gui.data.api.impl.DataCategoryImpl;
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
public class DataItemImplTest {
    
    private FileSystem fileSys;
    private DataCategoryImpl root;
    private DataCategoryImpl child1;
    private DataCategoryImpl child2;
    private DataCategoryImpl child1_1;
    
    public DataItemImplTest() {
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
        
        folder = folder.createFolder("child1");
        child1_1 = new DataCategoryImpl(null, folder, child1);
    }

    @Test
    public void testGetParent() {
        assertEquals(null, root.getParent());
        assertEquals(root, child1.getParent());
        assertEquals(root, child2.getParent());
        assertEquals(child1, child1_1.getParent());
    }

    @Test
    public void testGetName() {
        assertEquals("Data", root.getName());
        assertEquals("child1", child1.getName());
        assertEquals("child2", child2.getName());
        assertEquals("child1", child1_1.getName());
    }

    @Test
    public void testGetPath() {
        assertEquals("Data", root.getPath());
        assertEquals("Data/child1", child1.getPath());
        assertEquals("Data/child2", child2.getPath());
        assertEquals("Data/child1/child1", child1_1.getPath());
    }
}