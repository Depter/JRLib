package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.ClaimRatioDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Claim-Ratio",
    position = 100
)
@Messages({
    "LBL.ClaimRatioAdapter.Name=ClaimRatioAdapter"
})
public class ClaimRatioAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.claimRatio";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new ClaimRatioDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("ratios(ClaimTriangle, ClaimTriangle)");
        functions.add("ratios(RatioTriangle)");
        functions.add("ratios(LinkRatio, LinkRatio)");
        functions.add("ratios(Map)");
        
        functions.add("ratios(RatioTriangle, LinkRatio, LinkRatio)");
        functions.add("ratios(RatioTriangle, int, LinkRatio, LinkRatio)");
        
        functions.add("ratios(LinkRatio, LinkRatio, Closure)");
        functions.add("ratios(LinkRatio, LinkRatio, int, Closure)");
        functions.add("ratios(RatioTriangle, int, Closure)");
    }

    @Override
    protected void initProperties(List<String> properies) {
    }

    @Override
    public String getName() {
        return Bundle.LBL_LinkRatioAdapter_Name();
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