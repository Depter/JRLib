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

package org.jreserve.gui.data.binaryprovider;

import java.util.Collections;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.spi.DataProvider;
import org.jreserve.gui.data.spi.DataSourceWizard;
import org.openide.WizardDescriptor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class BinaryDataSourceWizard implements DataSourceWizard {
    
    @Override
    public List<? extends WizardDescriptor.Panel> getPanels() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public DataProvider createDataProvider(WizardDescriptor wizard) {
        return new BinaryDataProvider(BinaryDataProviderFactory.getInstance());
    }
    
    @Override
    public void addChangeListener(ChangeListener listener) {
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
    }
}
