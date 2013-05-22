package org.jreserve.grscript.gui.script.functions;

import groovy.lang.ExpandoMetaClass;
import groovy.lang.Script;
import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.ClaimResidualDelegate;
import org.jreserve.grscript.jrlib.ClaimResidualScaleDelegate;
import org.jreserve.grscript.jrlib.ScaledClaimResidualDelegate;
import org.jreserve.grscript.jrlib.OdpBootstrapDelegate;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Bootstrap",
    position = 100
)
@NbBundle.Messages({
    "LBL.OdpBootstrapAdapter.Name=OdpBootstrapAdapter"
})
public class OdpBootstrapAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.odpBootstrap";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new OdpFunctionProvider();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        //Residuals
        functions.add("residuals(LinkRatio)");
        functions.add("exclude(OdpResidualTriangle, int, int)");
        functions.add("exclude(OdpResidualTriangle, Map)");
        functions.add("adjust(OdpResidualTriangle)");
        functions.add("center(OdpResidualTriangle)");
        functions.add("residuals(LinkRatio, Closure)");
        
        //Scales
        functions.add("constantScale(LinkRatio)");
        functions.add("constantScale(OdpResidualTriangle)");
        functions.add("constantScale(OdpResidualTriangle, double)");
        functions.add("variableScale(LinkRatio)");
        functions.add("variableScale(OdpResidualTriangle)");
        functions.add("variableScale(OdpResidualTriangle, Closure)");
        
        //Scaled residuals
        functions.add("residuals(OdpResidualScale)");
        functions.add("exclude(OdpScaledResidualTriangle, int, int)");
        functions.add("exclude(OdpScaledResidualTriangle, Map)");
        functions.add("adjust(OdpScaledResidualTriangle)");
        functions.add("center(OdpScaledResidualTriangle)");
        functions.add("residuals(OdpResidualScale, Closure)");
        
        
        //Bootstrap
        functions.add("odpBootstrap(Closure)");
    }

    @Override
    public String getName() {
        return Bundle.LBL_ClaimTriangleAdapter_Name();
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
    
    private class OdpFunctionProvider implements FunctionProvider {
        
        private ClaimResidualDelegate residuals = new ClaimResidualDelegate();
        private ClaimResidualScaleDelegate scales = new ClaimResidualScaleDelegate();
        private ScaledClaimResidualDelegate scaledResiduals = new ScaledClaimResidualDelegate();
        private OdpBootstrapDelegate bootstrap = new OdpBootstrapDelegate();
        
        @Override
        public void initFunctions(Script script, ExpandoMetaClass emc) {
            residuals.initFunctions(script, emc);
            scales.initFunctions(script, emc);
            scaledResiduals.initFunctions(script, emc);
            bootstrap.initFunctions(script, emc);
        }
    
    }
}
