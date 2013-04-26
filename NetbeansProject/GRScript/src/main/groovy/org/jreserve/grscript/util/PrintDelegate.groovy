package org.jreserve.grscript.util

import org.jreserve.grscript.FunctionProvider
import java.text.DecimalFormat

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PrintDelegate implements FunctionProvider {
    
    private DecimalFormat df = new DecimalFormat("#.###")
    private Script script
    
    @Override
    void initFunctions(Script script, ExpandoMetaClass emc) {
        this.script = script;
        emc.setNumberFormat = this.&setNumberFormat
        emc.printData = this.&printData
    }
    
    void printData(String title, double[][] data) {
        script.println title
        printData data
    }
    
    void printData(double[][] data) {
        data.each {printData(it)}
    }
    
    void printData(String title, double[] data) {
        script.println title
        printData data
    }
    
    void printData(double[] data) {
        int count = 0;
        data.each {
            String str = Double.isNaN(it)? "NaN" : df.format(it)
            if(count == 0) {
                script.print "\t"+str
            } else {
                script.print ",\t"+str
            }
            count++
        }
        script.println()
    }
    
    void setNumberFormat(String format) {
        df.applyPattern(format)
    }
}

