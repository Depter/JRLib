package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.EstimateDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Estimates",
    position = 100
)
@Messages({
    "LBL.EstimateAdapter.Name=EstimateAdapter"
})
public class EstimateAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.estimates";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new EstimateDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("averageCostEstimate(LinkRatio, LinkRatio)");
        functions.add("ACEstimate(LinkRatio, LinkRatio)");
        functions.add("bornhuetterFergusonEstimate(LinkRatio, Vector, Vector)");
        functions.add("BFEstimate(LinkRatio, Vector, Vector)");
        functions.add("expectedLossRatioEstimate(LinkRatio, Vector, Vector)");
        functions.add("ELREstimate(LinkRatio, Vector, Vector)");
        functions.add("capeCodEstimate(LinkRatio, Vector)");
        functions.add("CCEstimate(LinkRatio, Vector)");
        functions.add("chainLadderEstimate(LinkRatio)");
        functions.add("CLEstimate(LinkRatio)");
        functions.add("mackEstimate(LinkRatioSE)");
        functions.add("munichChainLadderEstimate(Closure)");
        functions.add("MCLEstimate(Closure)");
        functions.add("estimate(Map)");
        functions.add("compositeEstimate(Estimate...)");
        functions.add("compositeEstimate(Collection)");
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