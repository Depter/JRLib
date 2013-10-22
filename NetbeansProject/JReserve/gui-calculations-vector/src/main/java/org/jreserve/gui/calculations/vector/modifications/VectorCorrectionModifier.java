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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractEditableCalculationModifier;
import org.jreserve.gui.calculations.api.ModifiableCalculationProvider;
import org.jreserve.gui.calculations.vector.VectorModifier;
import org.jreserve.gui.calculations.vector.impl.VectorGeometryUtil;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.vector.Vector;
import org.jreserve.jrlib.vector.VectorCorrection;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - accident",
    "# {1} - value",
    "LBL.VectorCorrectionModifier.Description=Correction [{0}] = {1}",
    "LBL.VectorCorrectionModifier.Name=Correction"
})
public class VectorCorrectionModifier 
    extends AbstractEditableCalculationModifier<Vector>
    implements VectorModifier {
    
    private final static String IMG_PATH = "org/jreserve/gui/calculations/icons/correction.png";    //NOI18
    public final static Icon ICON = ImageUtilities.loadImageIcon(IMG_PATH, false);
    
    public final static String ROOT_ELEMENT = "vectorCorrection";
    public final static String ACCIDENT_ELEMENT = "accident";
    public final static String VALUE_ELEMENT = "value";

    private int accident;
    private double value;
    
    public VectorCorrectionModifier(int accident, double value) {
        super(Vector.class);
        this.accident = accident;
        this.value = value;
    }
    
    public int getAccident() {
        return accident;
    }
    
    public synchronized double getValue() {
        return value;
    }
    
    public synchronized void setValue(double value) {
        Map preState = getState() ;
        this.value = value;
        fireChange(preState);
    }

    @Override
    public void getState(Map state) {
        state.put(VALUE_ELEMENT, value);
    }
    
    private Map getState() {
        return Collections.singletonMap(VALUE_ELEMENT, this.value);
    }
    
    @Override
    public void loadState(Map state) {
        setValue(getInt(state, VALUE_ELEMENT));
    }

    @Override
    public Element toXml() {
        Element root = new Element(ROOT_ELEMENT);
        JDomUtil.addElement(root, ACCIDENT_ELEMENT, accident);
        JDomUtil.addElement(root, VALUE_ELEMENT, value);
        return root;
    }

    @Override
    protected Displayable createDisplayable() {
        return new CorrectionDisplayable();
    }

    @Override
    public void edit(ModifiableCalculationProvider<Vector> calculation) {
        Vector source = super.getSource(calculation);
        VectorCorrectionEditDialog.editCorrection(this, source);
    }

    @Override
    public Vector createCalculation(Vector sourceCalculation) {
        return new VectorCorrection(sourceCalculation, accident, value);
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_VectorCorrectionModifier_Description(accident, value);
    }

    @Override
    public TriangleLayer createLayer(Vector input) {
        Triangle triangle = VectorGeometryUtil.toTriangle(input);
        return new CorrectionLayer(triangle, accident);
    }

    @Override
    public List<Cell> getAffectedCells() {
        return Collections.singletonList(new Cell(accident, 0));
    }

    private class CorrectionDisplayable implements Displayable {
        
        @Override
        public Icon getIcon() {
            return ICON;
        }

        @Override
        public String getDisplayName() {
            return Bundle.LBL_VectorCorrectionModifier_Name();
        }
    }

}
