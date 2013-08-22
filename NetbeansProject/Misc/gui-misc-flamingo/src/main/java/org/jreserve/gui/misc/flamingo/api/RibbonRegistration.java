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

package org.jreserve.gui.misc.flamingo.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Retention(value = RetentionPolicy.SOURCE)
@Target(value = {ElementType.TYPE})
public @interface RibbonRegistration {
    
    public String path();
    public String menuText() default "";
    public String description() default "";
    public String iconBase() default "";
    public String tooltipBody() default "";
    public String tooltipTitle() default "";
    public String tooltipIcon() default "";
    public String tooltipFooter() default "";
    public String tooltipFooterIcon() default "";
    
    public int separatorBefore() default Integer.MAX_VALUE;
    public int separatorAfter() default Integer.MAX_VALUE;
    public int position() default Integer.MAX_VALUE;
}
