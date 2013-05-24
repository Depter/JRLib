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

import java.text.DecimalFormatSymbols

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CsvWriter {
    private final static char DECIMAL_SEPARATOR = '.'

    char decimalSeparator = '\u0000'
    char columnSeparator = '\t'
    List rowNames = null
    List columnNames = null
    boolean quotHeader = false
    boolean quotValues = false
    
    CsvWriter() {
        initNullValues()
    }
    
    private void initNullValues() {
        if(decimalSeparator == '\u0000') {
            decimalSeparator = new DecimalFormatSymbols().getDecimalSeparator()
        }
    }
    
    CsvWriter(Map map) {
        initValues(map)
    }
    
    private void initValues(Map map) {
        map.each {key, value ->
            switch(key?.toLowerCase()) {
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
                case "columnnames":
                case "column names":
                case "column-names":
                    setColumnNames(value)
                    break
                case "rownames":
                case "row names":
                case "row-names":
                    setRowNames(value)
                    break
                case "quotheader":
                case "quot header":
                case "quot-header":
                    setQuotHeader(value)
                    break
                case "quotvalues":
                case "quot values":
                case "quot-values":
                    setQuotHeader(value)
                    break
                default:
                    throw new IllegalArgumentException("Unknow parameter: '${key}'!")
                    break
            }
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
    
    void write(String path, double[][] values) {
        write(new File(path), values)
    }
    
    void write(File file, double[][] values) {
        file.withWriter {write(it, values)}
    }
    
    void write(Writer writer, double[][] values) {
        writeColumnHeader(writer)
        values.eachWithIndex { it, i ->
            writeRowHeader(writer, i)
            writeRow(writer, it)
        }
        writer.flush()
    }
    
    private void writeColumnHeader(Writer writer) {
        if(columnNames) {
            if(rowNames)
                writer.print(columnSeparator)
            
            columnNames.eachWithIndex { it, i -> 
                String cell = quote(it, quotHeader)
                writer.print(i==0? cell : ""+columnSeparator + cell)
            }
            writer.println()
        }
    }
    
    private String quote(def value, boolean quote) {
        if(value)
            quote? "\""+value+"\"" : value;
        else
            value
    }
    
    private void writeRowHeader(Writer writer, int row) {
        if(row > 0)
            writer.println()
            
        if(rowNames && row < rowNames.size()) {
            String header = quote(rowNames.get(row), quotHeader)
            writer.write(header + columnSeparator)
        }
    }
    
    private void writeRow(Writer writer, double[] values) {
        int size = values.length
        for(int i=0; i<size; i++) {
            if(i > 0) writer.write(columnSeparator)
            String str = escapeValue(values[i])
            writer.write(str)
        }
    }
    
    private String escapeValue(double value) {
        String str = ""+value
        if(DECIMAL_SEPARATOR != decimalSeparator)
            str = str.replace(DECIMAL_SEPARATOR, decimalSeparator)
        quote(str, quotValues)
    }
    
}

