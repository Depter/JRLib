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

import java.awt.Component;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.jrlib.triangle.Triangle;
import org.openide.util.HelpCtx;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface SmoothDialogController<T extends Triangle> {
    
    public String getDialogTitle();
    
    public Component getParameterComponent();
    
    public HelpCtx getHelpContext();
    
    public List<SmoothRecord> getRecords();
    
    public boolean canModifyCells();
    
    public boolean isValid();
    
    public CalculationModifier<T> createModifier();
    
    public void updateRecords(List<SmoothRecord> records);
    
    public void addChangeListener(ChangeListener listener);
    
    public void removeChangeListener(ChangeListener listener);
}
