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

import javax.swing.event.ChangeListener;
import org.jreserve.jrlib.CalculationData;
import org.openide.nodes.Node;
import org.openide.util.ChangeSupport;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCalculationModifier<C extends CalculationData> implements CalculationModifier<C>{

    private final ChangeSupport cs = new ChangeSupport(this);
    private final Class<C> clazz;
    private Node node;
    
    protected AbstractCalculationModifier(Class<C> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public Class<? extends C> getCalculationClass() {
        return clazz;
    }

    @Override
    public synchronized Node getNodeDelegate() {
        if(node == null)
            node = createNodeDelegate();
        return node;
    }
    
    protected abstract Node createNodeDelegate();

    @Override
    public void addChangeListener(ChangeListener changeListener) {
        cs.addChangeListener(changeListener);
    }

    @Override
    public void removeChangeListener(ChangeListener changeListener) {
        cs.removeChangeListener(changeListener);
    }
    
    protected void fireChange() {
        cs.fireChange();
    }
}
