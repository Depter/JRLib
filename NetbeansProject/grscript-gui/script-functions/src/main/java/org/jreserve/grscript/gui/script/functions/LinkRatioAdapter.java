package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.openide.util.NbBundle.Messages;
import org.jreserve.grscript.jrlib.LinkRatioDelegate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Link-Ratio",
    position = 100
)
@Messages({
    "LBL.LinkRatioAdapter.Name=LinkRatioAdapter"
})
public class LinkRatioAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.linkRatio";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new LinkRatioDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("linkRatio(double[][])");
        functions.add("linkRatio(ClaimTriangle)");
        functions.add("linkRatio(FactorTriangle)");
        functions.add("linkRatio(double[][], Closure)");
        functions.add("linkRatio(ClaimTriangle, Closure)");
        functions.add("linkRatio(FactorTriangle, Closure)");
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
