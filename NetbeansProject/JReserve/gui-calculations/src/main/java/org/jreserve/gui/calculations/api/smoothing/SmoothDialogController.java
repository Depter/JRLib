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
package org.jreserve.gui.calculations.api.smoothing;

import java.awt.Component;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.calculations.api.CalculationModifier;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface SmoothDialogController<C extends CalculationModifier> {
    
    public String getDialogTitle();
    
    public Component getParameterComponent();
    
    public List<SmoothRecord> getRecords();
    
    public boolean isValid();
    
    public C createModifier();
    
    public void updateRecords(List<SmoothRecord> records);
    
    public void addChangeListener(ChangeListener listener);
    
    public void removeChangeListener(ChangeListener listener);
}
