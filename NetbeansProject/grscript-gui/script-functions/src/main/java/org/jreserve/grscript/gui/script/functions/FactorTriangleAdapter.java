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
import org.jreserve.grscript.jrlib.FactorTriangleDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Triangle",
    position = 200
)
@Messages({
    "LBL.FactorTriangleAdapter.Name=FactorTriangleAdapter"
})
public class FactorTriangleAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.factorTriangle";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new FactorTriangleDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("factors(double[][])");
        functions.add("factors(ClaimTriangle)");
        functions.add("exclude(FactorTriangle, int, int)");
        functions.add("exclude(FactorTriangle, Map)");
        functions.add("corrigate(FactorTriangle, int, int, double)");
        functions.add("corrigate(FactorTriangle, Map)");
        functions.add("smooth(FactorTriangle, Closure)");
        functions.add("factors(double[][], Closure)");
        functions.add("factors(ClaimTriangle, Closure)");
    }

    @Override
    protected void initProperties(List<String> properies) {
    }

    @Override
    public String getName() {
        return Bundle.LBL_FactorTriangleAdapter_Name();
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
