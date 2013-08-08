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
package org.jreserve.gui.data.inport;

import java.util.Collections;
import java.util.List;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.gui.trianglewidget.DefaultTriangleWidgetRenderer;
import org.jreserve.gui.trianglewidget.TriangleWidgetRenderer;
import org.jreserve.gui.trianglewidget.model.DevelopmentTriangleModel;
import org.jreserve.gui.trianglewidget.model.IndexTitleModel;
import org.jreserve.gui.trianglewidget.model.MonthDateTitleModel;
import org.jreserve.gui.trianglewidget.model.TitleModel;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.trianglewidget.model.TriangleModel;
import org.jreserve.jrlib.gui.data.EntryTriangle;
import org.jreserve.jrlib.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleUtil {

    private EntryTriangle triangle;
    private TriangleModel triangleModel;
    private DataEntryTriangleLayer layer;
    
    TriangleUtil(List<DataEntry> entries) {
        triangle = new EntryTriangle(entries);
        TitleModel hTitles = new IndexTitleModel();
        TitleModel vTitles = new MonthDateTitleModel(triangle.getAccidentDates());
        triangleModel = new DevelopmentTriangleModel(triangle, hTitles, vTitles);
        layer = new DataEntryTriangleLayer();
    }
    
    TriangleModel getTriangleModel() {
        return triangleModel;
    }
    
    List<TriangleLayer> getLayers() {
        return Collections.singletonList((TriangleLayer) layer);
    }
    
    TriangleLayer getTriangleLayer() {
        return layer;
    }
    
    DataEntry getEntry(int row, int column) {
        int accident = triangleModel.getAccidentIndex(row, column);
        int development = triangleModel.getDevelopmentIndex(row, column);
        return triangle.getEntry(accident, development);
    }
    
    void setRenderer(TriangleWidgetRenderer renderer) {
        layer.renderer = renderer;
    }
    
    private class DataEntryTriangleLayer implements TriangleLayer {
        
        private TriangleWidgetRenderer renderer = new DefaultTriangleWidgetRenderer();
        
        @Override
        public String getDisplayName() {
            return "import data";
        }

        @Override
        public Triangle getTriangle() {
            return triangle;
        }

        @Override
        public boolean rendersCell(int accident, int development) {
            return true;
        }

        @Override
        public TriangleWidgetRenderer getCellRenderer() {
            return renderer;
        }
    }
}

