package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.CRResidualTriangleDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Claim-Ratio",
    position = 300
)
@Messages({
    "LBL.ClaimRatioResidualsAdapter.Name=ClaimRatioResidualsAdapter"
})
public class ClaimRatioResidualsAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.claimRatioResiduals";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new CRResidualTriangleDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("residuals(ClaimRatioScale)");
        functions.add("exclude(CRResidualTriangle, int, int)");
        functions.add("exclude(CRResidualTriangle, Map)");
        functions.add("adjust(CRResidualTriangle)");
        functions.add("center(CRResidualTriangle)");
        functions.add("residuals(ClaimRatioScale, Closure)");
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