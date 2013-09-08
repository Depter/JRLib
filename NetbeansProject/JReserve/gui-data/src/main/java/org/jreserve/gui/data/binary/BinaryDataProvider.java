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
package org.jreserve.gui.data.binary;

import org.jreserve.gui.data.spi.AbstractFileDataProvider;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class BinaryDataProvider extends AbstractFileDataProvider {
    final static String BINARY_EXTENSION = "bde";

    private BinaryLoader loader;
    private BinaryWriter writer;
    
    BinaryDataProvider(FileObject primaryFile) {
        super(primaryFile, BINARY_EXTENSION);
    }

    @Override
    protected Loader getLoader() {
        if(loader == null)
            loader = new BinaryLoader();
        return loader;
    }

    @Override
    protected Writer getWriter() {
        if(writer == null)
            writer = new BinaryWriter();
        return writer;
    }
}
