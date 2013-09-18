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
package org.jreserve.gui.calculations.triangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.claim.InputClaimTriangle;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleGeometryUtil {
    
    private final static String CT_ROOT = "claimTriangle";
    private final static String GEOMETRY_ELEMENT = "geometry";
    private final static String START_ELEMENT = "startDate";
    private final static String END_ELEMENT = "startDate";
    private final static String AL_ELEMENT = "accidentLength";
    private final static String DL_ELEMENT = "developmentLength";
    
    static Element toXml(TriangleGeometry geometry) {
        Element root = new Element(GEOMETRY_ELEMENT);
        JDomUtil.addElement(root, START_ELEMENT, geometry.getStartDate());
        JDomUtil.addElement(root, END_ELEMENT, geometry.getEndDate());
        JDomUtil.addElement(root, AL_ELEMENT, geometry.getAccidentLength());
        JDomUtil.addElement(root, DL_ELEMENT, geometry.getDevelopmentLength());
        return root;
    }
    
    static TriangleGeometry fromXml(Element root) throws IOException {
        Element ge = JDomUtil.getExistingChild(root, GEOMETRY_ELEMENT);
        MonthDate start = getDate(ge, START_ELEMENT);
        MonthDate end = getDate(ge, END_ELEMENT);
        int al = JDomUtil.getExistingInt(ge, AL_ELEMENT);
        int dl = JDomUtil.getExistingInt(root, DL_ELEMENT);
        return new TriangleGeometry(start, end, al, dl);
    }
    
    private static MonthDate getDate(Element ge, String name) throws IOException {
        String str = JDomUtil.getExistingString(ge, name);
        
        try {
            return new MonthDate(str);
        } catch (Exception ex) {
            String msg = "Illegal MonthDate string '%s' at '%s'!";
            Element e = JDomUtil.getExistingChild(ge, name);
            String path = JDomUtil.getPath(e);
            throw new IOException(String.format(msg, str, path), ex);
        }
    }
    
    static InputClaimTriangle createTriangle(DataSource ds, TriangleGeometry geometry) throws Exception {
        DateTriangle triangle = new DateTriangle(geometry);
        for(DataEntry entry : ds.getEntries(new Filter(geometry)))
            triangle.addEntry(entry);
        return new InputClaimTriangle(triangle.getValues());
    }
    
    private TriangleGeometryUtil() {
    }
    
    private static class Filter implements DataEntryFilter {
        
        private final MonthDate start;
        private final MonthDate end;
        
        private Filter(TriangleGeometry geometry) {
            this.start = geometry.getStartDate();
            this.end = geometry.getEndDate();
        }
        
        @Override
        public boolean acceptsEntry(DataEntry entry) {
            MonthDate date = entry.getDevelopmentDate();
            if(date.before(start) || date.after(end))
                return false;
            return true;
        }
    }
    
    private static class DateTriangle {
        private List<AccidentRow> rows = new ArrayList<AccidentRow>();
        
        private DateTriangle(TriangleGeometry geometry) {
            
            MonthDate start = geometry.getStartDate();
            MonthDate end = geometry.getEndDate();
            int al = geometry.getAccidentLength();
            int dl = geometry.getDevelopmentLength();
            
            while(!start.after(end)) {
                MonthDate rowEnd = start.addMonth(al);
                rows.add(new AccidentRow(start, rowEnd, end, dl));
                start = rowEnd;
            }
        }
        
        void addEntry(DataEntry entry) {
            getRow(entry).addEntry(entry);
        }
        
        private AccidentRow getRow(DataEntry entry) {
            for(AccidentRow r : rows)
                if(r.containsEntry(entry))
                    return r;
            return null;
        }
        
        double[][] getValues() {
            int rCount = rows.size();
            double[][] result = new double[rCount][];
            for(int i=0; i<rCount; i++)
                result[i] = rows.get(i).getValues();
            return result;
        }
    }
    
    private static class AccidentRow {
        private MonthDate rowStart;
        private MonthDate rowEnd;
        private List<DevelopmentCell> cells = new ArrayList<DevelopmentCell>();
        
        private AccidentRow(MonthDate rowStart, MonthDate rowEnd, MonthDate end, int dl) {
            this.rowStart = rowStart;
            this.rowEnd = rowEnd;
            
            MonthDate start = rowStart;
            while(!start.after(end)) {
                MonthDate cellEnd = start.addMonth(dl);
                cells.add(new DevelopmentCell(start, cellEnd));
                start = cellEnd;
            }
        }
        
        boolean containsEntry(DataEntry entry) {
            MonthDate accident = entry.getAccidentDate();
            return rowEnd.after(accident) &&
                   !accident.before(rowStart);
        }
        
        void addEntry(DataEntry entry) {
            getCell(entry).addEntry(entry);
        }
        
        private DevelopmentCell getCell(DataEntry entry) {
            for(DevelopmentCell c : cells)
                if(c.containsEntry(entry))
                    return c;
            return null;
        }
        
        double[] getValues() {
            int cCount = cells.size();
            double[] result = new double[cCount];
            for(int i=0; i<cCount; i++)
                result[i] = cells.get(i).value;
            return result;
        }
    }
    
    private static class DevelopmentCell {
        private MonthDate cellStart;
        private MonthDate cellEnd;
        private Double value = Double.NaN;
        private boolean firstValue = true;
        
        public DevelopmentCell(MonthDate cellStart, MonthDate cellEnd) {
            this.cellStart = cellStart;
            this.cellEnd = cellEnd;
        }
        
        boolean containsEntry(DataEntry entry) {
            MonthDate development = entry.getDevelopmentDate();
            return cellEnd.after(development) &&
                   !development.before(cellStart);
        }
        
        void addEntry(DataEntry entry) {
            if(firstValue) {
                value = entry.getValue();
                firstValue = false;
            } else {
                value += entry.getValue();
            }
        }
    }
}
