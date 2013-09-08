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

package org.jreserve.gui.excel.dataimport;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.spi.inport.ImportDataWizardPanelGeometry;
import org.jreserve.gui.data.spi.inport.ImportDataWizardPanelLast;
import org.jreserve.gui.data.spi.inport.ImportDataProvider;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ImportDataProvider.Registration(
    id = "org.jreserve.gui.excel.dataimport.ExcelTriangleImportDataProvider",
    displayName = "#LBL.ExcelTriangleImportDataProvider.Name",
    iconBase = "org/jreserve/gui/excel/excel.png",
    position = 3000
)
@Messages({
    "LBL.ExcelTriangleImportDataProvider.Name=Excel Triangle"
})
public class ExcelTriangleImportDataProvider implements ImportDataProvider {
    
    private List<WizardDescriptor.Panel> panels;
    
    @Override
    public List<? extends WizardDescriptor.Panel> getPanels() {
        if(panels == null) {
            panels = new ArrayList<WizardDescriptor.Panel>(3);
            panels.add(new ExcelTriangleImportWizardPanel());
            panels.add(new ImportDataWizardPanelGeometry());
            panels.add(new ImportDataWizardPanelLast());
        }
        return panels;
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
    }
}
