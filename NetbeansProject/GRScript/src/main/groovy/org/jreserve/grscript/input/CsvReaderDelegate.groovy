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

