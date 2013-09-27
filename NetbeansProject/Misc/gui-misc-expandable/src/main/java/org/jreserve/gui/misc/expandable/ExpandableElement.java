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

import java.awt.Component;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.swing.JComponent;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ExpandableElement extends Lookup.Provider {

    public Lookup getGlobalLookup();
    
    public Component getVisualComponent();
    
    public JComponent[] getFrameComponents();
    
    public void setHandler(ExpandableComponentHandler handler);
    
    public ExpandableComponentHandler getHandler();
    
    public void componentOpened();

    public void componentClosed();

    public void componentShowing();

    public void componentHidden();

    public void componentActivated();

    public void componentDeactivated();
    
    public CloseOperationState canCloseElement();
    
    @Retention(value = RetentionPolicy.SOURCE)
    @Target(value = ElementType.TYPE)
    public static @interface Registration {
        
        public String displayName();
        
        public String mimeType();
        
        public String prefferedID() default "";
        
        public String iconBase() default "";
        
        public String background() default "43C443";//{67, 196, 67};
        
        public String foreground() default "FFFFFF";
        
        public int position() default Integer.MAX_VALUE;
    }
}
