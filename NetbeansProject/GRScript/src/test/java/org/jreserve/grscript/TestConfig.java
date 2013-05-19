package org.jreserve.grscript;

import java.io.File;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TestConfig {
    public final static double EPSILON = 0.00000001;
    
    public final static String APC_PAID = "apc_paid.csv";
    public final static String APC_INCURRED = "apc_incurred.csv";
    public final static String APC_EXPOSURE = "apc_exposure.csv";
    public final static String APC_NOC = "apc_noc.csv";
    public final static String MCL_PAID = "mcl_paid.csv";
    public final static String MCL_INCURRED = "mcl_incurred.csv";
    private final static String DATA_HOME = "src/main/resources/org/jreserve/grscript/input/";
    
    public static String getPath(String dataName) {
        File file = new File(DATA_HOME + dataName);
        return file.getAbsolutePath();
    }
}
