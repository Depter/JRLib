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

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.gui.triangletable.TriangleLayer;
import org.jreserve.gui.triangletable.trianglemodel.ArrayTitleModel;
import org.jreserve.gui.triangletable.trianglemodel.IndexTitleModel;
import org.jreserve.gui.triangletable.trianglemodel.TitleModel;
import org.jreserve.gui.triangletable.trianglemodel.TriangleModel;
import org.jreserve.gui.triangletable.widget.DefaultTriangleWidgetRenderer;
import org.jreserve.gui.triangletable.widget.TriangleWidget;
import org.jreserve.gui.triangletable.widget.TriangleWidgetRenderer;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangleCorrection;
import org.jreserve.jrlib.triangle.claim.CummulatedClaimTriangle;
import org.jreserve.jrlib.triangle.claim.InputClaimTriangle;
import org.jreserve.jrlib.triangle.claim.SmoothedClaimTriangle;
import org.jreserve.jrlib.triangle.smoothing.ExponentialSmoothing;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleFactory {

    
    private final static double[][] APC_PAID = {
        {4426765, 992330 , 88952 , 13240 , 38622 , 26720, 36818, 10750},
        {4388958, 984169 , 60162 , 35004 , 75768 , 23890, 572},
        {5280130, 1239396, 76122 , 110189, 112895, 11751},
        {5445384, 1164234, 171583, 16427 , 6451},
        {5612138, 1837950, 155863, 127146},
        {6593299, 1592418, 74189},
        {6603091, 1659748},
        {7194587}
    };
    private final static TitleModel ACCIDENT_TITLES = new ArrayTitleModel(
            "1997-01", "1998-01", "1999-01", "2000-01", 
            "2001-01", "2002-01", "2003-01", "2004-01");
    private final static TitleModel DEVELOPMENT_TITLES = new IndexTitleModel();
    
    static LayerBundle createTriangle() {
        
        DataLayer data = new DataLayer();
        CorrectionLayer correction = new CorrectionLayer(data.triangle);
        SmoothingLayer smoothing = new SmoothingLayer(correction.triangle);
        ExcludeLayer exclude = new ExcludeLayer(smoothing.triangle);
        
        List<TriangleLayer> layers = new ArrayList<TriangleLayer>(4);
        layers.add(data);
        layers.add(correction);
        layers.add(smoothing);
        layers.add(exclude);
        
        return new LayerBundle(layers, ACCIDENT_TITLES, DEVELOPMENT_TITLES);
    }
    
    
    private static class DataLayer implements TriangleLayer {

        private ClaimTriangle triangle = new CummulatedClaimTriangle(new InputClaimTriangle(APC_PAID));
        
        @Override
        public String getDisplayName() {
            return "Source";
        }

        @Override
        public Triangle getTriangle() {
            return triangle;
        }

        @Override
        public boolean rendersCell(int accident, int development) {
            return false;
        }

        @Override
        public DefaultTriangleWidgetRenderer getCellRenderer() {
            return null;
        }    
    }
    
    private static class CorrectionLayer implements TriangleLayer {

        private ClaimTriangle triangle;
        private DefaultTriangleWidgetRenderer renderer = new DefaultTriangleWidgetRenderer();
        
        private CorrectionLayer(ClaimTriangle triangle) {
            renderer.setOpaque(true);
            renderer.setBackground(new Color(255, 153, 0));
            this.triangle = new ClaimTriangleCorrection(triangle, 0, 3, 5600000);
        }
        
        @Override
        public String getDisplayName() {
            return "Correction";
        }

        @Override
        public Triangle getTriangle() {
            return triangle;
        }

        @Override
        public boolean rendersCell(int accident, int development) {
            return accident==0 && development==3;
        }

        @Override
        public DefaultTriangleWidgetRenderer getCellRenderer() {
            return renderer;
        }    
    }
    
    private static class SmoothingLayer implements TriangleLayer {

        private ClaimTriangle triangle;
        private SmoothingRenderer renderer;
        
        private SmoothingLayer(ClaimTriangle triangle) {
            TriangleSmoothing smoothing = new ExponentialSmoothing(
                    new SmoothingCell[] {
                        new SmoothingCell(3, 0, false),
                        new SmoothingCell(4, 0, false),
                        new SmoothingCell(5, 0, true)
                    }
                    , 0.5);
            this.renderer = new SmoothingRenderer(smoothing);
            this.triangle = new SmoothedClaimTriangle(triangle, smoothing);
        }
        
        @Override
        public String getDisplayName() {
            return "Smoothing";
        }

        @Override
        public Triangle getTriangle() {
            return triangle;
        }

        @Override
        public boolean rendersCell(int accident, int development) {
            return renderer.containsCell(accident, development);
        }

        @Override
        public TriangleWidgetRenderer getCellRenderer() {
            return renderer;
        }    
    }
    
    private static class SmoothingRenderer extends DefaultTriangleWidgetRenderer {
        
        private SmoothingCell[] cells;
        private Color applied = new Color(0, 102, 255);
        private Color notApplied = new Color(102, 204, 255);
        
        private SmoothingRenderer(TriangleSmoothing smoothing) {
            this.cells = smoothing.getSmoothingCells();
            setOpaque(true);
        }
        
        boolean containsCell(int accident, int development) {
            for(SmoothingCell cell : cells)
                if(cell.equals(accident, development))
                    return true;
            return false;
        }

        @Override
        public Component getComponent(TriangleWidget widget, double value, int row, int column, boolean selected) {
            boolean isApplied = isApplied(widget, row, column);
            super.getComponent(widget, value, row, column, selected);
            setBackground(isApplied? applied : notApplied);
            return this;
        }
        
        private boolean isApplied(TriangleWidget widget, int row, int column) {
            TriangleModel model = widget.getModel();
            int accident = model.getAccidentIndex(row, column);
            int development = model.getDevelopmentIndex(row, column);
            for(SmoothingCell cell : cells)
                if(cell.equals(accident, development))
                    return cell.isApplied();
            return false;
        }
    }
    
    private static class ExcludeLayer implements TriangleLayer {

        private ClaimTriangle triangle;
        private DefaultTriangleWidgetRenderer renderer = new DefaultTriangleWidgetRenderer();
        
        private ExcludeLayer(ClaimTriangle triangle) {
            renderer.setOpaque(true);
            renderer.setBackground(Color.red);
            this.triangle = new ClaimTriangleCorrection(triangle, 2, 2, Double.NaN);
        }
        
        @Override
        public String getDisplayName() {
            return "Exclusion";
        }

        @Override
        public Triangle getTriangle() {
            return triangle;
        }

        @Override
        public boolean rendersCell(int accident, int development) {
            return accident==2 && development==2;
        }

        @Override
        public DefaultTriangleWidgetRenderer getCellRenderer() {
            return renderer;
        }    
    }
}
