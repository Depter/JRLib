package org.jreserve.grscript.gui.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jreserve.grscript.FunctionProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractFunctionProviderAdapter implements FunctionProviderAdapter {
    
    protected static String surroundHtml(String str) {
        return "<html><head></head><body>"+escapeString(str)+"</body></html>";
    }
    
    private static String escapeString(String str) {
        str = str.replaceAll("'", "&quot;");
        str = str.replaceAll("\"", "&quot;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        return str;
    }
    
    private FunctionProvider provider;
    private Map<String, String> functions;
    
    @Override
    public synchronized FunctionProvider getFunctionProvider() {
        if(provider == null)
            provider = createFunctionProvider();
        return provider;
    }

    protected abstract FunctionProvider createFunctionProvider();
    
    @Override
    public synchronized List<String> getFunctionSignitures() {
        return new ArrayList<String>(getFunctions().keySet());
    }

    private Map<String, String> getFunctions() {
        if(functions == null) {
            functions = new TreeMap<String, String>();
            initFunctions(functions);
        }
        return functions;
    }
    
    protected abstract void initFunctions(Map<String, String> functions);
    
    @Override
    public synchronized String getFunctionDescription(String signiture) {
        String desc = getFunctions().get(signiture);
        return desc == null? "" : desc;
    }

}
