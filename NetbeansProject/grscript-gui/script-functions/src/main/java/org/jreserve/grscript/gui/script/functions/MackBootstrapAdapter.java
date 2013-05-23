package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.MackBootstrapDelegate;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Bootstrap",
    position = 200
)
@NbBundle.Messages({
    "LBL.MackBootstrapAdapter.Name=MackBootstrapAdapter"
})
public class MackBootstrapAdapter extends AbstractFunctionProviderAdapter {

    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.mackBootstrap";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new MackBootstrapDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("mackBootstrap(Closure)");
    }

    @Override
    public String getName() {
        return Bundle.LBL_MackBootstrapAdapter_Name();
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