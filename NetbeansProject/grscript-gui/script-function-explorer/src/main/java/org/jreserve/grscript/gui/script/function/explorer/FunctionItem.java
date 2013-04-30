package org.jreserve.grscript.gui.script.function.explorer;

import org.jreserve.grscript.gui.script.FunctionProviderAdapter;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FunctionItem {
    
    private final FunctionProviderAdapter adapter;
    private final String signiture;

    public FunctionItem(FunctionProviderAdapter adapter, String signiture) {
        this.adapter = adapter;
        this.signiture = signiture;
    }

    public FunctionProviderAdapter getAdapter() {
        return adapter;
    }

    public String getSigniture() {
        return signiture;
    }
    
    public String getDescription() {
        return adapter.getFunctionDescription(signiture);
    }
}
