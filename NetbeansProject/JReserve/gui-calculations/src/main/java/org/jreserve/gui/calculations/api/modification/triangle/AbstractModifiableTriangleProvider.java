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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.CalculationDataObject;
import org.jreserve.gui.calculations.api.modification.AbstractModifiableCalculationProvider;
import org.jreserve.gui.trianglewidget.DefaultTriangleLayer;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.jrlib.triangle.Triangle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.AbstractModifiableTriangleProvider.Layer.Base=Input"
})
public abstract class AbstractModifiableTriangleProvider<T extends Triangle>
    extends AbstractModifiableCalculationProvider<T> {

    public AbstractModifiableTriangleProvider(CalculationDataObject obj) {
        super(obj);
    }

    public AbstractModifiableTriangleProvider(CalculationDataObject obj, Element parent, String category) throws Exception {
        super(obj, parent, category);
    }

    public List<TriangleLayer> createLayers() {
        synchronized(lock) {
            if(!calculationExists()) {
                recalculate();
                T dummy = createDummyCalculation();
                return Collections.singletonList(createBaseLayer(dummy));
            }
            
            int count = super.getModificationCount() + 1;
            List<TriangleLayer> result = new ArrayList<TriangleLayer>(count);
            
            T base = super.getCalculation(0);
            result.add(createBaseLayer(base));
            
            for(int i=1; i<count; i++) {
                TriangleModifier modifier = (TriangleModifier) getModificationAt(i-1);
                Triangle layer = super.getCalculation(i);
                result.add(modifier.createLayer(layer));
            }
            return result;
        }
    }
    
    private TriangleLayer createBaseLayer(T input) {
        String name = Bundle.LBL_AbstractModifiableTriangleProvider_Layer_Base();
        return new DefaultTriangleLayer(input, name);
    }
    
}
