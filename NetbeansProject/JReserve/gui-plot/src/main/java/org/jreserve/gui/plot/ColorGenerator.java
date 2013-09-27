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
package org.jreserve.gui.plot;

import java.awt.Paint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ColorGenerator {
	
    public Paint nextColor();
    
    public void reset();
                            
    public static interface Factory {
        public ColorGenerator createColorGenerator();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface ListRegistration {
        public String id();
        public String displayName();
        public int position() default Integer.MAX_VALUE;
        public String[] colors();
    }
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Registration {
        public String id();
        public String displayName();
        public int position() default Integer.MAX_VALUE;
    }
}
