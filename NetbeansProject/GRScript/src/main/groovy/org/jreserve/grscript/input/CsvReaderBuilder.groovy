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
package org.jreserve.grscript.input

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CsvReaderBuilder {
	
    CsvReader reader;
    
    CsvReaderBuilder() {
        reader = new CsvReader()
    }
    
    void decimalSeparator(String separator) {
        reader.decimalSeparator = getChar(separator)
    }
    
    private char getChar(String str) {
        if(str.size() == 1)
            return str.charAt(0)
        throw new IllegalArgumentException("Input must be one character! Found ${str}")
    }

    void columnSeparator(String separator) {
        reader.columnSeparator = getChar(separator)
    }
    
    void rowHeader(boolean hasHeader) {
        reader.hasRowHeader = hasHeader
    }
    
    void columnHeader(boolean hasHeader) {
        reader.hasColumnHeader = hasHeader
    }
    
    void quotes(boolean hasQuotes) {
        reader.hasQuotes = hasQuotes
    }
}

