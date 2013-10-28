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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.jdom2.Element;
import org.jreserve.jrlib.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface CalculationModifierFactory<C extends CalculationData> {
    
    public CalculationModifier<C> fromXml(Element element) throws Exception;
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Registration {
        //The Category, to which factory belongs (exclusion, correction, smoothing)
        String category();
        //The xml root name for the factory
        String rootName();
        int position() default Integer.MAX_VALUE;
    }
}
