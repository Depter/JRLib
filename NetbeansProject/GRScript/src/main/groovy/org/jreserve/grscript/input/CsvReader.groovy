package org.jreserve.grscript.input

import java.text.DecimalFormatSymbols
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CsvReader {
    
    private final static char DECIMAL_SEPARATOR = '.'
    
    char decimalSeparator = '\u0000'
    char columnSeparator = '\t'
    boolean hasRowHeader = false
    boolean hasColumnHeader = false
    boolean hasQuotes = false
    
    CsvReader() {
        initNullValues()
    }
    
    CsvReader(Map map) {
        initValues(map)
    }
    
    private void initValues(Map map) {
        map.each {key, value ->
            switch(key.toLowerCase()) {
                case "decimal":
                case "decimals":
                case "decimalseparator":
                    setDecimalSeparator(map[key])
                    break
                case "column":
                case "columns":
                case "columnseparator":
                    setColumnSeparator(map[key])
                    break
                case "rowheader":
                case "rowheaders":
                case "hasrowheader":
                case "hasrowheaders":
                    hasRowHeader = map[key]
                    break
                case "columnheader":
                case "columnheaders":
                case "hascolumnheader":
                case "hascolumnheaders":
                    hasColumnHeader = map[key]
                    break
                case "quote":
                case "quotes":
                case "hasquote":
                case "hasquotes":
                    hasQuotes = map[key]
                    break
                default:
                    throw new IllegalArgumentException("Unknow parameter: '${key}'!")
                    break
            }
        }
    }
    
    private void initNullValues() {
        if(decimalSeparator == '\u0000') {
            decimalSeparator = new DecimalFormatSymbols().getDecimalSeparator()
        }
    }
    
    void setDecimalSeparator(value) {
        def tmp = getChar(value)
        if(tmp) {
            this.decimalSeparator = tmp
        } else {
            throw new IllegalArgumentException("Illegal value for decimal separator: '${value}'")
        }
    }
    
    private def getChar(value) {
        if(value == null) {
            return null
        } 
        switch(value.class) {
            case char:
            case Character:
                return value
            case String:
            case GString:
                if(value.length() == 1)
                    return value.charAt(0)
                else
                    return null
            default:
                return null
        }
    }
    
    void setColumnSeparator(value) {
        def tmp = getChar(value)
        if(tmp) {
            this.columnSeparator = tmp
        } else {
            throw new IllegalArgumentException("Illegal value for column separator: '${value}'")
        }
    }
    
    void setHasColumnHeader(boolean hasColumnHeader) {
        this.hasColumnHeader = hasColumnHeader
    }
    
    boolean hasColumnHeader() {
        return hasColumnHeader
    }
    
    void setHasRowHeader(boolean hasRowHeader) {
        this.hasRowHeader = hasRowHeader
    }
    
    boolean hasRowHeader() {
        return hasRowHeader
    }
    
    void setHasQuotes(boolean hasQuotes) {
        this.hasQuotes = hasQuotes
    }
    
    boolean hasQuotes() {
        return hasQuotes
    }
    
    double[][] read(String path) {
        read(new File(path))
    }
    
    double[][] read(File file) {
        file.withReader {read(it)}
    }
    
    double[][] read(Reader reader) {
        if(!(reader instanceof BufferedReader))
            reader = new BufferedReader(reader)
        
        int row = 0
        def rows = []
        
        String line
        while(line = reader.readLine()) {
            switch(row++) {
                case 0:
                    if(!hasColumnHeader)
                        rows << readLine(line, row)
                    break
                default:
                    rows << readLine(line, row)
            }
        }
        return rowsToDouble(rows)
    }
    
    private double[] readLine(String line, int row) {
        def cells = []
        int cell = 0;
        line.tokenize(columnSeparator).each {
            if(cell == 0) {
                if(!hasRowHeader)
                    cells << readCell(it, row, cell)
            } else {
                cells << readCell(it, row, cell)
            }
            cell++
        }
        return cellsToDouble(cells)
    }
    
    private double readCell(String str, int row, int cell) {
        String value = str.trim()
        value = escapeQuotes(value)
        value = escapeDecimal(value)
        
        if(value.isDouble())
            return value.toDouble()
        throw new IllegalArgumentException("Value '${str}' at line '${row}', column '${cell+1}' can not be parsed to double!")
    }
    
    private String escapeQuotes(String str) {
        if(!hasQuotes)
            return str;
        if(str.startsWith("\""))
            str = str.substring(0)
        if(str.endsWith("\""))
            str = str.subString(0, str.length()-1)
        return str
    }
    
    private String escapeDecimal(String str) {
        str.tr(decimalSeparator, DECIMAL_SEPARATOR)
    }
    
    private double[] cellsToDouble(cells) {
        int size = cells.size()
        double[] result = new double[size]
        for(c in 0..<size)
            result[c] = cells[c].doubleValue()
        return result
    }
    
    private double[][] rowsToDouble(rows) {
        int size = rows.size()
        double[][] result = new double[size][]
        for(r in 0..<size)
            result[r] = rows[r]
        return result
    }	
}

