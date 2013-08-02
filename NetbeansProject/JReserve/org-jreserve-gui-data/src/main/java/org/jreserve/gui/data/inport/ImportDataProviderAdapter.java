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
package org.jreserve.gui.data.inport;

import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.spi.ImportDataProvider;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.openide.util.ChangeSupport;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ImportDataProviderAdapter implements Displayable {
    
    private final String name;
    private final Icon icon;
    private final ImportDataProvider delegate;
    private final ChangeSupport cs;
    
    ImportDataProviderAdapter(String name, Icon icon, ImportDataProvider delegate) {
        this.name = name;
        this.icon = icon;
        this.delegate = delegate;
        cs = new ChangeSupport(this);
        delegate.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cs.fireChange();
            }
        });
    }
    
    @Override
    public Icon getIcon() {
        return icon;
    }
    
    @Override
    public String getDisplayName() {
        return name;
    }
    
    public ImportDataProvider getImportDataProvider() {
        return delegate;
    }
    
    public void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }
}
