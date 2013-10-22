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
import org.jreserve.jrlib.triangle.smoothing.DoubleExponentialSmoothing;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DoubleExponentialSmoothingModifier.Name=Double Exp. Smoothing",
    "# {0} - alpha",
    "# {1} - beta",
    "# {2} - cells",
    "LBL.DoubleExponentialSmoothingModifier.Description=Double Exponential Smoothing [alpha={0}, beta={1}], [{2}]",
    "LBL.DoubleExponentialSmoothingModifier.ProgressName=Double Exponential Smoothing"
})
public abstract class DoubleExponentialSmoothingModifier<T extends Triangle> 
    extends AbstractSmoothingModifier<T> {

    public final static String ROOT_TAG = "doubleExponentialSmoothing";
    public final static String ALPHA_TAG = "alpha";
    public final static String BETA_TAG = "beta";
    
    private double alpha;
    private double beta;
    
    public DoubleExponentialSmoothingModifier(List<SmoothingCell> cells, Class<T> clazz, double alpha, double beta) {
        super(cells, clazz);
        checkCells(cells);
        
        checkAlpha(alpha);
        this.alpha = alpha;
        
        checkBeta(beta);
        this.beta = beta;
    }
    
    private void checkCells(List<SmoothingCell> cells) {
        if(cells.size() < 2)
            throw new IllegalArgumentException("There must be at least two cells, but there was only "+cells.size());
    }
    
    private void checkAlpha(double alpha) {
        if(alpha < 0d || alpha > 1d)
            throw new IllegalArgumentException("Alpha must be within [0;1] but was "+alpha);
    }
    
    private void checkBeta(double beta) {
        if(beta < 0d || beta > 1d)
            throw new IllegalArgumentException("Beta must be within [0;1] but was "+beta);
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
        state.put(BETA_TAG, beta);
    }
    
    @Override
    public synchronized void loadState(Map state) {
        super.setChangeFired(false);
        
        super.loadState(state);
        
        double a = getDouble(state, ALPHA_TAG);
        setAlpha(a);
        
        super.setChangeFired(true);
        
        double b = getDouble(state, BETA_TAG);
        setAlpha(b);
    }
    
    public synchronized double getBeta() {
        return beta;
    }
    
    public synchronized void setBeta(double beta) {
        checkBeta(beta);
        Map preState = createState();
        this.beta = beta;
        fireChange(preState);
    }

    @Override
    public synchronized void setCells(List<SmoothingCell> cells) {
        checkCells(cells);
        super.setCells(cells);
    }
    
    @Override
    protected String getDisplayName() {
        return Bundle.LBL_DoubleExponentialSmoothingModifier_Name();
    }

    @Override
    public Element toXml() {
        Element root = new Element(ROOT_TAG);
        JDomUtil.addElement(root, ALPHA_TAG, alpha);
        JDomUtil.addElement(root, BETA_TAG, beta);
        root.addContent(cellsToXml());
        return root;
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_DoubleExponentialSmoothingModifier_Description(alpha, beta, getCellsAsString());
    }

    protected final DoubleExponentialSmoothing createSmoothing() {
        return new DoubleExponentialSmoothing(getCellsAsArray(), alpha, beta);
    }
    
    protected final void updateFrom(DoubleExponentialSmoothingModifier<T> modifier) {
        UpdateTask task = new UpdateTask(this, modifier.getAlpha(), modifier.getBeta());
        TaskUtil.execute(task, null, Bundle.LBL_DoubleExponentialSmoothingModifier_ProgressName());
    }
    
    private static class UpdateTask implements Callable<Void> {
        
        private final DoubleExponentialSmoothingModifier desm;
        private final double alpha;
        private final double beta;
        
        private UpdateTask(DoubleExponentialSmoothingModifier desm, double alpha, double beta) {
            this.desm = desm;
            this.alpha = alpha;
            this.beta = beta;
        }
        
        @Override
        public Void call() throws Exception {
            synchronized(desm) {
                desm.setChangeFired(false);
                desm.setAlpha(alpha);
                desm.setChangeFired(true);
                desm.setBeta(beta);
                return null;
            }
        }    
    }
}
