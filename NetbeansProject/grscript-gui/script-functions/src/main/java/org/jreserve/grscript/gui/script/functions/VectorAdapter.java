package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.VectorDelegate;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Vector",
    position = 100
)
@NbBundle.Messages({
    "LBL.VectorAdapter.Name=VectorAdapter"
})
public class VectorAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.vector";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new VectorDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("vector(double[])");
        functions.add("vector(Collection)");
        functions.add("vector(int, double)");
        functions.add("corrigate(Vector, int, double)");
        functions.add("corrigate(Vector, Map)");
        functions.add("exclude(Vector, int...)");
        functions.add("exclude(Vector, Collection)");
        functions.add("smooth(double[], Closure)");
        functions.add("smooth(Vector, Closure)");
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
