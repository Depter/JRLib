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
package org.jreserve.gui.misc.expandable;

import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.swing.JComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ExpandableElement {
    
    public JComponent getVisualComponent();
    
    public JComponent[] getFrameComponents();
    
    public Color getBackground();
    
    public Color getForeground();
    
    @Retention(value = RetentionPolicy.SOURCE)
    @Target(value = ElementType.TYPE)
    public static @interface Registration {
        
        public String displayName();
        
        public String mimeType();
        
        public String prefferedID() default "";
        
        public String iconBase() default "";
        
        public int position() default Integer.MAX_VALUE;
    }
}
