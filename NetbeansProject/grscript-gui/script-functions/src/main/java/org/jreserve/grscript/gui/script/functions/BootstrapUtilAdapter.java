package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.BootstrapUtilDelegate;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Bootstrap",
    position = 300
)
@NbBundle.Messages({
    "LBL.BootstrapUtilAdapter.Name=BootstrapUtilAdapter"
})
public class BootstrapUtilAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.bootstrapUtil";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new BootstrapUtilDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("totalReserves(double[][])");
        functions.add("accidentReserves(double[][], int)");
        functions.add("meanReserve(double[][])");
        functions.add("meanReserve(double[][], int)");
        functions.add("meanReserve(double[])");
        functions.add("minReserve(double[])");
        functions.add("maxReserve(double[])");
        functions.add("accidentMeans(double[][])");
        functions.add("shift(double[], double)");
        functions.add("shift(double[][], double[])");
        functions.add("scale(double[], double)");
        functions.add("scale(double[][], double[])");
        functions.add("percentile(double[], double)");
        functions.add("histogram(double[])");
        functions.add("histogram(double[], int)");
        functions.add("histogram(double[], double , double)");
    }

    @Override
    public String getName() {
        return Bundle.LBL_BootstrapUtilAdapter_Name();
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
