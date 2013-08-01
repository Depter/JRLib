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
package org.jreserve.gui.data.actions.createsourcewizard;

import java.util.List;
import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.DataType;
import org.jreserve.gui.data.spi.DataProvider;
import org.jreserve.gui.data.spi.DataSourceWizard;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataSourceWizardAdapter implements DataSourceWizard {
    
    private final String name;
    private final Icon icon;
    private final DataSourceWizard delegate;
    private final ChangeSupport cs;
    
    DataSourceWizardAdapter(String name, Icon icon, DataSourceWizard delegate) {
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
    
    Icon getIcon() {
        return icon;
    }
    
    String getName() {
        return name;
    }
    
    @Override
    public List<? extends WizardDescriptor.Panel> getPanels() {
        return delegate.getPanels();
    }

    @Override
    public DataProvider createDataProvider(DataType dataType, WizardDescriptor wizard) {
        return delegate.createDataProvider(dataType, wizard);
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }
}
