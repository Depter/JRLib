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

package org.jreserve.jrlib.gui.data;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleGeometry {
    
    private MonthDate start;
    private MonthDate end;
    private int accidentLength;
    private int developmentLength;
    private final List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    public TriangleGeometry(MonthDate start, MonthDate end, int accidentLength, int developmentLength) {
        this.end = end;
        this.start = start;
        this.accidentLength = accidentLength;
        this.developmentLength = developmentLength;
        checkDates();
        checkLengths();
    }
    
    private void checkDates() {
        if(start == null)
            throw new NullPointerException("Start date is null!");
        if(end == null)
            throw new NullPointerException("End date is null!");
        if(end.before(start))
            throw new IllegalArgumentException(String.format("End date '%s' is before start date '%s'!", end, start));
    }
    
    private void checkLengths() {
        if(accidentLength < 1)
            throw new IllegalArgumentException("Accident length '"+accidentLength+"' is smaller than 1!");
        if(developmentLength < 1)
            throw new IllegalArgumentException("Development length '"+developmentLength+"' is smaller than 1!");
    }
    
    public MonthDate getStartDate() {
        return start;
    }
    
    public void setStartDate(MonthDate start) {
        this.start = start;
        checkDates();
        fireChange();
    }
    
    public MonthDate getEndDate() {
        return end;
    }
    
    public void setEndDate(MonthDate end) {
        this.end = end;
        checkDates();
        fireChange();
    }
    
    public void setBounds(MonthDate start, MonthDate end) {
        this.start = start;
        this.end = end;
        checkDates();
        fireChange();
    }
    
    public int getAccidentLength() {
        return accidentLength;
    }
    
    public void setAccidentLength(int length) {
        this.accidentLength = length;
        checkLengths();
        fireChange();
    }
    
    public int getDevelopmentLength() {
        return developmentLength;
    }
    
    public void setDevelopmentLength(int length) {
        this.developmentLength = length;
        checkLengths();
        fireChange();
    }
    
    public MonthDate getAccidentDate(int accident) {
        return start.addMonth(accident * accidentLength);
    }
    
    public MonthDate getDevelopmentDate(int accident, int development) {
        int months = accident * accidentLength + development * developmentLength;
        return start.addMonth(months);
    }
    
    public void addChangeListener(ChangeListener listener) {
        if(listener != null && !listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : listeners.toArray(new ChangeListener[listeners.size()]))
            l.stateChanged(evt);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(this == o) return true;
        if(!(o instanceof TriangleGeometry)) return false;
        return equals((TriangleGeometry) o);
    }
    
    private boolean equals(TriangleGeometry tg) {
        return accidentLength == tg.accidentLength &&
               developmentLength == tg.developmentLength &&
                start.equals(tg.start) &&
               end.equals(tg.end);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + start.hashCode();
        hash = 17 * hash + end.hashCode();
        hash = 17 * hash + accidentLength;
        return 17 * hash + developmentLength;
    }
    
    @Override
    public String toString() {
        String str = "TriangleGeometry [%s -> %s; %d / %d]";
        return String.format(str, start, end, accidentLength, developmentLength);
    }
}
