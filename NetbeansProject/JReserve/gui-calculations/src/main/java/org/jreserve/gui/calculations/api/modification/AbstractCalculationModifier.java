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
package org.jreserve.gui.calculations.api.modification;

import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.jrlib.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCalculationModifier<C extends CalculationData> implements CalculationModifier<C>{

    private final Class<C> clazz;
    private Displayable displayable;
    
    protected AbstractCalculationModifier(Class<C> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public Class<? extends C> getCalculationClass() {
        return clazz;
    }

    @Override
    public synchronized Displayable getDisplayable() {
        if(displayable == null)
            displayable = createDisplayable();
        return displayable;
    }
    
    protected abstract Displayable createDisplayable();    
}
