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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdom2.Element;
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
    
    private List<CalculationModifier<C>> modifications = new ArrayList<CalculationModifier<C>>();
    private final Class<C> clazz;
    private final ModificationListener cmListener = new ModificationListener();
    //private C calculation;
    
    protected AbstractModifiableCalculationProvider(CalculationDataObject obj, Class<C> clazz) {
        super(obj);
        this.clazz = clazz;
    }
    
    protected AbstractModifiableCalculationProvider(CalculationDataObject obj, Element parent, Class<C> category) throws Exception {
        super(obj);
        this.clazz = category;
        Element mr = JDomUtil.getExistingChild(parent, MODIFICATIONS_ELEMENT);
        for(Element me : mr.getChildren()) {
            String rootName = me.getName();
            CalculationModifierFactory<C> factory = CalculationModifierFactoryRegistry.getFactory(category, rootName);
            if(factory != null) {
                CalculationModifier cm = factory.fromXml(me);
                cm.addChangeListener(cmListener);
                modifications.add(cm);
            }
        }
    }
    
    @Override
    public Class<C> getCalculationClass() {
        return clazz;
    }
    
    @Override
    public int getModificationCount() {
        synchronized(super.obj.lock) {
            return modifications.size();
        }
    }

    @Override
    public CalculationModifier<C> getModificationAt(int index) {
        synchronized(super.obj.lock) {
            return modifications.get(index);
        }
    }

    @Override
    public void setModification(int index, CalculationModifier<C> cm) {
        synchronized(super.obj.lock) {
            if(cm == null)
                throw new NullPointerException("ModificationFactory is null!");
            CalculationModifier removed = modifications.set(index, cm);
            cm.addChangeListener(cmListener);
            modificationsChanged();
            
            if(removed != null) {
                removed.removeChangeListener(cmListener);
                super.events.fireModificationDeleted(removed);
            }
            super.events.fireModificationAdded(cm);
        }
    }
    
    protected abstract void modificationsChanged();
    
    @Override
    public void addModification(CalculationModifier<C> cm) {
        synchronized(super.obj.lock) {
            this.addModification(modifications.size(), cm);
        }
    }
    
    @Override
    public void addModification(int index, CalculationModifier<C> cm) {
        synchronized(super.obj.lock) {
            if(cm == null)
                throw new NullPointerException("ModificationFactory is null!");
            modifications.add(index, cm);
            cm.addChangeListener(cmListener);
            modificationsChanged();
            super.events.fireModificationAdded(cm);
        }
    }

    @Override
    public void deleteModification(int index) {
        synchronized(super.obj.lock) {
            CalculationModifier cm = modifications.remove(index);
            cm.removeChangeListener(cmListener);
            modificationsChanged();
            super.events.fireModificationDeleted(cm);
        }
    }

    protected C modifyCalculation(C calculation) throws Exception {
        for(CalculationModifier<C> mf : modifications)
            calculation = mf.createCalculation(calculation);
        return calculation;
    }
    
    protected Element toXml() {
        Element root = new Element(MODIFICATIONS_ELEMENT);
        for(CalculationModifier cm : modifications)
            root.addContent(cm.toXml());
        return root;
    }
    
    private class ModificationListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            synchronized(AbstractModifiableCalculationProvider.super.obj.lock) {
                modificationsChanged();
            }
        }
    }
}
