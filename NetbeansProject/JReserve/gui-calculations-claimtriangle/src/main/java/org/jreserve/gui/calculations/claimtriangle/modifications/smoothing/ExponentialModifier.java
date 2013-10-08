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
package org.jreserve.gui.calculations.claimtriangle.modifications.smoothing;

import java.util.List;
import org.jdom2.Element;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.smoothing.ExponentialSmoothing;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ExponentialModifier.Name=Exponential Smoothing",
    "# {0} - alpha",
    "# {1} - cells",
    "LBL.ExponentialModifier.Description=Exponential Smoothing [alpha={0}], [{1}]"
})
public class ExponentialModifier extends AbstractSmoothingModifier {
    
    final static String ROOT_TAG = "exponentialSmoothing";
    final static String ALPHA_TAG = "alpha";
    
    private double alpha;
    
    public ExponentialModifier(List<SmoothingCell> cells, double alpha) {
        super(cells);
        if(cells.size() < 2)
            throw new IllegalArgumentException("There must be at least to cells, but there was only "+cells.size());
        if(alpha < 0d || alpha > 1d)
            throw new IllegalArgumentException("Alpha must be within [0;1] but was "+alpha);
        this.alpha = alpha;
    }
    
    public void setAlpha(double alpha) {
        if(alpha < 0d || alpha > 1d)
            throw new IllegalArgumentException("Alpha must be within [0;1] but was "+alpha);
        this.alpha = alpha;
        fireChange();
    }
    
    public double getAlpha() {
        return alpha;
    }
    
    @Override
    public void setCells(List<SmoothingCell> cells) {
        if(cells.size() < 2)
            throw new IllegalArgumentException("There must be at least to cells, but there was only "+cells.size());
        super.setCells(cells);
    }

    @Override
    protected String getDisplayName() {
        return Bundle.LBL_ExponentialModifier_Name();
    }
    
    @Override
    protected TriangleSmoothing createSmoothing() {
        return new ExponentialSmoothing(getCellsAsArray(), alpha);
    }

    @Override
    public Element toXml() {
        Element root = new Element(ROOT_TAG);
        JDomUtil.addElement(root, ALPHA_TAG, alpha);
        root.addContent(cellsToXml());
        return root;
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_ExponentialModifier_Description(alpha, getCellsAsString());
    }
}
