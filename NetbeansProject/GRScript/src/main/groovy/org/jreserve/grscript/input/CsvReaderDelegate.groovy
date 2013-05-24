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
class CsvReaderDelegate implements FunctionProvider {
    
    @Override
    void initFunctions(Script script, ExpandoMetaClass emc) {
        emc.readCsv = this.&readCsv
    }
    
    double[][] readCsv(String path) {
        return new CsvReader().read(path)
    }
    
    double[][] readCsv(File file) {
        return new CsvReader().read(file)
    }
    
    double[][] readCsv(Reader reader) {
        return new CsvReader().read(reader)
    }
    
    double[][] readCsv(String path, Map settings) {
        return new CsvReader(settings).read(path)
    }
    
    double[][] readCsv(File file, Map settings) {
        return new CsvReader(settings).read(file)
    }
    
    double[][] readCsv(Reader reader, Map settings) {
        return new CsvReader(settings).read(reader)
    }
    
    double[][] readCsv(String path, Closure cl) {
        return createReader(cl).read(path)
    }
    
    private CsvReader createReader(Closure cl) {
        CsvReaderBuilder builder = new CsvReaderBuilder()
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.reader
    }
    
    double[][] readCsv(File file, Closure cl) {
        return createReader(cl).read(file)
    }
    
    double[][] readCsv(Reader reader, Closure cl) {
        return createReader(cl).read(reader)
    }
}

