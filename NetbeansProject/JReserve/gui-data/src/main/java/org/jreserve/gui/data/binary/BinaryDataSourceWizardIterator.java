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

package org.jreserve.gui.data.binary;

import java.io.IOException;
import org.jreserve.gui.data.api.wizard.AbstractDataSourceWizardIterator;
import org.netbeans.api.templates.TemplateRegistration;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@TemplateRegistration(
    folder = "Data",
    displayName = "#LBL.BinaryDataSourceWizardIterator.DisplayName",
    iconBase = "org/jreserve/gui/data/binaryprovider/binary.png",
    id = "org.jreserve.gui.data.binary.BinaryDataSourceWizardIterator",
    category = {"jreserve-data"},
    position = 100
)
@Messages({
    "LBL.BinaryDataSourceWizardIterator.DisplayName=Binary Storage"
})
public class BinaryDataSourceWizardIterator extends AbstractDataSourceWizardIterator {

    @Override
    public void initialize(WizardDescriptor wizard) {
        super.initialize(wizard);
        wizard.putProperty(AbstractDataSourceWizardIterator.PROP_FACTORY_ID, BinaryDataProviderFactory.ID);
    }
    
    @Override
    protected void createSecondaryFiles(FileObject primaryFile) throws IOException {
        FileObject parent = primaryFile.getParent();
        parent.createData(primaryFile.getName(), BinaryDataProvider.BINARY_EXTENSION);
    }
}
