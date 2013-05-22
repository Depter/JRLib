package org.jreserve.grscript.input

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CsvWriterBuilder {
    CsvWriter writer;
    
    CsvWriterBuilder() {
        writer = new CsvWriter()
    }
    
    void decimalSeparator(String separator) {
        writer.decimalSeparator = getChar(separator)
    }
    
    private char getChar(String str) {
        if(str.size() == 1)
            return str.charAt(0)
        throw new IllegalArgumentException("Input must be one character! Found ${str}")
    }

    void columnSeparator(String separator) {
        writer.columnSeparator = getChar(separator)
    }
    
    void rowNames(List rowNames) {
        writer.rowNames = rowNames
    }
    
    void columnNames(List columnNames) {
        writer.columnNames = columnNames
    }
    
    void quotHeader(boolean quote) {
        writer.quotHeader = quote
    }
    
    void quotValues(boolean quote) {
        writer.quotValues = quote
    }	
}

