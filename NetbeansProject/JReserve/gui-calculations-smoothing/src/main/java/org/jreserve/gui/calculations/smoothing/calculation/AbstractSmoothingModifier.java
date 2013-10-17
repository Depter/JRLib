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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractEditableCalculationModifier;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractSmoothingModifier<T extends Triangle> 
    extends AbstractEditableCalculationModifier<T>{

    @StaticResource private final static String IMG_PATH = "org/jreserve/gui/calculations/smoothing/smoothing.png";
    protected final static Icon ICON = ImageUtilities.loadImageIcon(IMG_PATH, false);
    
    private List<SmoothingCell> cells;
    
    protected AbstractSmoothingModifier(List<SmoothingCell> cells, Class<T> clazz) {
        super(clazz);
        this.cells = new ArrayList<SmoothingCell>(cells);
    }
    
    public synchronized void setCells(List<SmoothingCell> cells) {
        Map preState = super.isChangeFired()? createState() : Collections.EMPTY_MAP;
        this.cells = new ArrayList<SmoothingCell>(cells);
        fireChange(preState);
    }
    
    protected final Map createState() {
        Map map = new HashMap();
        getState(map);
        return map;
    }
    
    @Override
    public synchronized void getState(Map state) {
        state.put(SmoothingModifierUtil.CELLS_TAG, getCells());
    }
    
    @Override
    public synchronized void loadState(Map state) {
        List<SmoothingCell> newCells = (List<SmoothingCell>) state.get(SmoothingModifierUtil.CELLS_TAG);
        setCells(newCells);
    }
    
    public synchronized List<SmoothingCell> getCells() {
        return new ArrayList<SmoothingCell>(cells);
    }
    
    @Override
    protected Displayable createDisplayable() {
        return new DisplayableSmoothing();
    }
    
    protected abstract String getDisplayName();

    protected synchronized final SmoothingCell[] getCellsAsArray() {
        return SmoothingModifierUtil.getCellsAsArray(this);
    }
    
    protected synchronized final String getCellsAsString() {
        return SmoothingModifierUtil.getCellsAsString(this);
    }
    
    protected synchronized final Element cellsToXml() {
        return SmoothingModifierUtil.cellsToXml(this);
    }
    
    public synchronized final TriangleLayer createLayer(T input) {
        return new SmoothingLayer(input, getDisplayName());
    }
    
    private class DisplayableSmoothing implements Displayable {

        @Override
        public Icon getIcon() {
            return ICON;
        }

        @Override
        public String getDisplayName() {
            return AbstractSmoothingModifier.this.getDisplayName();
        }
    }
}
