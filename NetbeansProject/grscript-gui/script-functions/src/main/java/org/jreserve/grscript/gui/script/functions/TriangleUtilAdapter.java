package org.jreserve.grscript.gui.script.functions;

import java.util.Map;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.gui.script.AbstractFunctionProviderAdapter;
import org.jreserve.grscript.gui.script.FunctionProviderAdapter;
import org.jreserve.grscript.jrlib.TriangleUtilDelegate;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Utilities",
    position = 200
)
@NbBundle.Messages({
    "LBL.TriangleUtilAdapter.Name=TriangleUtil Delegate"
})
public class TriangleUtilAdapter extends AbstractFunctionProviderAdapter {

    private final static String CUMMULATE_D =
        surroundHtml("Cummulates the given accident period.");
    private final static String CUMMULATE_DD =
        surroundHtml("Cummulates the given triangle.");
    private final static String DECUMMULATE_D =
        surroundHtml("Decummulates the given accident period.");
    private final static String DECUMMULATE_DD =
        surroundHtml("Decummulates the given triangle.");
    private final static String COPY_D =
        surroundHtml("Copies the given vector.");
    private final static String COPY_DD =
        surroundHtml("Copies the given triangle.");
    
    private final static String PRINT_T =
        surroundHtml("Prints the given triangle.");
    private final static String PRINT_TS =
        surroundHtml("Prints the given triangle with the given title.");
    private final static String PRINT_V =
        surroundHtml("Prints the given vector.");
    private final static String PRINT_VS =
        surroundHtml("Prints the given vector with the given title.");
    
    @Override
    protected FunctionProvider createFunctionProvider() {
        return new TriangleUtilDelegate();
    }

    @Override
    protected void initFunctions(Map<String, String> functions) {
        functions.put("cummulate(double[][])",       CUMMULATE_DD);
        functions.put("cummulate(double[])",         CUMMULATE_D);
        functions.put("decummulate(double[][])",     DECUMMULATE_DD);
        functions.put("decummulate(double[])",       DECUMMULATE_D);
        functions.put("copy(double[][])",            COPY_DD);
        functions.put("copy(double[])",              COPY_DD);

        functions.put("printData(Triangle)",         PRINT_T);
        functions.put("printData(String, Triangle)", PRINT_TS);
        functions.put("printData(Vector)",           PRINT_V);
        functions.put("printData(String, Vector)",   PRINT_VS);
    }

    @Override
    public String getName() {
        return Bundle.LBL_TriangleUtilAdapter_Name();
    }
}
