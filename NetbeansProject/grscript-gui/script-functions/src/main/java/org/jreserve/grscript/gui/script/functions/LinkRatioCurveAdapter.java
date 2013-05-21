package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.LinkRatioCurveDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Link-Ratio",
    position = 200
)
@Messages({
    "LBL.LinkRatioCurveAdapter.Name=LinkRatioCurveAdapter"
})
public class LinkRatioCurveAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.linkRatioCurve";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new LinkRatioCurveDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("smooth(LinkRatio, int)");
        functions.add("smooth(LinkRatio, int, String)");
        functions.add("smooth(LinkRatio, int, String, int)");
        functions.add("smooth(LinkRatio, int, String, Collection)");
        functions.add("smoothAll(LinkRatio, int)");
        functions.add("smoothAll(LinkRatio, int, String)");
        functions.add("smoothAll(LinkRatio, int, String, int)");
        functions.add("smoothAll(LinkRatio, int, String, Collection)");
        functions.add("smooth(LinkRatio, int, Closure)");
        functions.add("rSquare(LinkRatioSmoothing)");
    }

    @Override
    protected void initProperties(List<String> properies) {
    }

    @Override
    public String getName() {
        return Bundle.LBL_LinkRatioCurveAdapter_Name();
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