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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jreserve.gui.data.api.DataEntry;
import org.jreserve.gui.data.spi.MonthDate;
import org.jreserve.gui.trianglewidget.DefaultTriangleWidgetRenderer;
import org.jreserve.gui.trianglewidget.TriangleWidgetRenderer;
import org.jreserve.gui.trianglewidget.model.DevelopmentTriangleModel;
import org.jreserve.gui.trianglewidget.model.IndexTitleModel;
import org.jreserve.gui.trianglewidget.model.TitleModel;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.trianglewidget.model.TriangleModel;
import org.jreserve.jrlib.triangle.AbstractTriangle;
import org.jreserve.jrlib.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 */
class TriangleUtil {

    private List<DataEntry> entries;
    private Map<MonthDate, Map<MonthDate, DataEntry>> valueMap;
    private List<MonthDate> accidents;
    private List<MonthDate> developments;
    private Triangle triangle;
    private TriangleModel triangleModel;
    private DataEntryTriangleLayer layer;
    
    TriangleUtil(List<DataEntry> entries) {
        this.entries = entries==null? Collections.EMPTY_LIST : entries;
        valueMap = new TreeMap<MonthDate, Map<MonthDate, DataEntry>>();
        accidents = new ArrayList<MonthDate>();
        developments = new ArrayList<MonthDate>();
        initValueMap();
        triangle = new EntryTriangle();
        triangleModel = new DevelopmentTriangleModel(triangle, new IndexTitleModel(), new MonthDateTitleModel(accidents));
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
    
    private void initValueMap() {
        for(DataEntry entry : entries) {
            MonthDate accident = entry.getAccidentDate();
            MonthDate development = entry.getDevelopmentDate();
            getRow(accident).put(development, entry);
            
            if(!accidents.contains(accident))
                accidents.add(accident);
            if(!developments.contains(development))
                developments.add(development);
        }
        Collections.sort(accidents);
        Collections.sort(developments);
    }
    
    private Map<MonthDate, DataEntry> getRow(MonthDate accident) {
        Map<MonthDate, DataEntry> row = valueMap.get(accident);
        if(row == null) {
            row = new TreeMap<MonthDate, DataEntry>();
            valueMap.put(accident, row);
        }
        return row;
    }
    
    DataEntry getEntry(int row, int column) {
        if(row < 0 || row>=accidents.size())
            return null;
        MonthDate md = accidents.get(row);
        Map<MonthDate, DataEntry> map = valueMap.get(md);
        
        DataEntry[] rowArr = map.values().toArray(new DataEntry[map.size()]);
        if(column < 0 || column>=rowArr.length)
            return null;
        return rowArr[column];
    }
    
    void setRenderer(TriangleWidgetRenderer renderer) {
        layer.renderer = renderer;
    }
    
    private class EntryTriangle extends AbstractTriangle {

        @Override
        protected boolean withinBounds(int accident) {
            return 0 <= accident && accident < valueMap.size();
        }

        @Override
        protected void recalculateLayer() {
        }

        @Override
        public int getAccidentCount() {
            return accidents.size();
        }

        @Override
        public int getDevelopmentCount() {
            return developments.size();
        }

        @Override
        public int getDevelopmentCount(int accident) {
            MonthDate md = accidents.get(accident);
            Map<MonthDate, DataEntry> row = valueMap.get(md);
            return md==null? 0 : row.size();
        }

        @Override
        public double getValue(int accident, int development) {
            MonthDate date = accidents.get(accident);
            Map<MonthDate, DataEntry> row = valueMap.get(date);
            int index = 0;
            for(DataEntry entry : row.values()) {
                if(index++ == development)
                    return entry==null? Double.NaN : entry.getValue();
            }
            return Double.NaN;
        }
    
    }

    private class MonthDateTitleModel implements TitleModel {
        
        private List<MonthDate> dates;

        MonthDateTitleModel(List<MonthDate> dates) {
            this.dates = dates;
        }
        
        @Override
        public String getName(int index) {
            if(index < 0 || index>=dates.size())
                return null;
            return dates.get(index).toString();
        }
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

