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
package org.jreserve.jrlib.splines;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SplineCurve {
    
    private List<Spline> splines = new ArrayList<Spline>();
    private int sCount = 0;
    private double minX;
    private double maxX;
    
    public void addSpline(Spline spline) {
        this.splines.add(spline);
        for(Spline s : splines) {
            double x = s.getX0();
            if(Double.isNaN(minX) || minX > x)
                minX = x;
            if(Double.isNaN(maxX) || maxX < x)
                maxX = x;
        }
        sCount++;
    }
    
    public double valueAt(double x) {
        if(sCount == 0 || x < minX || x > maxX)
            return Double.NaN;
        int index = getIndex(x);
        return splines.get(index).valueAt(x);
    }
    
    private int getIndex(double x) {
        for(int i=(sCount-1); i>0; i--) {
            if(x > splines.get(i).getX0())
                return i;
        }
        return 0;
    }
}
