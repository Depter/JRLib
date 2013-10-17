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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jdom2.Element;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractRegressionSmoothingModifier<T extends Triangle> extends AbstractSmoothingModifier<T> {

    public final static String INTERCEPT_TAG = "hasIntercept";
    
    private boolean hasIntercept;
    
    public AbstractRegressionSmoothingModifier(List<SmoothingCell> cells, Class<T> clazz, boolean hasIntercept) {
        super(cells, clazz);
        checkCells(cells);
        
        this.hasIntercept = hasIntercept;
    }
    
    private void checkCells(List<SmoothingCell> cells) {
        if(cells.size() < 2)
            throw new IllegalArgumentException("There must be at least two cells, but there was only "+cells.size());
    }
    
    public synchronized boolean hasIntercept() {
        return hasIntercept;
    }
    
    public synchronized void setIntercept(boolean hasIntercept) {
        Map preState = isChangeFired()? createState() : Collections.EMPTY_MAP;
        this.hasIntercept = hasIntercept;
        fireChange(preState);
    }
    
    @Override
    public synchronized void getState(Map state) {
        super.getState(state);
        state.put(INTERCEPT_TAG, hasIntercept);
    }
    
    @Override
    public synchronized void loadState(Map state) {
        boolean fc = isChangeFired();
        super.setChangeFired(false);
        super.loadState(state);
        super.setChangeFired(fc);
        setIntercept(getBoolean(state, INTERCEPT_TAG));
    }

    @Override
    public synchronized void setCells(List<SmoothingCell> cells) {
        checkCells(cells);
        super.setCells(cells);
    }

    @Override
    public Element toXml() {
        Element root = new Element(getRootTag());
        JDomUtil.addElement(root, INTERCEPT_TAG, hasIntercept);
        root.addContent(cellsToXml());
        return root;
    }
    
    protected abstract String getRootTag();
    
}
