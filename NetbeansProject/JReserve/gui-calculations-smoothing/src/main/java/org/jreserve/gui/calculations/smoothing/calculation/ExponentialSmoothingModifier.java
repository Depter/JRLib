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
import java.util.concurrent.Callable;
import org.jdom2.Element;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.smoothing.ExponentialSmoothing;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ExponentialSmoothingModifier.Name=Exponential Smoothing",
    "# {0} - alpha",
    "# {1} - cells",
    "LBL.ExponentialSmoothingModifier.Description=Exponential Smoothing [alpha={0}], [{1}]",
    "LBL.ExponentialSmoothingModifier.ProgressName=Exponential Smoothing"
})
public abstract class ExponentialSmoothingModifier<T extends Triangle> 
    extends AbstractSmoothingModifier<T> {

    public final static String ROOT_TAG = "exponentialSmoothing";
    public final static String ALPHA_TAG = "alpha";
    
    private double alpha;
    
    public ExponentialSmoothingModifier(List<SmoothingCell> cells, Class<T> clazz, double alpha) {
        super(cells, clazz);
        checkCells(cells);
        
        checkAlpha(alpha);
        this.alpha = alpha;
    }
    
    private void checkCells(List<SmoothingCell> cells) {
        if(cells.size() < 2)
            throw new IllegalArgumentException("There must be at least two cells, but there was only "+cells.size());
    }
    
    private void checkAlpha(double alpha) {
        if(alpha < 0d || alpha > 1d)
            throw new IllegalArgumentException("Alpha must be within [0;1] but was "+alpha);
    }
    
    public synchronized double getAlpha() {
        return alpha;
    }
    
    public synchronized void setAlpha(double alpha) {
        checkAlpha(alpha);
        Map preState = createState();
        this.alpha = alpha;
        fireChange(preState);
    }
    
    @Override
    public synchronized void getState(Map state) {
        super.getState(state);
        state.put(ALPHA_TAG, alpha);
    }
    
    @Override
    public synchronized void loadState(Map state) {
        super.setChangeFired(false);
        super.loadState(state);
        super.setChangeFired(true);
        double a = getDouble(state, ALPHA_TAG);
        setAlpha(a);
    }

    @Override
    public synchronized void setCells(List<SmoothingCell> cells) {
        checkCells(cells);
        super.setCells(cells);
    }
    
    @Override
    protected String getDisplayName() {
        return Bundle.LBL_ExponentialSmoothingModifier_Name();
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
        return Bundle.LBL_ExponentialSmoothingModifier_Description(alpha, getCellsAsString());
    }

    protected final ExponentialSmoothing createSmoothing() {
        return new ExponentialSmoothing(getCellsAsArray(), alpha);
    }
    
    protected final void updateFrom(ExponentialSmoothingModifier<T> modifier) {
        UpdateTask task = new UpdateTask(this, modifier.getAlpha());
        TaskUtil.execute(task, null, Bundle.LBL_ExponentialSmoothingModifier_ProgressName());
    }
    
    private static class UpdateTask implements Callable<Void> {
        
        private final ExponentialSmoothingModifier esm;
        private final double alpha;

        private UpdateTask(ExponentialSmoothingModifier esm, double alpha) {
            this.esm = esm;
            this.alpha = alpha;
        }
        
        @Override
        public Void call() throws Exception {
            synchronized(esm) {
                esm.setAlpha(alpha);
                return null;
            }
        }    
    }
}
