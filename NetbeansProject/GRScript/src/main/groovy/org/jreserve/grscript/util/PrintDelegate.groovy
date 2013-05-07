package org.jreserve.grscript.util

import org.jreserve.grscript.FunctionProvider
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PrintDelegate implements FunctionProvider {
    
    public final static String PRINT_DELEGATE = "print.delegate"
    public final static String DECIMAL_FORMAT = "format.decimal"
    public final static String DEFAULT_DECIMAL_FORMAT = "#.##"
    public final static String PERCENTAGE_FORMAT = "format.percentage"
    public final static String DEFAULT_PERCENTAGE_FORMAT = "0.00%"
    
    private DecimalFormat df = new DecimalFormat(DEFAULT_DECIMAL_FORMAT)
    private DecimalFormat pf = new DecimalFormat(DEFAULT_PERCENTAGE_FORMAT)
    Script script
    
    
    @Override
    void initFunctions(Script script, ExpandoMetaClass emc) {
        this.script = script;
        script.setProperty(DECIMAL_FORMAT, DEFAULT_DECIMAL_FORMAT)
        script.setProperty(PERCENTAGE_FORMAT, DEFAULT_PERCENTAGE_FORMAT)
        script.setProperty(PRINT_DELEGATE, this)
        
        emc.setNumberFormat = this.&setNumberFormat
        emc.setPercentageFormat = this.&setPercentageFormat
        
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
            String pre = (count++)==0? "\t" : ",\t"
            script.print pre+formatNumber(it)
        }
        script.println()
    }
    
    String formatNumber(double value) {
        return Double.isNaN(value)? "NaN" : df.format(value)
    }
    
    String formatPercentage(double value) {
        return Double.isNaN(value)? "NaN" : pf.format(value)        
    }
    
    void setNumberFormat(String format) {
        format = format?: DEFAULT_DECIMAL_FORMAT
        df.applyPattern(format)
        script.setProperty(DECIMAL_FORMAT, format)
    }
    
    void setPercentageFormat(String format) {
        format = format?: DEFAULT_PERCENTAGE_FORMAT
        pf.applyPattern(format)
        script.setProperty(PERCENTAGE_FORMAT, format)
    }
}

