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
package org.jreserve.gui.calculations.vector.impl;

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
import org.jreserve.jrlib.triangle.InputTriangle;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.vector.InputVector;
import org.jreserve.jrlib.vector.Vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorGeometryUtil {
    
    private final static String GEOMETRY_ELEMENT = "geometry";
    private final static String START_ELEMENT = "startDate";
    private final static String END_ELEMENT = "endDate";
    private final static String AL_ELEMENT = "accidentLength";
    
    public static Element toXml(TriangleGeometry geometry) {
        Element root = new Element(GEOMETRY_ELEMENT);
        JDomUtil.addElement(root, START_ELEMENT, geometry.getStartDate());
        JDomUtil.addElement(root, END_ELEMENT, geometry.getEndDate());
        JDomUtil.addElement(root, AL_ELEMENT, geometry.getAccidentLength());
        return root;
    }
    
    static TriangleGeometry fromXml(Element root) throws IOException {
        Element ge = JDomUtil.getExistingChild(root, GEOMETRY_ELEMENT);
        MonthDate start = getDate(ge, START_ELEMENT);
        MonthDate end = getDate(ge, END_ELEMENT);
        int al = JDomUtil.getExistingInt(ge, AL_ELEMENT);
        return new TriangleGeometry(start, end, al, 1);
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
    
    static InputVector createTriangle(DataSource ds, TriangleGeometry geometry) throws Exception {
        DateVector vector = new DateVector(geometry);
        for(DataEntry entry : ds.getEntries(new Filter(geometry)))
            vector.addEntry(entry);
        return new InputVector(vector.getValues());
    }
    
    public static Triangle toTriangle(Vector vector) {
        int length = vector.getLength();
        double[][] data = new double[length][1];
        for(int i=0; i<length; i++)
            data[i][0] = vector.getValue(i);
        return new InputTriangle(data);
    }
    
    private VectorGeometryUtil() {
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
            MonthDate date = entry.getAccidentDate();
            if(date.before(start) || date.after(end))
                return false;
            return true;
        }
    }
    
    private static class DateVector {
        private List<AccidentRow> rows = new ArrayList<AccidentRow>();
        
        private DateVector(TriangleGeometry geometry) {
            MonthDate start = geometry.getStartDate();
            MonthDate end = geometry.getEndDate();
            int al = geometry.getAccidentLength();
            
            while(!start.after(end)) {
                MonthDate rowEnd = start.addMonth(al);
                rows.add(new AccidentRow(start, rowEnd));
                start = rowEnd;
            }
        }
        
        void addEntry(DataEntry entry) {
            AccidentRow row = getRow(entry);
            if(row != null)
                row.addEntry(entry);
        }
        
        private AccidentRow getRow(DataEntry entry) {
            for(AccidentRow r : rows)
                if(r.containsEntry(entry))
                    return r;
            return null;
        }
        
        double[] getValues() {
            int rCount = rows.size();
            double[] result = new double[rCount];
            for(int i=0; i<rCount; i++)
                result[i] = rows.get(i).getValue();
            return result;
        }
    }
    
    private static class AccidentRow {
        private MonthDate rowStart;
        private MonthDate rowEnd;
        private double value = Double.NaN;
        private boolean firstValue = true;
        
        private AccidentRow(MonthDate rowStart, MonthDate rowEnd) {
            this.rowStart = rowStart;
            this.rowEnd = rowEnd;
        }
        
        boolean containsEntry(DataEntry entry) {
            MonthDate accident = entry.getAccidentDate();
            return rowEnd.after(accident) &&
                   !accident.before(rowStart);
        }
        
        void addEntry(DataEntry entry) {
            if(firstValue) {
                value = entry.getValue();
                firstValue = false;
            } else {
                value += entry.getValue();
            }
        }
        
        double getValue() {
            return value;
        }
    }
    
}
