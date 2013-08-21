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
package org.jreserve.gui.data.actions;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import javax.swing.Action;
import org.jreserve.gui.data.actions.createsourcewizard.CreateDataSourceWizardIterator;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataManager;
import org.jreserve.gui.misc.utils.widgets.AbstractContextAwareAction;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "File",
    id = "org.jreserve.gui.data.actions.CreateDataSourceAction"
)
@ActionRegistration(
    displayName = "#CTL_CreateDataSourceAction",
    lazy = false
)
@ActionReferences({
    @ActionReference(path = "Ribbon/TaskPanes/Edit/Data", position = 200),
    @ActionReference(path = "Node/DataCategory/Actions", position = 200, separatorAfter = 250)
})
@Messages({
    "CTL_CreateDataSourceAction=New Storage",
    "LBL.CreateDataSourceAction.Wizard.Title=New Data Source"
})
public class CreateDataSourceAction extends AbstractContextAwareAction {
    
    @StaticResource private final static String SMALL_ICON = "org/jreserve/gui/data/icons/database_add.png";   //NOI18
    @StaticResource private final static String LARGE_ICON = "org/jreserve/gui/data/icons/database_add32.png"; //NOI18
    
    private DataCategory dataCategory;
    
    public CreateDataSourceAction() {
        this(Utilities.actionsGlobalContext());
    }
 
    private CreateDataSourceAction(Lookup context) {
        super(context);
        putValue(Action.NAME, Bundle.CTL_CreateDataSourceAction());
        super.putValue(Action.LARGE_ICON_KEY, ImageUtilities.loadImageIcon(LARGE_ICON, false));
        super.putValue(Action.SMALL_ICON, ImageUtilities.loadImageIcon(SMALL_ICON, false));
    }
    
    @Override
    protected boolean shouldEnable(Lookup context) {
        dataCategory = context.lookup(DataCategory.class);
        if(dataCategory != null)
            return true;
        
        Project project = context.lookup(Project.class);
        if(project == null)
            return false;
        
        DataManager dm = project.getLookup().lookup(DataManager.class);
        if(dm == null)
            return false;
        dataCategory = dm.getCategory(null);
        return dataCategory != null;
    }

    @Override
    protected void performAction(ActionEvent evt) {
        WizardDescriptor wiz = new WizardDescriptor(new CreateDataSourceWizardIterator(dataCategory));
        wiz.setTitleFormat(new MessageFormat("{0} ({1})"));
        wiz.setTitle(Bundle.LBL_CreateDataSourceAction_Wizard_Title());
        DialogDisplayer.getDefault().notify(wiz);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new CreateDataSourceAction(lkp);
    }
}
