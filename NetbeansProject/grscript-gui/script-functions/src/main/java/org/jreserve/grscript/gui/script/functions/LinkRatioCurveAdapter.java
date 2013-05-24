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
import org.jreserve.grscript.jrlib.LinkRatioCurveDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Link-Ratio",
    position = 200
)
@Messages({
    "LBL.LinkRatioCurveAdapter.Name=LinkRatioCurveAdapter"
})
public class LinkRatioCurveAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.linkRatioCurve";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new LinkRatioCurveDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("smooth(LinkRatio, int)");
        functions.add("smooth(LinkRatio, int, String)");
        functions.add("smooth(LinkRatio, int, String, int)");
        functions.add("smooth(LinkRatio, int, String, Collection)");
        functions.add("smoothAll(LinkRatio, int)");
        functions.add("smoothAll(LinkRatio, int, String)");
        functions.add("smoothAll(LinkRatio, int, String, int)");
        functions.add("smoothAll(LinkRatio, int, String, Collection)");
        functions.add("smooth(LinkRatio, int, Closure)");
        functions.add("rSquare(LinkRatioSmoothing)");
    }

    @Override
    protected void initProperties(List<String> properies) {
    }

    @Override
    public String getName() {
        return Bundle.LBL_LinkRatioCurveAdapter_Name();
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