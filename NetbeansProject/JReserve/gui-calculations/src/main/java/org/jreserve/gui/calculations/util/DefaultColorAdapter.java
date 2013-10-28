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
public class DefaultColorAdapter {
    
    private final static Logger logger = Logger.getLogger(DefaultColorAdapter.class.getName());
    
    private String id;
    private String displayName;
    private Color color;
    
    DefaultColorAdapter(FileObject file) {
        id = AnnotationUtils.stringAttribute(DefaultColorRegistrationProcessor.ID, file);
        displayName = AnnotationUtils.stringAttribute(DefaultColorRegistrationProcessor.DISPLAY_NAME, file, id);
        color = getColor(file);
    }
    
    private Color getColor(FileObject file) {
        String hex = AnnotationUtils.stringAttribute(DefaultColorRegistrationProcessor.COLOR, file);
        if(hex == null || hex.length() == 0) {
            String msg = "Default color definition '%s' does not contain a color attribute!";
            logger.warning(String.format(msg, file.getPath()));
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

    public Color getColor() {
        return color;
    }
}
