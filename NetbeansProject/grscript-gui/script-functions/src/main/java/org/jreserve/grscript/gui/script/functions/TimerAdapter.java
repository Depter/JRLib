package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.util.TimerDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Utilities",
    position = 300
)
@Messages({
    "LBL.TimerAdapter.Name=TimerAdapter"
})
public class TimerAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.timer";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new TimerDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("startTimer(String)");
        functions.add("stopTimer(String)");
        functions.add("finnishTimer(String)");
    }

    @Override
    protected void initProperties(List<String> properies) {
    }

    @Override
    public String getName() {
        return Bundle.LBL_TimerAdapter_Name();
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