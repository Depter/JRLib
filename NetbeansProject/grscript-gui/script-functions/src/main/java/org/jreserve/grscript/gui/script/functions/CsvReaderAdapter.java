package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.input.CsvReaderDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Input",
    position = 100
)
@Messages({
    "LBL.CsvReaderAdapter.Name=CsvReaderAdapter"
})
public class CsvReaderAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.csvReader";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new CsvReaderDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("readCsv(String)");
        functions.add("readCsv(File)");
        functions.add("readCsv(String, Map)");
        functions.add("readCsv(File, Map)");
        functions.add("readCsv(String, Closure)");
        functions.add("readCsv(File, Closure)");
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