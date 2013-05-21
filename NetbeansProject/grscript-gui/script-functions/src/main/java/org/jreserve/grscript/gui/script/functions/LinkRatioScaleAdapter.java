package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.LinkRatioScaleDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Link-Ratio",
    position = 300
)
@Messages({
    "LBL.LinkRatioScaleAdapter.Name=LinkRatioScaleAdapter"
})
public class LinkRatioScaleAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.linkRatioScale";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new LinkRatioScaleDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("scale(LinkRatio)");
        functions.add("scale(LinkRatio, String)");
        functions.add("scale(LinkRatio, Closure)");
    }

    @Override
    protected void initProperties(List<String> properies) {
    }

    @Override
    public String getName() {
        return Bundle.LBL_LinkRatioScaleAdapter_Name();
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