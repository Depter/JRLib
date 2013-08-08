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

package org.jreserve.gui.data.csv.input.triangle;

import java.util.Arrays;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.ImportDataWizardPanelGeometry;
import org.jreserve.gui.data.spi.ImportDataProvider;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ImportDataProvider.Registration(
    id = "org.jreserve.gui.data.csv.input.triangle.CsvTableImportDataProvider",
    displayName = "#LBL.CsvTriangleImportDataProvider.Name",
    iconBase = "org/jreserve/gui/data/csv/csv.png",
    position = 6000
)
@Messages({
    "LBL.CsvTriangleImportDataProvider.Name=CSV Triangle"
})
public class CsvTriangleImportDataProvider implements ImportDataProvider {
    
    private WizardDescriptor.Panel[] panels;
    
    @Override
    public List<? extends WizardDescriptor.Panel> getPanels() {
        if(panels == null) {
            panels = new WizardDescriptor.Panel[2];
            panels[0] = new CsvTriangleImportWizardPanel1();
            panels[1] = new ImportDataWizardPanelGeometry();
        }
        return Arrays.asList(panels);
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
    }
}
