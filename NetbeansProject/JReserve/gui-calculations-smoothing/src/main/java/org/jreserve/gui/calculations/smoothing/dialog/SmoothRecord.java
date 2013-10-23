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
package org.jreserve.gui.calculations.smoothing.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.jreserve.jrlib.vector.Vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothRecord {
    
    public static List<SmoothRecord> createRecords(CalculationData data, TriangleGeometry geometry, List<Cell> cells) {
        Collections.sort(cells);
        List<SmoothRecord> records = new ArrayList<SmoothRecord>(cells.size());
        for(Cell cell : cells)
            records.add(createRecord(data, geometry, cell));
        return records;
    }
    
    public static SmoothRecord createRecord(CalculationData data, TriangleGeometry geometry, Cell cell) {
        MonthDate accident = geometry.getAccidentDate(cell.getAccident());
        int development = cell.getDevelopment();
        double value = getValue(data, cell);
        
        SmoothRecord record = new SmoothRecord(accident, cell.getAccident(), development, value);
        if(cell instanceof SmoothingCell)
            record.used = ((SmoothingCell)cell).isApplied();
        
        return record;
    }
    
    private static double getValue(CalculationData data, Cell cell) {
        if(data instanceof Triangle) {
            return ((Triangle)data).getValue(cell);
        } else if(data instanceof Vector) {
            int index = cell.getAccident();
            if(index<0)
                index = cell.getDevelopment();
            return ((Vector)data).getValue(index);
        } else {
            String msg = "Data must be an instance of '%s' or '%s', but it was '%s'!";
            msg = String.format(msg, Triangle.class, Vector.class, data.getClass());
            throw new IllegalArgumentException(msg);
        }
    }
    
    private final int accident;
    private final MonthDate accidentDate;
    private final int development;
    private boolean used;
    private double original;
    private double smoothed;
    
    private SmoothRecord(MonthDate accidentDate, int accident, int development, double value) {
        this.accident = accident;
        this.accidentDate = accidentDate;
        this.development = development;
        this.used = false;
        this.original = value;
        this.smoothed = value;
    }
    
    public int getAccident() {
        return accident;
    }
    
    public MonthDate getAccidentDate() {
        return accidentDate;
    }
    
    public int getDevelopment() {
        return development;
    }
    
    public boolean isUsed() {
        return used;
    }
    
    public void setUsed(boolean used) {
        this.used = used;
    }

    public double getOriginal() {
        return original;
    }

    public void setOriginal(double original) {
        this.original = original;
    }

    public double getSmoothed() {
        return smoothed;
    }

    public void setSmoothed(double smoothed) {
        this.smoothed = smoothed;
    }
}
