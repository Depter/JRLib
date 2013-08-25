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

package org.jreserve.gui.misc.audit.binary;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class BinaryIdReader extends AbstractBinaryReader<Long>{

    BinaryIdReader(File file) {
        super(file);
    }

    @Override
    Long read() throws IOException {
        if(file.length() == 0)
            return 0L;
        return super.read();
    }

    @Override
    protected Long readStream() throws IOException {
        return is.readLong();
    }
}
