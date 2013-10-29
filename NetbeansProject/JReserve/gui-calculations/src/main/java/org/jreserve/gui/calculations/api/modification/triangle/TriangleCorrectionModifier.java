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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.modification.AbstractEditableCalculationModifier;
import org.jreserve.gui.calculations.api.modification.ModifiableCalculationProvider;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.Cell;
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
    "# {0} - accident",
    "# {1} - development",
    "# {2} - value",
    "LBL.TriangleCorrectionModifier.Description=Correction [{0}; {1}] = {2}",
    "LBL.TriangleCorrectionModifier.Name=Correction"
})
public abstract class TriangleCorrectionModifier<T extends Triangle> 
    extends AbstractEditableCalculationModifier<T> 
    implements TriangleModifier<T> {
    
    public final static String ROOT_ELEMENT = "triangleCorrection";
    public final static String ACCIDENT_ELEMENT = "accident";
    public final static String DEVELOPMENT_ELEMENT = "development";
    public final static String VALUE_ELEMENT = "value";
    
    @StaticResource private final static String IMG_PATH = "org/jreserve/gui/calculations/icons/correction.png";
    public final static Icon ICON = ImageUtilities.loadImageIcon(IMG_PATH, false);
    
    private int accident;
    private int development;
    private double value;

    public TriangleCorrectionModifier(Class<T> clazz, int accident, int development, double value) {
        super(clazz);
        this.accident = accident;
        this.development = development;
        this.value = value;
    }
    
    public int getAccident() {
        return accident;
    }
    
    public int getDevelopment() {
        return development;
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
        JDomUtil.addElement(root, DEVELOPMENT_ELEMENT, development);
        JDomUtil.addElement(root, VALUE_ELEMENT, value);
        return root;
    }

    @Override
    public List<Cell> getAffectedCells() {
        return Collections.singletonList(new Cell(accident, development));
    }

    @Override
    protected Displayable createDisplayable() {
        return new CorrectionDisplayable();
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_TriangleCorrectionModifier_Description(
                accident+1, development+1, value);
    }

    protected String getDisplayName() {
        return Bundle.LBL_TriangleCorrectionModifier_Name();
    }
    
    protected boolean isEditInputCummulated() {
        return true;
    }

    @Override
    public void edit(ModifiableCalculationProvider<T> calculation) {
        T source = super.getSource(calculation);
        TriangleCorrectionEditDialog.editCorrection(this, source);
    }

    public TriangleLayer createLayer(T input) {
        return new TriangleCorrectionLayer(input, accident, development);
    }
    
    private class CorrectionDisplayable implements Displayable {
        
        @Override
        public Icon getIcon() {
            return ICON;
        }

        @Override
        public String getDisplayName() {
            return TriangleCorrectionModifier.this.getDisplayName();
        }
    }
}
