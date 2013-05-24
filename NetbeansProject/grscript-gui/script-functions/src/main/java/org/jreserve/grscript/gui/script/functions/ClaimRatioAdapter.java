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
package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.jrlib.ClaimRatioDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Claim-Ratio",
    position = 100
)
@Messages({
    "LBL.ClaimRatioAdapter.Name=ClaimRatioAdapter"
})
public class ClaimRatioAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.claimRatio";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new ClaimRatioDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("ratios(ClaimTriangle, ClaimTriangle)");
        functions.add("ratios(RatioTriangle)");
        functions.add("ratios(LinkRatio, LinkRatio)");
        functions.add("ratios(Map)");
        
        functions.add("ratios(RatioTriangle, LinkRatio, LinkRatio)");
        functions.add("ratios(RatioTriangle, int, LinkRatio, LinkRatio)");
        
        functions.add("ratios(LinkRatio, LinkRatio, Closure)");
        functions.add("ratios(LinkRatio, LinkRatio, int, Closure)");
        functions.add("ratios(RatioTriangle, int, Closure)");
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