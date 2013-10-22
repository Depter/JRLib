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
package org.jreserve.gui.calculations.vector.modifications;

import java.awt.Color;
import java.awt.Component;
import org.jreserve.gui.calculations.vector.modifications.VectorCorrectionModifier;
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
    "LBL.CorrectionLayer.Name=Correction"
})
class CorrectionLayer extends DefaultTriangleLayer {
    
    private int accident;
    
    CorrectionLayer(Triangle triangle, int accident) {
        super(triangle, Bundle.LBL_CorrectionLayer_Name(), 
                VectorCorrectionModifier.ICON, 
                new CorrectionRenderer());
        this.accident = accident;
    }

    @Override
    public boolean rendersCell(int accident, int development) {
        return this.accident == accident;
    }
    
    private static class CorrectionRenderer extends DefaultTriangleWidgetRenderer {

        @Override
        public Component getComponent(TriangleWidget widget, double value, int accident, int development, boolean selected) {
            super.getComponent(widget, value, accident, development, selected);
            if(!selected)
                setBackground(Color.ORANGE);
            return this;
        }
        
    }
}
