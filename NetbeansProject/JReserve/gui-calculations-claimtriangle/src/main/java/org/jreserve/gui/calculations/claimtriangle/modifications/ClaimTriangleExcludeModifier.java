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

import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.modification.AbstractCalculationModifier;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleModifier;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangleCorrection;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - accident",
    "# {1} - development",
    "LBL.ClaimTriangleExcludeModifier.Description=Exclusion [{0}; {1}]",
    "LBL.ClaimTriangleExcludeModifier.Name=Exclusion"
})
public class ClaimTriangleExcludeModifier extends AbstractCalculationModifier<ClaimTriangle> implements ClaimTriangleModifier {
    final static String ROOT_ELEMENT = "claimTriangleExclusion";
    final static String ACCIDENT_ELEMENT = "accident";
    final static String DEVELOPMENT_ELEMENT = "development";

    private final static String IMG_PATH = "org/jreserve/gui/calculations/icons/exclude.png";   //NOI18
    final static Icon ICON = ImageUtilities.loadImageIcon(IMG_PATH, false);
    
    private int accident;
    private int development;

    public ClaimTriangleExcludeModifier(int accident, int development) {
        super(ClaimTriangle.class);
        this.accident = accident;
        this.development = development;
    }    
    
    @Override
    public ClaimTriangle createCalculation(ClaimTriangle sourceCalculation) {
        return new ClaimTriangleCorrection(sourceCalculation, accident, development, Double.NaN);
    }

    @Override
    public Element toXml() {
        Element root = new Element(ROOT_ELEMENT);
        JDomUtil.addElement(root, ACCIDENT_ELEMENT, accident);
        JDomUtil.addElement(root, DEVELOPMENT_ELEMENT, development);
        return root;
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_ClaimTriangleExcludeModifier_Description(
                accident+1, development+1);
    }

    @Override
    protected Displayable createDisplayable() {
        return new ExclusionDisplayable();
    }

    @Override
    public TriangleLayer createLayer(ClaimTriangle input) {
        return new ExclusionLayer(input);
    }

    @Override
    public List<Cell> getAffectedCells() {
        return Collections.singletonList(new Cell(accident, development));
    }
    
    private static class ExclusionDisplayable implements Displayable {
        
        @Override
        public Icon getIcon() {
            return ICON;
        }

        @Override
        public String getDisplayName() {
            return Bundle.LBL_ClaimTriangleExcludeModifier_Name();
        }
    }
}
