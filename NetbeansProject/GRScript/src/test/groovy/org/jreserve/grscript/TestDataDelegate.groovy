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
        emc.apcIncurred = this.&apcIncurred
        emc.apcNoC = this.&apcNoC
        emc.apcExposure = this.&apcExposure
    }
    
    def apcPaid() {
        load(TestConfig.APC_PAID)
    }
    
    private double[][] load(String name) {
        String path = TestConfig.getPath(name)
        reader.read(path)
    }
    
    def apcIncurred() {
        load(TestConfig.APC_INCURRED)
    }
    
    def apcNoC() {
        load(TestConfig.APC_NOC)
    }
    
    def apcExposure() {
        double[][] exposure = load(TestConfig.APC_EXPOSURE)
        exposure[0]
    }
}

