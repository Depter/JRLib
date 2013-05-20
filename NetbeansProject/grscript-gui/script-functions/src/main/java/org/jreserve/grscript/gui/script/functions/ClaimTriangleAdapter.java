package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.ClaimTriangleDelegate;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Triangle",
    position = 100
)
@NbBundle.Messages({
    "LBL.ClaimTriangleAdapter.Name=ClaimTriangle Delegate"
})
public class ClaimTriangleAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.claimTriangle";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new ClaimTriangleDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("triangle(double[][])");
        functions.add("triangle(double[][]) {}");
        functions.add("cummulate(ClaimTriangle)");
        functions.add("corrigate(ClaimTriangle, int, int, double)");
        functions.add("corrigate(ClaimTriangle, Map)");
        functions.add("exclude(ClaimTriangle, int, int, double)");
        functions.add("exclude(ClaimTriangle, Map)");
    }

    @Override
    public String getName() {
        return Bundle.LBL_ClaimTriangleAdapter_Name();
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
