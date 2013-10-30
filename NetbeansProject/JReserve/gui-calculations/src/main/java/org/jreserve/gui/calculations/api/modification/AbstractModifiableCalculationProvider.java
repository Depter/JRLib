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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractCalculationProvider;
import org.jreserve.gui.calculations.api.CalculationDataObject;
import org.jreserve.gui.calculations.util.CalculationModifierFactoryRegistry;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractModifiableCalculationProvider<C extends CalculationData>
    extends AbstractCalculationProvider<C>
    implements ModifiableCalculationProvider<C> {

    public final static String MODIFICATIONS_ELEMENT = "modifications";
    
    protected final List<CalculationModifier<C>> modifications = new ArrayList<CalculationModifier<C>>();
    private final ModificationListener cmListener = new ModificationListener();
    private final CalculationDataObject obj;
    private Map<Integer, C> layers;
    
    protected AbstractModifiableCalculationProvider(CalculationDataObject obj) {
        super(obj);
        this.obj = obj;
    }
    
    protected AbstractModifiableCalculationProvider(CalculationDataObject obj, Element parent, String category) throws Exception {
        super(obj);
        this.obj = obj;
        Element mr = JDomUtil.getExistingChild(parent, MODIFICATIONS_ELEMENT);
        for(Element me : mr.getChildren()) {
            String rootName = me.getName();
            CalculationModifierFactory<C> factory = CalculationModifierFactoryRegistry.getFactory(category, rootName);
            if(factory != null) {
                CalculationModifier cm = factory.fromXml(me);
                registerListener(cm);
                modifications.add(cm);
            }
        }
    }
    
    private void registerListener(CalculationModifier cm) {
        if(cm instanceof EditableCalculationModifier) {
            EditableCalculationModifier ecm = (EditableCalculationModifier) cm;
            ecm.addCalculationModifierListener(cmListener);
        }
    }
    
    @Override
    public int getModificationCount() {
        synchronized(lock) {
            return modifications.size();
        }
    }
    
    @Override
    public int indexOfModification(CalculationModifier cm) {
        synchronized(lock) {
            int size = modifications.size();
            for(int i=0; i<size; i++)
                if(cm == modifications.get(i))
                    return i;
            return -1;
        }
    }
    
    @Override
    public CalculationModifier<C> getModificationAt(int index) {
        synchronized(lock) {
            return modifications.get(index);
        }
    }

    @Override
    public void setModification(int index, CalculationModifier<C> cm) {
        synchronized(lock) {
            if(cm == null)
                throw new NullPointerException("ModificationFactory is null!");
            CalculationModifier removed = modifications.set(index, cm);
            registerListener(cm);
            modificationsChanged();
            
            super.events.fireModificationAdded(index, cm, removed);
        }
    }
    
    protected final void modificationsChanged() {
        recalculateIfExists();
        events.fireChange();
        this.obj.setModified(true);
    }
    
    @Override
    public void addModification(CalculationModifier<C> cm) {
        synchronized(lock) {
            this.addModification(modifications.size(), cm);
        }
    }
    
    @Override
    public void addModification(int index, CalculationModifier<C> cm) {
        synchronized(lock) {
            if(cm == null)
                throw new NullPointerException("ModificationFactory is null!");
            modifications.add(index, cm);
            registerListener(cm);
            modificationsChanged();
            super.events.fireModificationAdded(index, cm);
        }
    }

    @Override
    public void deleteModification(int index) {
        synchronized(lock) {
            CalculationModifier cm = modifications.remove(index);
            deregisterListener(cm);
            modificationsChanged();
            super.events.fireModificationDeleted(index, cm);
        }
    }
    
    private void deregisterListener(CalculationModifier cm) {
        if(cm instanceof EditableCalculationModifier) {
            EditableCalculationModifier ecm = (EditableCalculationModifier) cm;
            ecm.removeCalculationModifierListener(cmListener);
        }
    }

    protected C modifyCalculation(C calculation) throws Exception {
        for(CalculationModifier<C> mf : modifications)
            calculation = mf.createCalculation(calculation);
        return calculation;
    }
    
    protected C modifyCalculation(C calculation, int layer) throws Exception {
        int count = 0;
        while(count < layer) {
            CalculationModifier<C> cm = modifications.get(count++);
            calculation = cm.createCalculation(calculation);
        }
        return calculation;
    }
    
    protected Element toXml() {
        Element root = new Element(MODIFICATIONS_ELEMENT);
        for(CalculationModifier cm : modifications)
            root.addContent(cm.toXml());
        return root;
    }
    
    @Override
    protected abstract ModificationCalculator createCalculator();
    
    private class ModificationListener implements CalculationModifierListener<C> {

        @Override
        public void modificationChanged(EditableCalculationModifier<C> source, Map<Object, Object> preState) {
            synchronized(AbstractModifiableCalculationProvider.super.lock) {
                modificationsChanged();
                
                int index = indexOf(source);
                if(index >= 0)
                    events.fireModificationChanged(index, source, preState);
            }
        }
        
        private int indexOf(CalculationModifier cm) {
            int size = modifications.size();
            for(int i=0; i<size; i++)
                if(cm == modifications.get(i))
                    return i;
            return -1;
        }
    }
    
    @Override
    public final C getCalculation(int layer) {
        synchronized(lock) {
            if(layers == null)
                return createDummyCalculation();
            C result = layers.get(layer);
            return result==null? createDummyCalculation() : result;
        }
    }
    
    protected abstract class ModificationCalculator implements Calculator<C> {
        
        private final List<CalculationModifier<C>> mods;
        
        protected ModificationCalculator() {
            synchronized(lock) {
                mods = new ArrayList<CalculationModifier<C>>(modifications);
            }
        }
        
        @Override
        public C createCalculation() {
            C result = getRootCalculation();
            final Map<Integer, C> layers = new HashMap<Integer, C>();
            int layer = 0;
            layers.put(layer++, result);
            for(CalculationModifier<C> mod : mods) {
                result = mod.createCalculation(result);
                layers.put(layer++, result);
            }
            
            synchronized(lock) {
                AbstractModifiableCalculationProvider.this.layers = layers;
            }
            
            return result;
        }
        
        protected abstract C getRootCalculation();
    
    }
}
