package org.jreserve.grscript.gui.script.function.explorer;

import org.jreserve.grscript.gui.script.functions.FunctionProviderAdapter;
import org.openide.util.HelpCtx;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FunctionItem implements HelpCtx.Provider {
    
    private final FunctionProviderAdapter adapter;
    private final String signiture;
    private boolean isProperty;
    private HelpCtx helpCtx;
    
    public FunctionItem(FunctionProviderAdapter adapter, String signiture, boolean isProperty) {
        this.adapter = adapter;
        this.signiture = signiture;
        this.isProperty = isProperty;
        
        String id = getHelpId();
        helpCtx = (id==null || id.length() == 0)?
                HelpCtx.DEFAULT_HELP :
                new HelpCtx(id);
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
    
    @Override
    public HelpCtx getHelpCtx() {
        return helpCtx;
    }
    
    private String getHelpId() {
        if(adapter == null)
            return null;
        return isProperty? 
                adapter.getPropertyHelpId(signiture) :
                adapter.getFunctionHelpId(signiture);
    }
}
