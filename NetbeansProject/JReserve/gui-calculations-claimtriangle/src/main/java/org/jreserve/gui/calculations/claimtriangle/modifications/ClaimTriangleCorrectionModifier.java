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
package org.jreserve.gui.calculations.claimtriangle.modifications;

import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractCalculationModifier;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleModifier;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangleCorrection;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - accident",
    "# {1} - development",
    "# {2} - value",
    "LBL.ClaimTriangleCorrectionModifier.Description=Correction [{0}; {1}] = {2}",
    "LBL.ClaimTriangleCorrectionModifier.Name=Correction"
})
public class ClaimTriangleCorrectionModifier extends AbstractCalculationModifier<ClaimTriangle> implements ClaimTriangleModifier {
    
    final static String ROOT_ELEMENT = "claimTriangleCorrection";
    final static String ACCIDENT_ELEMENT = "accident";
    final static String DEVELOPMENT_ELEMENT = "development";
    final static String VALUE_ELEMENT = "value";
    
    private int accident;
    private int development;
    private double value;

    public ClaimTriangleCorrectionModifier(int accident, int development, double value) {
        super(ClaimTriangle.class);
        this.accident = accident;
        this.development = development;
        this.value = value;
    }
    
    @Override
    public ClaimTriangle createCalculation(ClaimTriangle sourceCalculation) {
        return new ClaimTriangleCorrection(sourceCalculation, accident, development, value);
    }

    @Override
    public Element toXml() {
        Element root = new Element(ROOT_ELEMENT);
        JDomUtil.addElement(root, ACCIDENT_ELEMENT, accident);
        JDomUtil.addElement(root, DEVELOPMENT_ELEMENT, development);
        JDomUtil.addElement(root, VALUE_ELEMENT, value);
        return root;
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_ClaimTriangleCorrectionModifier_Description(
                accident+1, development+1, value);
    }

    @Override
    protected Node createNodeDelegate() {
        AbstractNode node = new AbstractNode(Children.LEAF);
        node.setDisplayName(Bundle.LBL_ClaimTriangleCorrectionModifier_Name());
        node.setShortDescription(getDescription());
        //TODO icon
        return node;
    }

    @Override
    public TriangleLayer createLayer(ClaimTriangle input) {
        return new CorrectionLayer(input);
    }
}
