package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.RatioTriangleDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Triangle",
    position = 300
)
@Messages({
    "LBL.RatioTriangleAdapter.Name=RatioTriangleAdapter"
})
public class RatioTriangleAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.ratioTriangle";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new RatioTriangleDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("ratioTriangle(ClaimTriangle, ClaimTriange)");
        functions.add("ratioTriangle(Map map)");
        functions.add("factors(ClaimTriangle)");
        functions.add("exclude(RatioTriangle, int, int)");
        functions.add("exclude(RatioTriangle, Map)");
        functions.add("corrigate(RatioTriangle, int, int, double)");
        functions.add("corrigate(RatioTriangle, Map)");
        functions.add("smooth(RatioTriangle, Closure)");
        functions.add("ratioTriangle(ClaimTriangle, ClaimTriange, Closure)");
    }

    @Override
    protected void initProperties(List<String> properies) {
    }

    @Override
    public String getName() {
        return Bundle.LBL_RatioTriangleAdapter_Name();
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
