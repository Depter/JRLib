package org.jreserve.grscript.gui.script.functions;

import java.util.List;
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
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.printData";
    
    @Override
    protected FunctionProvider createFunctionProvider() {
        return new PrintDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("printData(String, double[][])");
        functions.add("printData(double[][])");
        functions.add("printData(String, double[])");
        functions.add("printData(double[])");
        functions.add("setNumberFormat(String)");
    }

    @Override
    public String getName() {
        return Bundle.LBL_PrintDataAdapeter_Name();
    }

    @Override
    protected void initProperties(List<String> properies) {
    }

    @Override
    public String getFunctionHelpId(String function) {
        return HELP_ID;
    }

    @Override
    public String getPropertyHelpId(String property) {
        return HELP_ID;
    }
}
