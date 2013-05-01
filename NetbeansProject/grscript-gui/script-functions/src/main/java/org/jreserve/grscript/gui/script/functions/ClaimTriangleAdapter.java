package org.jreserve.grscript.gui.script.functions;

import java.util.Map;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.ClaimTriangleDelegate;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Triangles",
    position = 100
)
@NbBundle.Messages({
    "LBL.ClaimTriangleAdapter.Name=ClaimTriangle Delegate"
})
public class ClaimTriangleAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String CONSTRUCTOR_SIMPLE = 
        surroundHtml("Creates a claim triangle from the input.");
    private final static String CONTRUCTOR_CLOSURE =
        surroundHtml("Builder for triangle. Within the builder simply use the "+
            "'corrigate' and 'exclude' without the triangle parameter.");
    private final static String CORRIGATE_SIMPLE = 
        surroundHtml("Corrigates the given triangle. First int is accident, second is development.");
    private final static String CORRIGATE = 
        "<html><head/><body>Corrigates the given triangle. The map can "+
        "contain the following parameters:<ul>"+
           "<li>&quot;a&quot; or &quot;accident&quot; for accident period,</li>"+
           "<li>&quot;d&quot; or &quot;development&quot; for the development period,</li>"+
           "<li>&quot;correction&quot; or &quot;value&quot; for the new value.</li>"+
        "</ul></body></html>";
    private final static String EXCLUDE_SIMPLE = 
        surroundHtml("Excludes a cell from the given triangle. First "+
        "int is accident, second is development.");    
    private final static String EXCLUDE = 
        "<html><head/><body>Excludes a cell from the given triangle. "+
        "The map can contain the following parameters:<ul>"+
        "<li>&quot;a&quot; or &quot;accident&quot; for accident period,</li>"+
        "<li>&quot;d&quot; or &quot;development&quot; for the development period.</li>"+
        "</ul></body></html>";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new ClaimTriangleDelegate();
    }

    @Override
    protected void initFunctions(Map<String, String> functions) {
        functions.put("triangle(double[][])", CONSTRUCTOR_SIMPLE);
        functions.put("triangle(double[][]) {}", CONTRUCTOR_CLOSURE);
        functions.put("corrigate(ClaimTriangle, int, int, double)", CORRIGATE_SIMPLE);
        functions.put("corrigate(ClaimTriangle, Map)", CORRIGATE);
        functions.put("exclude(ClaimTriangle, int, int, double)", EXCLUDE_SIMPLE);
        functions.put("exclude(ClaimTriangle, Map)", EXCLUDE);
    }

    @Override
    public String getName() {
        return Bundle.LBL_ClaimTriangleAdapter_Name();
    }

    @Override
    protected void initProperties(Map<String, String> properies) {
    }
}
