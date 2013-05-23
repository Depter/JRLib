package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.MclBootstrapDelegate;
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
    "LBL.MclBootstrapAdapter.Name=MclBootstrapAdapter"
})
public class MclBootstrapAdapter extends AbstractFunctionProviderAdapter {

    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.mclBootstrap";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new MclBootstrapDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("MclBootstrap(Closure)");
    }

    @Override
    public String getName() {
        return Bundle.LBL_MclBootstrapAdapter_Name();
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