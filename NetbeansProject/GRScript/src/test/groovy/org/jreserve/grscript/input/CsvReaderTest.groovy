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
import org.jreserve.grscript.TestConfig

/**
 *
 * @author Peti
 */
class CsvReaderTest {
    
    private final static double[][] APC_PAID = [
        [4426765, 992330 , 88952 , 13240 , 38622 , 26720, 36818, 10750],
        [4388958, 984169 , 60162 , 35004 , 75768 , 23890, 572],
        [5280130, 1239396, 76122 , 110189, 112895, 11751],
        [5445384, 1164234, 171583, 16427 , 6451],
        [5612138, 1837950, 155863, 127146],
        [6593299, 1592418, 74189],
        [6603091, 1659748],
        [7194587],
    ]
    
    private CsvReader reader;

    @Before
    public void setUp() {
        reader = new CsvReader()
    }
    
    @Test
    void testConstructor() {
        reader = new CsvReader([decimal:',', column:';'])
        assertEquals(',' as char, reader.getDecimalSeparator())
        assertEquals(';' as char, reader.getColumnSeparator())
        
        reader = new CsvReader([decimals:',', columns:';'])
        assertEquals(',' as char, reader.getDecimalSeparator())
        assertEquals(';' as char, reader.getColumnSeparator())
        
        reader = new CsvReader([decimalseparator:',', columnseparator:';'])
        assertEquals(',' as char, reader.getDecimalSeparator())
        assertEquals(';' as char, reader.getColumnSeparator())
        
        reader = new CsvReader(decimalSeparator:',', columnSeparator:';')
        assertEquals(',' as char, reader.getDecimalSeparator())
        assertEquals(';' as char, reader.getColumnSeparator())
        
        reader = new CsvReader([rowheader:true, columnheader:true, quote:true])
        assertTrue(reader.hasColumnHeader())
        assertTrue(reader.hasRowHeader())
        assertTrue(reader.hasQuotes())
        
        reader = new CsvReader([rowheaders:true, columnheaders:true, quotes:true])
        assertTrue(reader.hasColumnHeader())
        assertTrue(reader.hasRowHeader())
        assertTrue(reader.hasQuotes())
        
        reader = new CsvReader([hasRowheader:true, hascolumnHeader:true, hasquOte:true])
        assertTrue(reader.hasColumnHeader())
        assertTrue(reader.hasRowHeader())
        assertTrue(reader.hasQuotes())
        
        reader = new CsvReader([HASROWHEADERS:true, HASCOLUMNHEADERS:true, HASQUOTES:true])
        assertTrue(reader.hasColumnHeader())
        assertTrue(reader.hasRowHeader())
        assertTrue(reader.hasQuotes())
    }
    
    @Test(expected=IllegalArgumentException.class)
    void testConstructor_UnknownParameter() {
        reader = new CsvReader(bela:true)
    }
    
    @Test
    void testDefaultValues() {
        char dSep = new java.text.DecimalFormatSymbols().getDecimalSeparator()
        assertEquals(dSep, reader.getDecimalSeparator())
        assertEquals('\t' as char, reader.getColumnSeparator())
        assertFalse(reader.hasColumnHeader())
        assertFalse(reader.hasRowHeader())
        assertFalse(reader.hasQuotes())
    }
    
    @Test
    void testSetDecimalSeparator() {
        char expected = ','
        reader.setDecimalSeparator(expected)
        assertEquals(expected, reader.getDecimalSeparator())
        
        expected = ';'
        reader.setDecimalSeparator(";")
        assertEquals(expected, reader.getDecimalSeparator())
    }
    
    @Test(expected=IllegalArgumentException.class)
    void testSetDecimalSeparator_NullValue() {
        reader.setDecimalSeparator(null)
    }
    
    @Test(expected=IllegalArgumentException.class)
    void testSetDecimalSeparator_NotCharacterString() {
        reader.setDecimalSeparator("aa")
    }
    
    @Test
    void testSetColumnSeparator() {
        char expected = '\t'
        assertEquals(expected, reader.getColumnSeparator())
        
        expected = ','
        reader.setColumnSeparator(expected)
        assertEquals(expected, reader.getColumnSeparator())
        
        expected = ';'
        reader.setColumnSeparator(";")
        assertEquals(expected, reader.getColumnSeparator())
    }
    
    @Test(expected=IllegalArgumentException.class)
    void testSetColumnSeparator_NullValue() {
        reader.setColumnSeparator(null)
    }
    
    @Test(expected=IllegalArgumentException.class)
    void testSetColumnSeparator_NotCharacterString() {
        reader.setColumnSeparator("aa")
    }
    
    @Test
    void testRead() {
        String path = "src/main/resources/org/jreserve/grscript/input/apc_paid.csv"
        reader.setColumnSeparator(",")
        double[][] values = reader.read(path)
        
        int rows = APC_PAID.length
        assertEquals(rows, values.length)
        for(r in 0..<rows) {
            int cells = APC_PAID[r].length
            assertEquals(cells, values[r].length)
            
            for(int c in 0..<cells)
                assertEquals(APC_PAID[r][c], values[r][c], TestConfig.EPSILON)
        }
    }
}
