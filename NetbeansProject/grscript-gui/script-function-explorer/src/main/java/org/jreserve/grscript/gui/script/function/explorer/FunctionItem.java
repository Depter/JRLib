/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
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
