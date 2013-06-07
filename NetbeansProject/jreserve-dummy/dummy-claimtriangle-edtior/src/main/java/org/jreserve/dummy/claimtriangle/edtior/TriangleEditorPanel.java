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

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.table.TableCellRenderer;
import org.jreserve.gui.triangletable.TriangleLayer;
import org.jreserve.gui.triangletable.TriangleTable;
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
class TriangleEditorPanel extends JPanel {
    
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
    
    private static List<TriangleLayer> createTriangle() {
        
        DataLayer data = new DataLayer();
        CorrectionLayer correction = new CorrectionLayer(data.triangle);
        SmoothingLayer smoothing = new SmoothingLayer(correction.triangle);
        ExcludeLayer exclude = new ExcludeLayer(smoothing.triangle);
        
        List<TriangleLayer> layers = new ArrayList<TriangleLayer>(4);
        layers.add(data);
        layers.add(correction);
        layers.add(smoothing);
        layers.add(exclude);
        
        return layers;
    }
    
    private List<TriangleLayer> layers = createTriangle();
    
    public TriangleEditorPanel() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        add(createToolBar(), BorderLayout.NORTH);
        
        TriangleTable table = new TriangleTable(layers);
        add(table, BorderLayout.CENTER);
        
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private JPanel createToolBar() {
        return new JPanel();
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
        public TableCellRenderer getCellRenderer() {
            return null;
        }    
    }
    
    private static class CorrectionLayer implements TriangleLayer {

        private ClaimTriangle triangle;
        
        private CorrectionLayer(ClaimTriangle triangle) {
            this.triangle = new ClaimTriangleCorrection(triangle, 0, 3, 5600000);
        }
        
        @Override
        public String getDisplayName() {
            return "Correction [1, 4]";
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
        public TableCellRenderer getCellRenderer() {
            return null;
        }    
    }
    
    private static class SmoothingLayer implements TriangleLayer {

        private ClaimTriangle triangle;
        
        private SmoothingLayer(ClaimTriangle triangle) {
            TriangleSmoothing smoothing = new ExponentialSmoothing(
                    new SmoothingCell[] {
                        new SmoothingCell(3, 0, false),
                        new SmoothingCell(4, 0, false),
                        new SmoothingCell(5, 0, true)
                    }
                    , 0.5);

            this.triangle = new SmoothedClaimTriangle(triangle, smoothing);
        }
        
        @Override
        public String getDisplayName() {
            return "Smoothing_1";
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
        public TableCellRenderer getCellRenderer() {
            return null;
        }    
    }
    
    private static class ExcludeLayer implements TriangleLayer {

        private ClaimTriangle triangle;
        
        private ExcludeLayer(ClaimTriangle triangle) {
            this.triangle = new ClaimTriangleCorrection(triangle, 2, 2, Double.NaN);
        }
        
        @Override
        public String getDisplayName() {
            return "Exclusion [2, 2]";
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
        public TableCellRenderer getCellRenderer() {
            return null;
        }    
    }
}
