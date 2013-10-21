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
package org.jreserve.gui.misc.namedcontent.folder;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import javax.swing.Action;
import org.jreserve.gui.misc.namedcontent.NamedContentProvider;
import org.jreserve.gui.misc.utils.actions.AbstractContextAwareAction;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.netbeans.api.project.Project;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
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
    id = "org.jreserve.gui.misc.namedcontent.folder.CreateFolderAction"
)
@ActionRegistration(
    displayName = "#LBL.CreateFolderAction.Name",
    lazy = false
)
@Messages({
    "LBL.CreateFolderAction.Name=Create Folder",
    "LBL.CreateFolderAction.WizardTitle=Create Folder"
})
public class CreateFolderAction extends AbstractContextAwareAction {
    
    public CreateFolderAction() {
        this(Utilities.actionsGlobalContext());
    }

    public CreateFolderAction(Lookup context) {
        super(context, Object.class);
        setDisplayName(Bundle.LBL_CreateFolderAction_Name());
        super.putValue(SMALL_ICON, CommonIcons.folder());
    }
    
    @Override
    protected boolean shouldEnable(Lookup context) {
        NamedContentProvider ncp = context.lookup(NamedContentProvider.class);
        if(ncp != null)
            return true;
        
        Project project = context.lookup(Project.class);
        if(project == null)
            return false;
        
        return !project.getLookup().lookupAll(NamedContentProvider.class).isEmpty();
    }

    @Override
    protected void performAction(ActionEvent evt) {
        WizardDescriptor wiz = new WizardDescriptor(createIterator());
        wiz.setTitleFormat(new MessageFormat("{0} ({1})"));
        wiz.setTitle(Bundle.LBL_CreateFolderAction_WizardTitle());
        DialogDisplayer.getDefault().notify(wiz);
    }
    
    private CreateFolderIterator createIterator() {
        NamedContentProvider ncp = getContext().lookup(NamedContentProvider.class);
        if(ncp == null)
            return new CreateFolderIterator();
        return new CreateFolderIterator(ncp);
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new CreateFolderAction(actionContext);
    }
}
