package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.LRResidualTriangleDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Link-Ratio",
    position = 500
)
@Messages({
    "LBL.LinkRatioResidualsAdapter.Name=LinkRatioResidualsAdapter"
})
public class LinkRatioResidualsAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.linkRatioResiduals";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new LRResidualTriangleDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("residuals(LinkRatioScale)");
        functions.add("exclude(LRResidualTriangle, int, int)");
        functions.add("exclude(LRResidualTriangle, Map)");
        functions.add("adjust(LRResidualTriangle)");
        functions.add("center(LRResidualTriangle)");
        functions.add("residuals(LinkRatioScale, Closure)");
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