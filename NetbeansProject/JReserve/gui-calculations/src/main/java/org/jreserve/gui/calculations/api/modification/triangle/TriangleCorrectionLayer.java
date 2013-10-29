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
import javax.swing.Icon;
import org.jreserve.gui.calculations.api.modification.DefaultColor;
import org.jreserve.gui.trianglewidget.DefaultTriangleLayer;
import org.jreserve.gui.trianglewidget.DefaultTriangleWidgetRenderer;
import org.jreserve.gui.trianglewidget.TriangleWidget;
import org.jreserve.jrlib.triangle.Triangle;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.TriangleCorrectionLayer.Name=Correction",
    "LBL.TriangleCorrectionLayer.Color.Name=Triangle Correction"
})
class TriangleCorrectionLayer extends DefaultTriangleLayer {
    
    @StaticResource private final static String IMG_PATH = "org/jreserve/gui/calculations/icons/correction.png"; //NOI18
    private final static Icon ICON = ImageUtilities.loadImageIcon(IMG_PATH, false);
    
    private final static Color BACKGROUND = DefaultColor.getBackground("triangle.correction");
    private final static Color FOREGROUND = DefaultColor.getForeground("triangle.correction");
    private int accident;
    private int development;
    
    TriangleCorrectionLayer(Triangle triangle, int accident, int development) {
        super(triangle, Bundle.LBL_TriangleCorrectionLayer_Name(),
                ICON, new CorrectionRenderer());
        this.accident = accident;
        this.development = development;
    }

    @Override
    public boolean rendersCell(int accident, int development) {
        return this.accident == accident &&
               this.development == development;
    }
    
    private static class CorrectionRenderer extends DefaultTriangleWidgetRenderer {

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
