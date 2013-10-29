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
package org.jreserve.gui.calculations.claimtriangle.editor;

import java.util.List;
import org.jreserve.gui.calculations.api.edit.TriangleLayerEditorPanel;
import org.jreserve.gui.calculations.api.modification.triangle.TriangleModifier;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleCalculationImpl;
import org.jreserve.gui.calculations.claimtriangle.modifications.ClaimTriangleCorrectionModifier;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionReferences({
    @ActionReference(
        id = @ActionID(category = "Edit", id = "org.jreserve.gui.calculations.actions.ExcludeAction"),
        path = ClaimTriangleLayerEditorPanel.POPUP_PATH,
        position = 100
    ),
    @ActionReference(
        id = @ActionID(category = "Edit", id = "org.jreserve.gui.calculations.actions.SmoothAction"),
        path = ClaimTriangleLayerEditorPanel.POPUP_PATH,
        position = 200
    )
})
class ClaimTriangleLayerEditorPanel extends TriangleLayerEditorPanel<ClaimTriangle, ClaimTriangleCalculationImpl>{
    
    final static String POPUP_PATH = "JReserve/ClaimTriangle/LayerEditor/Popup";

    ClaimTriangleLayerEditorPanel(ClaimTriangleCalculationImpl calculation) {
        super(calculation);
    }

    @Override
    protected TriangleGeometry getGeometry(ClaimTriangleCalculationImpl calculation) {
        return calculation.getGeometry();
    }

    @Override
    protected List<TriangleLayer> createLayers(ClaimTriangleCalculationImpl calculation) {
        return calculation.createLayers();
    }

    @Override
    protected TriangleModifier<ClaimTriangle> createEdit(int accident, int development, double value) {
        return new ClaimTriangleCorrectionModifier(accident, development, value);
    }

    @Override
    protected String getPopUpPath() {
        return POPUP_PATH;
    }
}
