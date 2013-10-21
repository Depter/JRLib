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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jreserve.jrlib.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractEditableCalculationModifier<C extends CalculationData>
    extends AbstractCalculationModifier<C>
    implements EditableCalculationModifier<C> {
    
    private List<CalculationModifierListener<C>> listeners = new ArrayList<CalculationModifierListener<C>>();
    private boolean changeFired = true;
    
    protected AbstractEditableCalculationModifier(Class<C> clazz) {
        super(clazz);
    }
    
    public void addCalculationModifierListener(CalculationModifierListener<C> listener) {
        listeners.add(listener);
    }
    
    public void removeCalculationModifierListener(CalculationModifierListener<C> listener) {
        listeners.remove(listener);
    }
    
    protected void setChangeFired(boolean fireChange) {
        this.changeFired = fireChange;
    }
    
    protected boolean isChangeFired() {
        return changeFired;
    }
    
    protected void fireChange(Map<Object, Object> preState) {
        if(changeFired)
            for(CalculationModifierListener listener : getListenerArray())
                listener.modificationChanged(this, preState);
    }
    
    private CalculationModifierListener[] getListenerArray() {
        int size = listeners.size();
        return listeners.toArray(new CalculationModifierListener[size]);
    }
    
    @Override
    public void edit(ModifiableCalculationProvider<C> calculation) {
        String msg = "Should not call edit on an uneditable CalculationModifier!";
        throw new IllegalStateException(msg);
    }
    
    protected C getSource(ModifiableCalculationProvider<C> calculation) {
        int index = calculation.indexOfModification(this);
        if(index >= 0)
            return calculation.getCalculation(index);
        return null;
    }
    
    protected static int getInt(Map state, Object key, int def) {
        Integer v = (Integer) getValue(state, key, null);
        return v==null? def : v.intValue();
    }
    
    protected static int getInt(Map state, Object key) {
        Object v = getValue(state, key, null);
        if(!(v instanceof Integer)) {
            String msg = "int value for key '%s' is missing!";
            throw new IllegalArgumentException(String.format(msg, key));
            
        }
        return ((Integer)v).intValue();
    }
    
    protected static double getDouble(Map state, Object key, int def) {
        Integer v = (Integer) getValue(state, key, null);
        return v==null? def : v.intValue();
    }
    
    protected static double getDouble(Map state, Object key) {
        Object v = getValue(state, key, null);
        if(!(v instanceof Double)) {
            String msg = "double value for key '%s' is missing!";
            throw new IllegalArgumentException(String.format(msg, key));
            
        }
        return ((Double)v).doubleValue();
    }
    
    protected static boolean getBoolean(Map state, Object key, boolean def) {
        Boolean b = (Boolean) getValue(state, key, null);
        return b==null? def : b.booleanValue();
    }
    
    protected static boolean getBoolean(Map state, Object key) {
        Object v = getValue(state, key, null);
        if(!(v instanceof Boolean)) {
            String msg = "boolean value for key '%s' is missing!";
            throw new IllegalArgumentException(String.format(msg, key));
            
        }
        return ((Boolean)v).booleanValue();
    }
    
    protected static Object getValue(Map state, Object key, Object def) {
        Object value = state.get(key);
        return value==null? def : value;
    }
}
