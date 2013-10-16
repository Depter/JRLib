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

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Spline implements Comparable<Spline> {
    
    private final double x0;
    private final double a;
    private final double b;
    private final double c;
    private final double d;

    public Spline(double x0, double a, double b, double c, double d) {
        this.x0 = x0;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    public double getA() {
        return a;
    }

    public double getX0() {
        return x0;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }
    
    public double valueAt(double x) {
        double h = x - x0;
        return a * h * h * h + b * h * h + c * h + d;
    }    

    public int compareTo(Spline o) {
        if(this == o) return 0;
        if(this.x0 < o.x0)
            return -1;
        else if(this.x0 > o.x0)
            return 1;
        return 0;
    }
}
