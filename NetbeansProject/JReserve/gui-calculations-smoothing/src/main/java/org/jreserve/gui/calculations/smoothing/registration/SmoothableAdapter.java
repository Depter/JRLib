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
package org.jreserve.gui.calculations.smoothing.registration;

import org.jreserve.gui.calculations.smoothing.Smoothable;
import org.jreserve.gui.misc.annotations.AnnotationUtils;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothableAdapter {

    private final Smoothable delegate;
    private final String name;
    private final String iconBase;
    private final boolean separatorBefore;
    private final boolean separatorAfter;
    
    SmoothableAdapter(FileObject file) throws Exception {
        delegate = (Smoothable) AnnotationUtils.loadInstance(file);
        name = AnnotationUtils.stringAttribute(SmoothableRegistrationProcessor.DISPLAY_NAME, file);
        iconBase = AnnotationUtils.stringAttribute(SmoothableRegistrationProcessor.ICON_BASE, file);
        separatorBefore = AnnotationUtils.booleanAttribute(SmoothableRegistrationProcessor.SEPARATOR_BEFORE, file, false);
        separatorAfter = AnnotationUtils.booleanAttribute(SmoothableRegistrationProcessor.SEPARATOR_AFTER, file, false);
    }
    
    public String getIconBase() {
        return iconBase;
    }

    public String getName() {
        return name;
    }
    
    public boolean separatorBefore() {
        return separatorBefore;
    }
    
    public boolean separatorAfter() {
        return separatorAfter;
    }
    
    public Smoothable getSmoothable() {
        return delegate;
    }
}
