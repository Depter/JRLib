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

import org.jdom2.Element;
import org.jreserve.jrlib.util.method.SelectableMethod;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 * @param <M> the method type.
 */
public interface CalculationMethod<M extends SelectableMethod> {
    
    public M createMethod();

    public Element toXml();
    
    public String getDisplayName();
    
    public String getId();
}
