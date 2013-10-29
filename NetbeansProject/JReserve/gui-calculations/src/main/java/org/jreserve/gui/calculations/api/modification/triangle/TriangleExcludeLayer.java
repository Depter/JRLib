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
package org.jreserve.gui.calculations.api.modification.triangle;

import java.awt.Color;
import java.awt.Component;
import org.jreserve.gui.calculations.api.modification.DefaultColor;
import org.jreserve.gui.trianglewidget.DefaultTriangleLayer;
import org.jreserve.gui.trianglewidget.DefaultTriangleWidgetRenderer;
import org.jreserve.gui.trianglewidget.TriangleWidget;
import org.jreserve.jrlib.triangle.Triangle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.TriangleExcludeLayer.Name=Exclusion",
    "LBL.TriangleExcludeLayer.Color.Name=Triangle Exclusion"
})
class TriangleExcludeLayer extends DefaultTriangleLayer {
    
    private final static Color BACKGROUND = DefaultColor.getBackground("triangle.exclusion");
    private final static Color FOREGROUND = DefaultColor.getForeground("triangle.exclusion");
    private int accident;
    private int development;
    
    TriangleExcludeLayer(Triangle triangle, int accident, int development) {
        super(triangle, Bundle.LBL_TriangleExcludeLayer_Name(),
                TriangleExcludeModifier.ICON, new ExcludeRenderer());
        this.accident = accident;
        this.development = development;
    }

    @Override
    public boolean rendersCell(int accident, int development) {
        return this.accident == accident &&
               this.development == development;
    }
    
    private static class ExcludeRenderer extends DefaultTriangleWidgetRenderer {

        @Override
        public Component getComponent(TriangleWidget widget, double value, int accident, int development, boolean selected) {
            super.getComponent(widget, value, accident, development, selected);
            if(!selected) {
                setBackground(BACKGROUND);
                setForeground(FOREGROUND);
            }
            return this;
        }
        
    }
}
