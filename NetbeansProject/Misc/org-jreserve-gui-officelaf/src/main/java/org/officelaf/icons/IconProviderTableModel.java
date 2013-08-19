package org.officelaf.icons;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;
import org.officelaf.spi.IconProvider;

/**
 *
 * @author mikael
 */
public class IconProviderTableModel extends AbstractTableModel {
    enum Column {
        NAME(    "Name",      String.class,    70),
        SIZE(    "Size",      Dimension.class, 15),
        SCALABLE("Scaleable", Boolean.class,   15);
        
        String name;
        Class type;
        int width;

        private Column(String name, Class type, int width) {
            this.name  = name;
            this.type  = type;
            this.width = width;
        }

        public String getName() {
            return name;
        }

        public Class getType() {
            return type;
        }

        public int getWidth() {
            return width;
        }
    };
    
    IconProvider provider;
    List<String> icons;
    
    public IconProviderTableModel() {
        this(new AbstractIconProvider() {
            public Set<String> getNames() {
                return Collections.EMPTY_SET;
            }
        });
    }

    public IconProviderTableModel(IconProvider provider) {
        this.provider = provider;
        icons = new ArrayList<String>(provider.getNames());
    }

    
    @Override
    public int getRowCount() {
        return icons.size();
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Class getColumnClass(int column) {
        return Column.values()[column].getType();
    }

    @Override
    public String getColumnName(int column) {
        return Column.values()[column].getName();
    }

    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object retVal = null;
        
        String name = icons.get(rowIndex);
        switch(columnIndex) {
            case 0:
                retVal = name;
                break;
            case 1:
                Icon icon = provider.getIcon(name);
                if(icon != null)
                    retVal = new Dimension(icon.getIconWidth(), icon.getIconHeight());
                break;
            case 2:
                retVal = provider.isScalable(name);
                
        }
        
        return retVal;
    }
}
