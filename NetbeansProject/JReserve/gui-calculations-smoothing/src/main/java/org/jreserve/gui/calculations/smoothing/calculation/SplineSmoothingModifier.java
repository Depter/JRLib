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
package org.jreserve.gui.calculations.smoothing.calculation;

import java.util.List;
import java.util.Map;
import org.jdom2.Element;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.jreserve.jrlib.triangle.smoothing.SplineSmoothing;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.SplineSmoothingModifier.Name=Spline Smoothing",
    "# {0} - lambda",
    "# {1} - cells",
    "LBL.SplineSmoothingModifier.Description=Spline Smoothing [lambda={0}], [{1}]"
})
public abstract class SplineSmoothingModifier<T extends Triangle> extends AbstractSmoothingModifier<T> {

    public final static String ROOT_TAG = "splineSmoothing";
    public final static String LAMBDA_TAG = "lambda";
    
    private double lambda;
    
    public SplineSmoothingModifier(List<SmoothingCell> cells, Class<T> clazz, double lambda) {
        super(cells, clazz);
        checkCells(cells);
        
        this.lambda = lambda;
    }
    
    private void checkCells(List<SmoothingCell> cells) {
        if(cells.size() < 3)
            throw new IllegalArgumentException("There must be at least two cells, but there was only "+cells.size());
    }
    
    public synchronized double getLambda() {
        return lambda;
    }
    
    public synchronized void setLambda(double lambda) {
        Map preState = createState();
        this.lambda = lambda;
        fireChange(preState);
    }
    
    @Override
    public synchronized void getState(Map state) {
        super.getState(state);
        state.put(LAMBDA_TAG, lambda);
    }
    
    @Override
    public synchronized void loadState(Map state) {
        super.setChangeFired(false);
        super.loadState(state);
        super.setChangeFired(true);
        setLambda(getDouble(state, LAMBDA_TAG));
    }

    @Override
    public synchronized void setCells(List<SmoothingCell> cells) {
        checkCells(cells);
        super.setCells(cells);
    }
    
    @Override
    protected String getDisplayName() {
        return Bundle.LBL_SplineSmoothingModifier_Name();
    }

    @Override
    public Element toXml() {
        Element root = new Element(ROOT_TAG);
        JDomUtil.addElement(root, LAMBDA_TAG, lambda);
        root.addContent(cellsToXml());
        return root;
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_SplineSmoothingModifier_Description(lambda, getCellsAsString());
    }

    protected final SplineSmoothing createSmoothing() {
        return new SplineSmoothing(getCellsAsArray(), lambda);
    }
}
