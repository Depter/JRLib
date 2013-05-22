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

