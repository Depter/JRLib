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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jdom2.Element;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.jreserve.jrlib.vector.smoothing.SmoothingIndex;
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
public class SmoothingModifierUtil {
    
    public final static String CELLS_TAG = "cells";
    public final static String CELL_TAG = "cell";
    public final static String ACCIDENT_TAG = "accident";
    public final static String DEVELOPMENT_TAG = "development";
    public final static String APPLIED_TAG = "applied";

    public static Element cellsToXml(AbstractSmoothingModifier modifier) {
        List<SmoothingCell> cells = modifier.getCells();
        Element root = new Element(CELLS_TAG);
        for(SmoothingCell cell : cells)
            root.addContent(toXml(cell));
        return root;
    }
    
    private static Element toXml(SmoothingCell cell) {
        Element root = new Element(CELL_TAG);
        JDomUtil.addElement(root, ACCIDENT_TAG, cell.getAccident());
        JDomUtil.addElement(root, DEVELOPMENT_TAG, cell.getDevelopment());
        JDomUtil.addElement(root, APPLIED_TAG, cell.isApplied());
        return root;
    }
    
    public static String getCellsAsString(AbstractSmoothingModifier modifier) {
        List<SmoothingCell> cells = modifier.getCells();
        StringBuilder sb = new StringBuilder();
        for(SmoothingCell cell : cells) {
            if(sb.length() > 0)
                sb.append(", ");
            sb.append(toString(cell));
        }
        return sb.toString();
    }
    
    private static String toString(SmoothingCell cell) {
        int accident = cell.getAccident()+1;
        int development = cell.getDevelopment()+1;
        String applied = cell.isApplied()? 
                Bundle.LBL_AbstractSmoothingModifier_Used() : 
                Bundle.LBL_AbstractSmoothingModifier_NotUsed();
        return Bundle.LBL_AbstractSmoothingModifier_Cell(accident, development, applied);
    }
    
    public static SmoothingCell[] getCellsAsArray(AbstractSmoothingModifier modifier) {
        List<SmoothingCell> cells = modifier.getCells();
        int size = cells.size();
        return cells.toArray(new SmoothingCell[size]);
    }
    
    public static List<SmoothingCell> readCells(Element root) throws IOException {
        Element cse = JDomUtil.getExistingChild(root, CELLS_TAG);
        List<SmoothingCell> result = new ArrayList<SmoothingCell>();
        for(Element ce : cse.getChildren(CELL_TAG))
            result.add(readCell(ce));
        Collections.sort(result);
        return result;
    }
    
    private static SmoothingCell readCell(Element root) throws IOException {
        int accident = JDomUtil.getExistingInt(root, ACCIDENT_TAG);
        int development = JDomUtil.getExistingInt(root, DEVELOPMENT_TAG);
        boolean applied = JDomUtil.getExistingBoolean(root, APPLIED_TAG);
        return new SmoothingCell(accident, development, applied);
    }
    
    public static SmoothingIndex[] getCellsAsIndices(AbstractSmoothingModifier modifier) {
        List<SmoothingCell> cells = modifier.getCells();
        int size = cells.size();
        SmoothingIndex[] indices = new SmoothingIndex[size];
        for(int i=0; i<size; i++)
            indices[i] = getCellAsIndex(cells.get(i));
        return indices;
    }
    
    private static SmoothingIndex getCellAsIndex(SmoothingCell cell) {
        int a = cell.getAccident();
        int d = cell.getDevelopment();
        return new SmoothingIndex(a<0? d : a, cell.isApplied());
    }
    
    private SmoothingModifierUtil() {}
}
