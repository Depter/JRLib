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

package org.jreserve.gui.data.csv.input;

import java.util.Collections;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.spi.ImportDataProvider;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ImportDataProvider.Registration(
    id = "org.jreserve.gui.data.csv.input.CsvTableImportDataProvider",
    displayName = "#LBL.CsvTableImportDataProvider.Name",
    iconBase = "org/jreserve/gui/data/csv/csv.png",
    position = 5000
)
@Messages({
    "LBL.CsvTableImportDataProvider.Name=CSV Table"
})
public class CsvTableImportDataProvider implements ImportDataProvider {

    private final ChangeSupport cs = new ChangeSupport(this);
    private List<? extends WizardDescriptor.Panel> panels;
    
    @Override
    public List<? extends WizardDescriptor.Panel> getPanels() {
        if(panels == null)
            panels = Collections.singletonList(new CsvTableImportWizardPanel());
        return panels;
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
