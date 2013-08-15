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
package org.jreserve.gui.excel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import org.jreserve.gui.excel.poiutil.ReferenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ReferenceComboModel extends AbstractListModel implements ComboBoxModel {

    private List<String> names = new ArrayList<String>();
    private Object selected;

    public void update(ReferenceUtil refUtil) {
        removeOldNames();
        addNewNames(refUtil);
        if(!names.isEmpty())
            fireIntervalAdded(this, 0, names.size() - 1);
    }

    private void removeOldNames() {
        int size = names.size();
        if(size > 0) {
            names.clear();
            fireIntervalRemoved(this, 0, size - 1);
        }
    }
    
    private void addNewNames(ReferenceUtil refUtil) {
        if(refUtil != null) {
            for(String name : refUtil.getNames())
                names.add(name);
        }
    }
    
    @Override
    public int getSize() {
        return names == null ? 0 : names.size();
    }

    @Override
    public Object getElementAt(int index) {
        return names.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        this.selected = anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }
}
