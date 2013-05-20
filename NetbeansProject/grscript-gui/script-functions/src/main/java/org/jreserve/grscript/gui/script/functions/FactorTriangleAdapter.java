package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.FactorTriangleDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Triangle",
    position = 200
)
@Messages({
    "LBL.FactorTriangleAdapter.Name=FactorTriangleAdapter"
})
public class FactorTriangleAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.factorTriangle";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new FactorTriangleDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("factors(double[][])");
        functions.add("factors(ClaimTriangle)");
        functions.add("exclude(FactorTriangle, int, int)");
        functions.add("exclude(FactorTriangle, Map)");
        functions.add("corrigate(FactorTriangle, int, int, double)");
        functions.add("corrigate(FactorTriangle, Map)");
        functions.add("factors(double[][], Closure)");
        functions.add("factors(ClaimTriangle, Closure)");
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
