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
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractSmoothDialogController<C extends CalculationModifier> implements SmoothDialogController<C> {

    private String title;
    private final ChangeSupport cs = new ChangeSupport(this);
    
    protected AbstractSmoothDialogController(String title) {
        this.title = title;
    }
    
    @Override
    public String getDialogTitle() {
        return title;
    }
    
    @Override
    public HelpCtx getHelpContext() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }
    
    protected final void fireChange() {
        cs.fireChange();
    }
}
