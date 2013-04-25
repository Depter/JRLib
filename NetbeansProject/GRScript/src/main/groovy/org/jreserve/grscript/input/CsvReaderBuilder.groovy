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

