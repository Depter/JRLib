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
import org.jreserve.grscript.jrlib.EstimateDelegate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@FunctionProviderAdapter.Registration(
    path = "Estimates",
    position = 100
)
@Messages({
    "LBL.EstimateAdapter.Name=EstimateAdapter"
})
public class EstimateAdapter extends AbstractFunctionProviderAdapter {
    
    private final static String HELP_ID = "org.jreserve.grscript.gui.script.functions.estimates";

    @Override
    protected FunctionProvider createFunctionProvider() {
        return new EstimateDelegate();
    }

    @Override
    protected void initFunctions(List<String> functions) {
        functions.add("averageCostEstimate(LinkRatio, LinkRatio)");
        functions.add("ACEstimate(LinkRatio, LinkRatio)");
        functions.add("bornhuetterFergusonEstimate(LinkRatio, Vector, Vector)");
        functions.add("BFEstimate(LinkRatio, Vector, Vector)");
        functions.add("expectedLossRatioEstimate(LinkRatio, Vector, Vector)");
        functions.add("ELREstimate(LinkRatio, Vector, Vector)");
        functions.add("capeCodEstimate(LinkRatio, Vector)");
        functions.add("CCEstimate(LinkRatio, Vector)");
        functions.add("chainLadderEstimate(LinkRatio)");
        functions.add("CLEstimate(LinkRatio)");
        functions.add("mackEstimate(LinkRatioSE)");
        functions.add("munichChainLadderEstimate(Closure)");
        functions.add("MCLEstimate(Closure)");
        functions.add("estimate(Map)");
        functions.add("compositeEstimate(Estimate...)");
        functions.add("compositeEstimate(Collection)");
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