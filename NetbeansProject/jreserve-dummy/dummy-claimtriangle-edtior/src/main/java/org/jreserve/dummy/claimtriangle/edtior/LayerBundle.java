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

package org.jreserve.dummy.claimtriangle.edtior;

import java.util.List;
import org.jreserve.gui.triangletable.TriangleLayer;
import org.jreserve.gui.triangletable.trianglemodel.TitleModel;
import org.jreserve.jrlib.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LayerBundle {
    
    private List<TriangleLayer> layers;
    private TitleModel accidentTitle;
    private TitleModel developmentTitle;
    
    LayerBundle(List<TriangleLayer> layers, TitleModel accidentTitle, TitleModel developmentTitle) {
        this.layers = layers;
        this.accidentTitle = accidentTitle;
        this.developmentTitle = developmentTitle;
    }
    
    public List<TriangleLayer> getLayers() {
        return layers;
    }
    
    public TitleModel getAccidentTitles() {
        return accidentTitle;
    }
    
    public TitleModel getDevelopmentTitle() {
        return developmentTitle;
    }
    
    public Triangle getTriangle() {
        return layers.isEmpty()? null : layers.get(layers.size()-1).getTriangle();
    }
}
