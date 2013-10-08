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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractCalculationModifier;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleModifier;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.claim.SmoothedClaimTriangle;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing;
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
    "# {2} - applied",
    "LBL.AbstractSmoothingModifier.Cell=[{0};{1},{2}]",
    "LBL.AbstractSmoothingModifier.Used=Applied",
    "LBL.AbstractSmoothingModifier.NotUsed=Not Applied"
})
abstract class AbstractSmoothingModifier extends AbstractCalculationModifier<ClaimTriangle> implements ClaimTriangleModifier {
    
    private final static String CELLS_TAG = "cells";
    private final static String CELL_TAG = "cell";
    private final static String ACCIDENT_TAG = "accident";
    private final static String DEVELOPMENT_TAG = "development";
    private final static String APPLIED_TAG = "applied";
    
    private final static String IMG_PATH = "org/jreserve/gui/calculations/icons/smoothing.png";
    final static Icon ICON = ImageUtilities.loadImageIcon(IMG_PATH, false);
    
    private List<SmoothingCell> cells;
    
    protected AbstractSmoothingModifier(List<SmoothingCell> cells) {
        super(ClaimTriangle.class);
        this.cells = new ArrayList<SmoothingCell>(cells);
    }
    
    public void setCells(List<SmoothingCell> cells) {
        this.cells = new ArrayList<SmoothingCell>(cells);
    }

    public List<SmoothingCell> getCells() {
        return new ArrayList<SmoothingCell>(cells);
    }
    
    @Override
    protected Displayable createDisplayable() {
        return new DisplayableSmoothing();
    }
    
    protected abstract String getDisplayName();

    @Override
    public ClaimTriangle createCalculation(ClaimTriangle sourceCalculation) {
        return new SmoothedClaimTriangle(sourceCalculation, createSmoothing());
    }
    
    protected abstract TriangleSmoothing createSmoothing();

    protected final SmoothingCell[] getCellsAsArray() {
        int size = cells.size();
        return cells.toArray(new SmoothingCell[size]);
    }
    
    protected final String getCellsAsString() {
        StringBuilder sb = new StringBuilder();
        for(SmoothingCell cell : cells) {
            if(sb.length() > 0)
                sb.append(", ");
            sb.append(toString(cell));
        }
        return sb.toString();
    }
    
    private String toString(SmoothingCell cell) {
        int accident = cell.getAccident()+1;
        int development = cell.getDevelopment()+1;
        String applied = cell.isApplied()? 
                Bundle.LBL_AbstractSmoothingModifier_Used() : 
                Bundle.LBL_AbstractSmoothingModifier_NotUsed();
        return Bundle.LBL_AbstractSmoothingModifier_Cell(accident, development, applied);
    }
    
    protected final Element cellsToXml() {
        Element root = new Element(CELLS_TAG);
        for(SmoothingCell cell : cells)
            root.addContent(toXml(cell));
        return root;
    }
    
    private Element toXml(SmoothingCell cell) {
        Element root = new Element(CELL_TAG);
        JDomUtil.addElement(root, ACCIDENT_TAG, cell.getAccident());
        JDomUtil.addElement(root, DEVELOPMENT_TAG, cell.getDevelopment());
        JDomUtil.addElement(root, APPLIED_TAG, cell.isApplied());
        return root;
    }
    
    protected static List<SmoothingCell> readCells(Element root) throws IOException {
        Element cse = JDomUtil.getExistingChild(root, CELLS_TAG);
        List<SmoothingCell> result = new ArrayList<SmoothingCell>();
        for(Element ce : cse.getChildren(CELL_TAG))
            readCell(ce);
        Collections.sort(result);
        return result;
    }
    
    private static SmoothingCell readCell(Element root) throws IOException {
        int accident = JDomUtil.getExistingInt(root, ACCIDENT_TAG);
        int development = JDomUtil.getExistingInt(root, DEVELOPMENT_TAG);
        boolean applied = JDomUtil.getExistingBoolean(root, APPLIED_TAG);
        return new SmoothingCell(accident, development, applied);
    }
    
    @Override
    public TriangleLayer createLayer(ClaimTriangle input) {
        return new SmoothingLayer(input, getDisplayName());
    }

    @Override
    public List<Cell> getAffectedCells() {
        return new ArrayList<Cell>(cells);
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
