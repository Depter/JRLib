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
package org.jreserve.gui.misc.utils.widgets;

import java.awt.Component;
import java.lang.reflect.Array;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JList;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EnumComboBox<T extends Enum<T>> extends JComboBox {
    
    private static <T extends Enum<T>> Map<T, String> createNames(Class<T> clazz) {
        Map<T, String> names = new EnumMap<T, String>(clazz);
        for(T item : clazz.getEnumConstants()) {
            if(item instanceof Displayable) {
                names.put(item, ((Displayable)item).getDisplayName());
            } else {
                names.put(item, item.name());
            }
        }
        return names;
    }
    
    private static <T extends Enum<T>> Map<T, Icon> createIcons(Class<T> clazz) {
        Map<T, Icon> icons = new EnumMap<T, Icon>(clazz);
        for(T item : clazz.getEnumConstants()) {
            if(item instanceof Displayable) {
                icons.put(item, ((Displayable)item).getIcon());
            } else {
                icons.put(item, EmptyIcon.EMPTY_16);
            }
        }
        return icons;
    }
    
    private final Class<T> clazz;
    private final Map<T, String> names;
    private final Map<T, Icon> icons;
    
    public EnumComboBox(Class<T> clazz) {
        this(clazz, createNames(clazz));
    }
    
    public EnumComboBox(Class<T> clazz, Map<T, String> names) {
        this(clazz, names, createIcons(clazz));
    }
    
    public EnumComboBox(Class<T> clazz, Map<T, String> names, Map<T, Icon> icons) {
        super(clazz.getEnumConstants());
        this.clazz = clazz;
        this.names = names==null? new EnumMap<T, String>(clazz) : names;
        this.icons = icons==null? new EnumMap<T, Icon>(clazz) : icons;
        setRenderer(new EnumComboRenderer());
    }

    @Override
    public void setSelectedItem(Object item) {
        if(item != null && !clazz.isAssignableFrom(item.getClass()))
            throw new IllegalArgumentException(String.format("Can not set '%s' as instance of '%s'!", item, clazz));
        super.setSelectedItem(item);
    }

    @Override
    public T getSelectedItem() {
        return (T) super.getSelectedItem();
    }

    @Override
    public T[] getSelectedObjects() {
        Object[] sel = super.getSelectedObjects();
        int size = sel.length;
        T[] values = (T[])Array.newInstance(clazz, size);
        for(int i=0; i<size; i++)
            values[i] = (T) sel[i];
        return values;
    }

    @Override
    public T getItemAt(int index) {
        return (T) super.getItemAt(index); //To change body of generated methods, choose Tools | Templates.
    }
    
    private class EnumComboRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if(value != null && clazz.isAssignableFrom(value.getClass())) {
                T item = (T) value;
                setText(names.get(item));
                setIcon(icons.get(item));
            } else {
                setIcon(null);
            }
            
            return this;
        }
    }
}
