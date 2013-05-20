package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import java.util.Map;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.TriangleUtilDelegate;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Utilities",
    position = 200
)
@NbBundle.Messages({
    "LBL.TriangleUtilAdapter.Name=TriangleUtil Delegate"
})
public class TriangleUtilAdapter extends AbstractFunctionProviderAdapter {

    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.triangleUtil";
    
    @Override
    protected FunctionProvider createFunctionProvider() {
        return new TriangleUtilDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("cummulate(double[][])");
        functions.add("cummulate(double[])");
        functions.add("decummulate(double[][])");
        functions.add("decummulate(double[])");
        functions.add("copy(double[][])");
        functions.add("copy(double[])");

        functions.add("printData(Triangle)");
        functions.add("printData(String, Triangle)");
        functions.add("printData(Vector)");
        functions.add("printData(String, Vector)");
    }

    @Override
    public String getName() {
        return Bundle.LBL_TriangleUtilAdapter_Name();
    }

    @Override
    protected void initProperties(List<String> properties) {
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
