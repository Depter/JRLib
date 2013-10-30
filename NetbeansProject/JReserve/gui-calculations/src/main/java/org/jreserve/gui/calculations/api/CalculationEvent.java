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

package org.jreserve.gui.calculations.api;

import org.jreserve.gui.calculations.api.method.CalculationMethod;
import org.jreserve.gui.calculations.api.modification.EditableCalculationModifier;
import org.jreserve.gui.calculations.api.modification.CalculationModifier;
import java.util.Map;
import org.jreserve.jrlib.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface CalculationEvent {
    
    public CalculationProvider getCalculationProvider();
    
    public static interface Created extends CalculationEvent {}
    
    public static interface Deleted extends CalculationEvent {}
    
    public static interface Saved extends CalculationEvent {}
    
    public static interface Renamed extends CalculationEvent {
        public String getOldPath();
    }
    
    public static interface Change extends CalculationEvent {}
    
    public static interface ValueChanged<C extends CalculationData> extends CalculationEvent {
        public C getCalculationData();
    };
    
    public static interface ModificationChange extends Change {
        public int getModifiedIndex();
        public CalculationModifier getModifier();
    }
    
    public static interface ModificationAdded extends ModificationChange {
        public CalculationModifier getRemovedModifier();
    }
    
    public static interface ModificationDeleted extends ModificationChange {
    }
    
    public static interface ModificationChanged extends ModificationChange {
        @Override
        public EditableCalculationModifier getModifier();
        
        public Map getPreState();
    }
    
    public static interface MethodChange extends Change {
        public int getMethodIndex();
        public CalculationMethod getOldMethod();
    }
}
