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
package org.jreserve.gui.misc.expandable.registration;

import org.jreserve.gui.misc.annotations.AnnotationUtils;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.jreserve.gui.misc.expandable.ExpandableElementDescription;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class FileAdapter implements ExpandableElementDescription {

    private ExpandableElement element;
    private String name;
    private String iconBase;
    private String prefferedId;
    private int position;
    
    FileAdapter(FileObject file, Lookup context) {
        name = AnnotationUtils.stringAttribute(ExpandableElementRegistrationProcessor.NAME, file);
        prefferedId = AnnotationUtils.stringAttribute(ExpandableElementRegistrationProcessor.PREFFERED_ID, file);
        position = AnnotationUtils.intAttribute(ExpandableElementRegistrationProcessor.POSITION, file, Integer.MAX_VALUE);
        initIconBase(file);
        initElement(file, context);
    }
    
    private void initIconBase(FileObject file) {
        iconBase = AnnotationUtils.stringAttribute(ExpandableElementRegistrationProcessor.ICON, file);
        if(iconBase!=null && iconBase.trim().length()==0)
            iconBase = null;
    }
    
    private void initElement(FileObject file, Lookup context) {
        element = (context == null)?
                (ExpandableElement) AnnotationUtils.instantiate(file) :
                (ExpandableElement) AnnotationUtils.instantiate(file, context);
    }
    
    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public String getIconBase() {
        return iconBase;
    }

    @Override
    public String getPrefferedID() {
        return prefferedId;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public ExpandableElement getElement() {
        return element;
    }
}
