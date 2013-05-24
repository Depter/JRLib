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

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CsvWriterTest {
	
    CsvWriter writer
    
    @Before
    void setUp() {
        writer = new CsvWriter()
    }
    
    @Test
    void test() {
        double[][] data = [
          [1.5, 2.5, 3.5],
          [4.5, 5.5],
          [6.5]
        ];
        
        StringWriter sw = new StringWriter()
        writer.setDecimalSeparator('.')
        writer.setColumnSeparator(';')
        writer.setRowNames(1997..1999)
        writer.setColumnNames(1..3)
        writer.setQuotHeader(true)
        writer.setQuotValues(false)
        writer.write(sw, data)
        String found = sw.toString()
        
        String lineSep = System.getProperty("line.separator")
        String expected = 
            ";\"1\";\"2\";\"3\""+lineSep+
            "\"1997\";1.5;2.5;3.5"+lineSep+
            "\"1998\";4.5;5.5"+lineSep+
            "\"1999\";6.5"
        assertEquals(expected, found)
    }
}

