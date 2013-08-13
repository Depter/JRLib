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
package org.jreserve.gui.excel.template.dataimport;

import java.text.MessageFormat;
import org.jreserve.gui.excel.template.ExcelTemplateBuilder;
import org.jreserve.gui.excel.template.dataimport.createwizard.CreateTemplateWizardIterator;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataImportTemplateBuilder.WizardTitle=Create Data Import Tempalte"
})
class DataImportTemplateBuilder implements ExcelTemplateBuilder {
    
    private final DataImportTemplates templateManager;

    DataImportTemplateBuilder(DataImportTemplates templateManager) {
        this.templateManager = templateManager;
    }
    
    @Override
    public void buildTemplate() {
        WizardDescriptor wiz = new WizardDescriptor(new CreateTemplateWizardIterator(templateManager));
        wiz.setTitleFormat(new MessageFormat("{0} ({1})"));
        wiz.setTitle(Bundle.LBL_DataImportTemplateBuilder_WizardTitle());
        DialogDisplayer.getDefault().notify(wiz);
    }
}
