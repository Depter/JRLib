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
package org.jreserve.gui.plot.colors;

import javax.swing.Icon;
import org.jreserve.gui.misc.annotations.AnnotationUtils;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.misc.utils.widgets.EmptyIcon;
import org.jreserve.gui.plot.ColorGenerator;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractColorGeneratorAdapter 
    implements Displayable, 
               Comparable<AbstractColorGeneratorAdapter>, 
               ColorGenerator.Factory {
    
    private String id;
    private int position;
    private String displayName;
    
    AbstractColorGeneratorAdapter(FileObject fo) {
        id = AnnotationUtils.stringAttribute(ColorGeneratorRegistrationProcessor.ID, fo);
        displayName = AnnotationUtils.stringAttribute(ColorGeneratorRegistrationProcessor.DISPLAY_NAME, fo, id);
        position = AnnotationUtils.intAttribute("position", fo, Integer.MAX_VALUE);
    }
    
    public String getId() {
        return id;
    }
    
    public int getPosition() {
        return position;
    }
    
    @Override
    public Icon getIcon() {
        return EmptyIcon.EMPTY_16;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int compareTo(AbstractColorGeneratorAdapter o) {
        int dif = position - o.position;
        return dif!=0? dif : displayName.compareTo(o.displayName);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof AbstractColorGeneratorAdapter) &&
               id.equals(((AbstractColorGeneratorAdapter)o).id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
