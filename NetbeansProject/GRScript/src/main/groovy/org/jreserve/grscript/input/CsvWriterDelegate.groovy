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

import org.jreserve.grscript.FunctionProvider
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CsvWriterDelegate implements FunctionProvider {
    
    @Override
    void initFunctions(Script script, ExpandoMetaClass emc) {
        emc.writeCsv = this.&writeCsv
    }
    
    void writeCsv(String path, double[][] values) {
        new CsvWriter().write(path, values)
    }
    
    void writeCsv(File file, double[][] value) {
        new CsvWriter().write(file, values)
    }
    
    void writeCsv(Writer writer, double[][] values) {
        new CsvWriter().write(writer, values)
    }
    
    void writeCsv(String path, double[][] values, Map configuration) {
        new CsvWriter(configuration).write(path, values)
    }
    
    void writeCsv(File file, double[][] value, Map configuration) {
        new CsvWriter().write(file, values)
    }
    
    void writeCsv(Writer writer, double[][] values, Map configuration) {
        new CsvWriter(configuration).write(writer, values)
    }
    
    void writeCsv(String path, double[][] values, Closure cl) {
        buildWriter(cl).write(path, values)
    }
    
    private CsvWriter buildWriter(Closure cl) {
        CsvWriterBuilder builder = new CsvWriterBuilder()
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.writer
    }
    
    void writeCsv(File file, double[][] value, Closure cl) {
        buildWriter(cl).write(file, values)
    }
    
    void writeCsv(Writer writer, double[][] values, Closure cl) {
        buildWriter(cl).write(writer, values)
    }
    
    void writeCsv(String path, double[] values) {
        writeCsv(path, toMatrix(values))
    }
    
    double[][] toMatrix(double[] values) {
        if(!values) return null
        int size = values.length
        double[][] result = new double[size][1]
        for(int i=0; i<size; i++)
            result[i][0] = values[i]
        return result
    }
    
    void writeCsv(File file, double[] value) {
        writeCsv(file, toMatrix(values))
    }
    
    void writeCsv(Writer writer, double[] values) {
        writeCsv(writer, toMatrix(values))
    }
    
    void writeCsv(String path, double[] values, Map configuration) {
        writeCsv(path, toMatrix(values), configuration)
    }
    
    void writeCsv(File file, double[] value, Map configuration) {
        writeCsv(file, toMatrix(values), configuration)
    }
    
    void writeCsv(Writer writer, double[] values, Map configuration) {
        writeCsv(writer, toMatrix(values), configuration)
    }
    
    void writeCsv(String path, double[] values, Closure cl) {
        writeCsv(path, toMatrix(values), cl)
    }
    
    void writeCsv(File file, double[] value, Closure cl) {
        writeCsv(file, toMatrix(values), cl)
    }
    
    void writeCsv(Writer writer, double[] values, Closure cl) {
        writeCsv(writer, toMatrix(values), cl)
    }
}

