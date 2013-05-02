package org.jreserve.grscript

import org.jreserve.grscript.FunctionProvider
import org.jreserve.grscript.TestConfig
import org.jreserve.grscript.input.CsvReader

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TestDataDelegate implements FunctionProvider {
    
    private CsvReader reader
    
    TestDataDelegate() {
        reader = new CsvReader()
        reader.setColumnSeparator(',')
    }
    
    @Override
    void initFunctions(Script script, ExpandoMetaClass emc) {
        emc.apcPaid = this.&apcPaid
    }
    
    def apcPaid() {
        return load(TestConfig.APC_PAID)
    }
    
    private double[][] load(String name) {
        String path = TestConfig.getPath(name)
        return reader.read(path)
    }
}

