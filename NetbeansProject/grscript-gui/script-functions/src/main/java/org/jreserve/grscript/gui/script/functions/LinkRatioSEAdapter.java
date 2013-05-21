package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.LinkRatioSEDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Link-Ratio",
    position = 400
)
@Messages({
    "LBL.LinkRatioSEAdapter.Name=LinkRatioSEAdapter"
})
public class LinkRatioSEAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.linkRatioSE";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new LinkRatioSEDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("standardError(LinkRatioScale)");
        functions.add("standardError(LinkRatioScale, String)");
        functions.add("standardError(LinkRatioScale, Closure)");
    }

    @Override
    protected void initProperties(List<String> properies) {
    }

    @Override
    public String getName() {
        return Bundle.LBL_LinkRatioSEAdapter_Name();
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