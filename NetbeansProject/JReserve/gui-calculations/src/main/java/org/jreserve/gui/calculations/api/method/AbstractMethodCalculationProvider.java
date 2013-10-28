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
package org.jreserve.gui.calculations.api.method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractCalculationProvider;
import org.jreserve.gui.calculations.api.CalculationDataObject;
import org.jreserve.gui.calculations.util.CalculationMethodFactoryRegistry;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.util.method.FixedValueMethod;
import org.jreserve.jrlib.util.method.SelectableMethod;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 * @param <C> the result calculation type.
 * @param <M> the method type.
 */
public abstract class AbstractMethodCalculationProvider<C extends CalculationData, M extends SelectableMethod> 
    extends AbstractCalculationProvider<C> 
    implements MethodCalculationProvider<C, M> {
    
    private final static Logger logger = Logger.getLogger(AbstractMethodCalculationProvider.class.getName());
    private final static String CONSTANTS_ELEMENT = "constant";
    private final static String CONSTANT_ELEMENT = "constant";
    private final static String INDEX_ELEMENT = "index";
    private final static String VALUE_ELEMENT = "value";
    
    private List<MethodEntry> entries = new ArrayList<MethodEntry>();
    private FixedCalculationMethod fixed = new FixedCalculationMethod();
    
    public AbstractMethodCalculationProvider(CalculationDataObject obj, Element root, String category) throws Exception {
        super(obj);
        
        for(Element me : root.getChildren()) {
            String rootName = me.getName();
            if(rootName.equals(CONSTANT_ELEMENT)) {
                int index = JDomUtil.getExistingInt(me, INDEX_ELEMENT);
                double value = JDomUtil.getExistingDouble(me, VALUE_ELEMENT);
                fixed.set(index, value);
            } else {
                CalculationMethodFactory<M> factory = CalculationMethodFactoryRegistry.getFactory(category, rootName);
                if(factory != null) {
                    Set<Integer> indices = getIndices(me);
                    CalculationMethod<M> cm = factory.fromXml(me);
                    entries.add(new MethodEntry(cm, indices));
                } else {
                    String msg = "CalculationMethodFactory for xml name '%s' in category '%s' is not found!";   //NOI18
                    logger.log(Level.WARNING, String.format(msg, rootName, category));
                }
            }
        }
    }
    
    private Set<Integer> getIndices(Element me) throws IOException {
        Set<Integer> result = new TreeSet<Integer>();
        for(Element ie : me.getChildren())
            result.add(JDomUtil.getInt(ie));
        return result;
    }
    
    @Override
    public CalculationMethod<M> getMethodAt(int index) {
        synchronized(lock) {
            Integer objIndex = index;
            for(MethodEntry me : entries)
                if(me.indices.contains(objIndex))
                    return me.method;
        
            if(fixed.containsIndex(index))
                return fixed;
        
            return getDefaultMethod();
        }
    }
    
    protected abstract CalculationMethod<M> getDefaultMethod();
    
    @Override
    public void setMethod(int index, CalculationMethod<M> cm) {
        synchronized(lock) {
            CalculationMethod oldMethod = deleteMethod(index);
            if(cm != null) {
                MethodEntry entry = getEntry(cm.getId());
                if(entry == null) {
                    entry = new MethodEntry(cm);
                    entries.add(entry);
                }
                entry.indices.add(index);
            }
            
            methodsChanged();
            if(cm == null)
                cm = getDefaultMethod();
            events.fireMethodChanged(index, oldMethod, cm);
        }
    }
    
    protected abstract void methodsChanged();
    
    private CalculationMethod<M> deleteMethod(int index) {
        if(fixed.containsIndex(index)) {
            fixed.set(index, Double.NaN);
            return fixed;
        }
        
        Integer objIndex = index;
        Iterator<MethodEntry> it = entries.iterator();
        while(it.hasNext()) {
            MethodEntry me = it.next();
            if(me.indices.contains(objIndex)) {
                me.indices.remove(objIndex);
                if(me.indices.isEmpty())
                    it.remove();
                return me.method;
            }
        }
        
        return getDefaultMethod();
    }
    
    private MethodEntry getEntry(String id) {
        for(MethodEntry me : entries)
            if(me.method.getId().equals(id))
                return me;
        return null;
    }
    
    @Override
    public void setFixedValue(int index, double value) {
        synchronized(lock) {
            CalculationMethod oldMethod = deleteMethod(index);
            fixed.set(index, value);
            CalculationMethod newMethod = Double.isNaN(value)? getDefaultMethod() : fixed;
            methodsChanged();
            events.fireMethodChanged(index, oldMethod, newMethod);
        }
    }
    
    protected Element toXml(String rootName) {
        synchronized(lock) {
            Element root = new Element(rootName);
            for(MethodEntry entry : entries)
                root.addContent(toXml(entry));
            root.addContent(fixed.toXml());
            return root;
        }
    }
    
    private Element toXml(MethodEntry me) {
        Element root = me.method.toXml();
        for(int index : me.indices)
            JDomUtil.addElement(root, INDEX_ELEMENT, index);
        return root;
    }
    
    protected final Map<Integer, M> createMethods() {
        Map<Integer, M> methods = new HashMap<Integer, M>();
        for(MethodEntry entry : entries) {
            M method = entry.method.createMethod();
            for(Integer index : entry.indices)
                methods.put(index, method);
        }
        
        M method = fixed.createMethod();
        for(Integer index : fixed.indices)
            methods.put(index, method);
        
        return methods;
    }
    
    private class MethodEntry {
        private CalculationMethod<M> method;
        private Set<Integer> indices;

        private MethodEntry(CalculationMethod<M> method) {
            this(method, new TreeSet<Integer>());
        }

        private MethodEntry(CalculationMethod<M> method, Set<Integer> indices) {
            this.method = method;
            this.indices = indices;
        }
    }
    
    protected abstract FixedValueMethod getFixedMethod();
    
    private class FixedCalculationMethod implements CalculationMethod<M> {

        private final Set<Integer> indices = new HashSet<Integer>();
        
        @Override
        public M createMethod() {
            return (M) getFixedMethod();
        }
        
        private boolean containsIndex(int index) {
            return indices.contains(index);
        }
        
        private void set(int index, double value) {
            getFixedMethod().setValue(index, value);
            if(Double.isNaN(value))
                indices.remove(index);
            else
                indices.add(index);
        }

        @Override
        public Element toXml() {
            Element root = new Element(CONSTANTS_ELEMENT);
            FixedValueMethod m = getFixedMethod();
            for(int i : indices) {
                double value = m.getValue(i);
                if(Double.isNaN(value)) 
                    root.addContent(createElement(i, value));
            }
            return root;
        }
        
        private Element createElement(int index, double value) {
            Element root = new Element(CONSTANT_ELEMENT);
            JDomUtil.addElement(root, INDEX_ELEMENT, index);
            JDomUtil.addElement(root, VALUE_ELEMENT, value);
            return root;
        }

        @Override
        public String getDisplayName() {
            return "fixed";
        }

        @Override
        public String getId() {
            return FixedCalculationMethod.class.getName();
        }
    }
}
