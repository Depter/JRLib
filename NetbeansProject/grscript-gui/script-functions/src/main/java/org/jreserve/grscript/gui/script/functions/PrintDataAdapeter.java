package org.jreserve.grscript.gui.script.functions;

import java.util.Map;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.util.PrintDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Utilities",
    position = 100
)
@Messages({
    "LBL.PrintDataAdapeter.Name=PrintDataDelegate"
})
public class PrintDataAdapeter extends AbstractFunctionProviderAdapter {
    
    private final static String PRINT_D = 
        surroundHtml("Prints the given array, horizontally.");
    private final static String PRINT_S_D = 
        surroundHtml("Prints the given array horizontally, with the given title.");
    private final static String PRINT_DD = 
        surroundHtml("Prints the given matrix as a triangle.");
    private final static String PRINT_S_DD = 
        surroundHtml("Prints the given matrix as a triangle, with the given title.");
    
    @Override
    protected FunctionProvider createFunctionProvider() {
        return new PrintDelegate();
    }

    @Override
    protected void initFunctions(Map<String, String> functions) {
        functions.put("printData(String, double[][])", PRINT_S_DD);
        functions.put("printData(double[][])", PRINT_DD);
        functions.put("printData(String, double[])", PRINT_S_D);
        functions.put("printData(double[])", PRINT_D);
        functions.put("setNumberFormat(String)", "Sets the numberformat used by the printData methods.");
    }

    @Override
    public String getName() {
        return Bundle.LBL_PrintDataAdapeter_Name();
    }

    @Override
    protected void initProperties(Map<String, String> properies) {
    }
}
