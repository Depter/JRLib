package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.input.CsvWriterDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Input-Output",
    position = 200
)
@Messages({
    "LBL.CsvWriterAdapter.Name=CsvWriterAdapter"
})
public class CsvWriterAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.csvWriter";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new CsvWriterDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("writeCsv(String, double[])");
        functions.add("writeCsv(File, double[])");
        functions.add("writeCsv(Writer, double[])");
        functions.add("writeCsv(String, double[], Map)");
        functions.add("writeCsv(File, double[], Map)");
        functions.add("writeCsv(Writer, double[], Map)");
        functions.add("writeCsv(String, double[], Closure)");
        functions.add("writeCsv(File, double[], Closure)");
        functions.add("writeCsv(Writer, double[], Closure)");
        functions.add("writeCsv(String, double[][])");
        functions.add("writeCsv(File, double[][])");
        functions.add("writeCsv(Writer, double[][])");
        functions.add("writeCsv(String, double[][], Map)");
        functions.add("writeCsv(File, double[][], Map)");
        functions.add("writeCsv(Writer, double[][], Map)");
        functions.add("writeCsv(String, double[][], Closure)");
        functions.add("writeCsv(File, double[][], Closure)");
        functions.add("writeCsv(Writer, double[][], Closure)");
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
