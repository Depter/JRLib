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
    implements ModifiableCalculationProvider<C> {

    protected final static String MODIFICATIONS_ELEMENT = "modifications";
    
    private List<CalculationModifier<C>> modifications = new ArrayList<CalculationModifier<C>>();
    private final Class<C> clazz;
    
    protected AbstractModifiableCalculationProvider(Class<C> clazz) {
        this.clazz = clazz;
    }
    
    protected AbstractModifiableCalculationProvider(Element parent, Class<C> category) throws Exception {
        this.clazz = category;
        Element mr = JDomUtil.getExistingChild(parent, MODIFICATIONS_ELEMENT);
        for(Element me : mr.getChildren()) {
            String rootName = me.getName();
            CalculationModifierFactory<C> factory = CalculationModifierFactoryRegistry.getFactory(category, rootName);
            if(factory != null)
                modifications.add(factory.fromXml(me));
        }
    }
    
    @Override
    public Class<C> getCalculationClass() {
        return clazz;
    }
    
    @Override
    public synchronized int getModificationCount() {
        return modifications.size();
    }

    @Override
    public synchronized CalculationModifier<C> getModificationAt(int index) {
        return modifications.get(index);
    }

    @Override
    public synchronized void setModification(int index, CalculationModifier<C> cm) {
        if(cm == null)
            throw new NullPointerException("ModificationFactory is null!");
        modifications.set(index, cm);
        modificationsChanged();
    }

    protected abstract void modificationsChanged();
    
    @Override
    public synchronized void addModification(CalculationModifier<C> cm) {
        this.addModification(modifications.size(), cm);
    }
    
    @Override
    public synchronized void addModification(int index, CalculationModifier<C> cm) {
        if(cm == null)
            throw new NullPointerException("ModificationFactory is null!");
        modifications.add(index, cm);
        modificationsChanged();
    }

    @Override
    public synchronized void deleteModification(int index) {
        modifications.remove(index);
        modificationsChanged();
    }

    @Override
    public C createCalculation() throws Exception {
        C calculation = createBaseCalculation();
        for(CalculationModifier<C> mf : modifications)
            calculation = mf.createCalculation(calculation);
        return calculation;
    }
    
    protected abstract C createBaseCalculation() throws Exception;
    
    protected Element toXml() {
        Element root = new Element(MODIFICATIONS_ELEMENT);
        for(CalculationModifier cm : modifications)
            root.addContent(cm.toXml());
        return root;
    }
}
