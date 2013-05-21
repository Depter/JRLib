package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.ClaimRatioScaleDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Claim-Ratio",
    position = 200
)
@Messages({
    "LBL.ClaimRatioScaleAdapter.Name=ClaimRatioScaleAdapter"
})
public class ClaimRatioScaleAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.claimRatioScale";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new ClaimRatioScaleDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("scale(ClaimRatio)");
        functions.add("scale(ClaimRatio, String)");
        functions.add("scale(ClaimRatio, Closure)");
    }

    @Override
    protected void initProperties(List<String> properies) {
    }

    @Override
    public String getName() {
        return Bundle.LBL_ClaimRatioScaleAdapter_Name();
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