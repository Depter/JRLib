package org.jreserve.grscript.util

import org.jreserve.grscript.FunctionProvider
import java.text.DecimalFormat

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PrintDelegate implements FunctionProvider {
    
    private static DecimalFormat df = new DecimalFormat("#.###")
    
    @Override
    void initFunctions(ExpandoMetaClass emc) {
        emc.printData = this.&printData
        emc.setNumberFormat = this.&setNumberFormat
    }
    
    static void printData(String title, double[][] data) {
        println title
        printData data
    }
    
    static void printData(double[][] data) {
        data.each {
            printData(it)
        }
    }
    
    static void printData(String title, double[] data) {
        println title
        printData data
    }
    
    static void printData(double[] data) {
        int count = 0;
        data.each {
            String str = Double.isNaN(it)? "NaN" : df.format(it)
            if(count == 0) {
                print "\t"+str
            } else {
                print ",\t"+str
            }
            count++
        }
        println ""
    }
    
    static void setNumberFormat(String format) {
        df.applyPattern(format)
    }
}

