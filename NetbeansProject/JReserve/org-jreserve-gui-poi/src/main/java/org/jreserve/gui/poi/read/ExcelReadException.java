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

package org.jreserve.gui.poi.read;

import java.io.IOException;
import org.apache.poi.ss.util.CellReference;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExcelReadException extends IOException {
    
    private final CellReference reference;
    
    public ExcelReadException(CellReference reference) {
        this.reference = reference;
    }

    public ExcelReadException(CellReference reference, String message) {
        super(message);
        this.reference = reference;
    }

    public ExcelReadException(CellReference reference, String message, Throwable cause) {
        super(message, cause);
        this.reference = reference;
    }

    public ExcelReadException(CellReference reference, Throwable cause) {
        super(cause);
        this.reference = reference;
    }
    
    public CellReference getReference() {
        return reference;
    }
}
