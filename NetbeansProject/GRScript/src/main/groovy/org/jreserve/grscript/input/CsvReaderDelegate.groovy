package org.jreserve.grscript.input

import org.jreserve.grscript.FunctionProvider

/**
 *
 * @author Peter Decsi
 */
class CsvReaderDelegate implements FunctionProvider {
    
    @Override
    void initFunctions(ExpandoMetaClass emc) {
        emc.readCsv = this.&readCsv
    }
    
    double[][] readCsv(String path) {
        return new CsvReader().read(path)
    }
    
    double[][] readCsv(String path, Map settings) {
        return new CsvReader(settings).read(path)
    }
    
    double[][] readCsv(File file) {
        return new CsvReader().read(file)
    }
    
    double[][] readCsv(File file, Map settings) {
        return new CsvReader(settings).read(file)
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
}

