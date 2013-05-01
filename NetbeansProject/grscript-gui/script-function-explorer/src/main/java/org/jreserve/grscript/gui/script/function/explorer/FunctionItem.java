package org.jreserve.grscript.gui.script.function.explorer;

import org.jreserve.grscript.gui.script.functions.FunctionProviderAdapter;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FunctionItem {
    
    private final FunctionProviderAdapter adapter;
    private final String signiture;
    private boolean isProperty;
    
    public FunctionItem(FunctionProviderAdapter adapter, String signiture, boolean isProperty) {
        this.adapter = adapter;
        this.signiture = signiture;
        this.isProperty = isProperty;
    }

    public FunctionProviderAdapter getAdapter() {
        return adapter;
    }

    public String getSigniture() {
        return signiture;
    }
    
    public boolean isProperty() {
        return isProperty;
    }
    
    public String getDescription() {
        if(isProperty)
            return adapter.getPropertyDescription(signiture);
        return adapter.getFunctionDescription(signiture);
    }
}
