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
package org.jreserve.gui.calculations.factor.impl.factors;

import java.awt.Color;
import java.awt.Component;
import org.jreserve.gui.trianglewidget.DefaultTriangleLayer;
import org.jreserve.gui.trianglewidget.DefaultTriangleWidgetRenderer;
import org.jreserve.gui.trianglewidget.TriangleWidget;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangleCorrection;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ExclusionLayer.Name=Exclusion"
})
class ExclusionLayer extends DefaultTriangleLayer {
    
    private int accident;
    private int development;
    
    ExclusionLayer(Triangle triangle) {
        super(triangle, Bundle.LBL_ExclusionLayer_Name(), 
                FactorTriangleExcludeModifier.ICON, 
                new ExclusionRenderer());
        if(triangle instanceof ClaimTriangleCorrection) {
            ClaimTriangleCorrection correction = (ClaimTriangleCorrection) triangle;
            accident = correction.getCorrigatedAccident();
            development = correction.getCorrigatedDevelopment();
        } else {
            accident = -1;
            development = -1;
        }
    }

    @Override
    public boolean rendersCell(int accident, int development) {
        return this.accident == accident &&
               this.development == development;
    }
    
    private static class ExclusionRenderer extends DefaultTriangleWidgetRenderer {

        @Override
        public Component getComponent(TriangleWidget widget, double value, int row, int column, boolean selected) {
            super.getComponent(widget, value, row, column, selected);
            if(!selected)
                setBackground(Color.RED);
            return this;
        }
    }
}
