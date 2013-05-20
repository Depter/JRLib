package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.TestDataDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Test Data",
    position = 100
)
@Messages({
    "LBL.TestDataAdapter.Name=TestDataAdapter"
})
public class TestDataAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.testData";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new TestDataDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("apcPaid()");
        functions.add("apcIncurred()");
        functions.add("apcNoC()");
        functions.add("apcExposure()");
        functions.add("mclPaid()");
        functions.add("mclIncurred()");
    }

    @Override
    protected void initProperties(List<String> properies) {
    }

    @Override
    public String getName() {
        return Bundle.LBL_TestDataAdapter_Name();
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
