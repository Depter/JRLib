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

package org.jreserve.gui.trianglewidget.model;

import javax.swing.event.ChangeListener;
import org.jreserve.jrlib.triangle.Triangle;

/**
 * A triangle model is responsible for the layout of
 * a {@link Triangle Triangle} within a widget.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleModel {
    
    /**
     * Returns the triangle, representing the data.
     */
    public Triangle getTriangle();
    
    /**
     * Sets the triangle, representing the data.
     */
    public void setTriangle(Triangle triangle);
    
    /**
     * Returns the {@link TitleModel TitleModel} for
     * the row header.
     */
    public TitleModel getHorizontalTitleModel();
    
    /**
     * Sets the {@link TitleModel TitleModel} for
     * the row header.
     */
    public void setHorizontalTitleModel(TitleModel model);
    
    /**
     * Returns the {@link TitleModel TitleModel} for
     * the column header.
     */
    public TitleModel getVerticalTitleModel();
    
    /**
     * Returns the {@link TitleModel TitleModel} for
     * the column header.
     */
    public void setVerticalTitleModel(TitleModel model);
    
    /**
     * Returns the number of rows.
     */
    public int getRowCount();
    
    /**
     * Returns the number of columns.
     */
    public int getColumnCount();
    
    /**
     * Returns the row index for the given accident and
     * development indices.
     */
    public int getRowIndex(int accident, int development);
    
    /**
     * Returns the column index for the given accident and
     * development indices.
     */
    public int getColumnIndex(int accident, int development);
    
    /**
     * Returns the accident index for the given row and
     * column indices.
     */
    public int getAccidentIndex(int row, int column);
    
    /**
     * Returns the development index for the given row and
     * column indices.
     */
    public int getDevelopmentIndex(int row, int column);
    
    /**
     * Checks wether there is a value at the given
     * cell.
     */
    public boolean hasValueAt(int row, int column);
    
    /**
     * Gets the value from the given cell. If it falls
     * outside of the bounds of the {@link Triangle Triangle}
     * the *Double.NaN* is returned.
     */
    public double getValueAt(int row, int column);
    
    /**
     * Ads a {@link ChangeListener ChangeListener} to the model.
     */
    public void addChangeListener(ChangeListener listener);

    /**
     * Removes a {@link ChangeListener ChangeListener} from the model.
     */
    public void removeChangeListener(ChangeListener listener);
}
