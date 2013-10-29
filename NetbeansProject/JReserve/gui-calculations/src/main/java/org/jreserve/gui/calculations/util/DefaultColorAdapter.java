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
package org.jreserve.gui.calculations.util;

import java.awt.Color;
import java.util.logging.Logger;
import org.jreserve.gui.misc.annotations.AnnotationUtils;
import org.jreserve.gui.misc.utils.widgets.ColorUtil;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultColorAdapter implements Comparable<DefaultColorAdapter>{
    
    private final static Logger logger = Logger.getLogger(DefaultColorAdapter.class.getName());
    
    private String id;
    private String displayName;
    private Color background;
    private Color foreground;
    
    DefaultColorAdapter(FileObject file) {
        id = AnnotationUtils.stringAttribute(DefaultColorRegistrationProcessor.ID, file);
        displayName = AnnotationUtils.stringAttribute(DefaultColorRegistrationProcessor.DISPLAY_NAME, file, id);
        background = getColor(file, DefaultColorRegistrationProcessor.BACKGROUND);
        foreground = getColor(file, DefaultColorRegistrationProcessor.FOREGROUND);
    }
    
    private Color getColor(FileObject file, String property) {
        String hex = AnnotationUtils.stringAttribute(property, file);
        if(hex == null || hex.length() == 0) {
            String msg = "Default color definition '%s' does not contain a color for attribute '%s'!";
            logger.warning(String.format(msg, file.getPath(), property));
            return Color.BLACK;
        }
        return ColorUtil.parseColor(hex);
    }
    
    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Color getBackground() {
        return background;
    }

    public Color getForeground() {
        return foreground;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof DefaultColorAdapter) &&
               id.equals(((DefaultColorAdapter)o).id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public int compareTo(DefaultColorAdapter o) {
        return displayName.compareTo(o.displayName);
    }
}
