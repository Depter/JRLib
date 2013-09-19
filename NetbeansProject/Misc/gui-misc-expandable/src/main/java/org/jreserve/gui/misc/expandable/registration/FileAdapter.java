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

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private final static Logger logger = Logger.getLogger(FileAdapter.class.getName());
    private final static Color DEFAULT_BG = Color.BLACK;
    private final static Color DEFAULT_FG = Color.WHITE;
    
    private ExpandableElement element;
    private String name;
    private String iconBase;
    private String prefferedId;
    private Color background;
    private Color foreground;
    private int position;
    
    FileAdapter(FileObject file, Lookup context) {
        name = AnnotationUtils.stringAttribute(ExpandableElementRegistrationProcessor.DISPLAY_NAME, file);
        prefferedId = AnnotationUtils.stringAttribute(ExpandableElementRegistrationProcessor.PREFFERED_ID, file);
        position = AnnotationUtils.intAttribute(ExpandableElementRegistrationProcessor.POSITION, file, Integer.MAX_VALUE);
        initIconBase(file);
        initBackground(file);
        initForeground(file);
        initElement(file, context);
    }
    
    private void initIconBase(FileObject file) {
        iconBase = AnnotationUtils.stringAttribute(ExpandableElementRegistrationProcessor.ICON, file);
        if(iconBase!=null && iconBase.trim().length()==0)
            iconBase = null;
    }
    
    private void initBackground(FileObject file) {
        background = getColor(file, ExpandableElementRegistrationProcessor.BACKGROUND);
        if(background == null) {
            background = DEFAULT_BG;
            logger.log(Level.WARNING, "Unable to read background color for ExpandableElementDescription! Color.BLACK will be used instead.");
        }
    }
    
    private Color getColor(FileObject file, String name) {
        String hex = (String) file.getAttribute(name);
        if(hex != null && hex.length()==6 && hex.matches("[0-9A-Fa-f]+"))
            return parseColor(hex);
        return null;
    }
    
    private Color parseColor(String hex) {
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new Color(r, g, b);
    }
    
    private void initForeground(FileObject file) {
        foreground = getColor(file, ExpandableElementRegistrationProcessor.FOREGROUND);
        if(foreground == null) {
            foreground = DEFAULT_FG;
            logger.log(Level.WARNING, "Unable to read background color for ExpandableElementDescription! Color.WHITE will be used instead.");
        }
    }
    
    private void initElement(FileObject file, Lookup context) {
        if(context == null) {
            element = (ExpandableElement) AnnotationUtils.instantiate(file);
        } else {
            try {
                element = (ExpandableElement) AnnotationUtils.instantiate(file, context);
            } catch (Exception ex) {
                element = (ExpandableElement) AnnotationUtils.instantiate(file);
            }
        }
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

    @Override
    public Color getBackground() {
        return background;
    }

    @Override
    public Color getForeground() {
        return foreground;
    }
}
