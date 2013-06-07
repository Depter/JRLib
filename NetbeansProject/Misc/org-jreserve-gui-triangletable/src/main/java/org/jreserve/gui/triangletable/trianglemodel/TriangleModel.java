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

package org.jreserve.gui.triangletable.trianglemodel;

import javax.swing.event.ChangeListener;
import org.jreserve.jrlib.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleModel {
    
    public Triangle getTriangle();
    
    public void setTriangle(Triangle triangle);
    
    public int getRowCount();
    
    public int getColumnCount();
    
    public int getRowIndex(int accident, int development);
    
    public int getColumnIndex(int accident, int development);
    
    public int getDevelopmentIndex(int row, int column);
    
    public int getAccidentIndex(int row, int column);

    public boolean hasValueAt(int row, int column);
    
    public double getValueAt(int row, int column);
    
    public void addChangeListener(ChangeListener listener);

    public void removeChangeListener(ChangeListener listener);
}
