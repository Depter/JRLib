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

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import javax.swing.Action;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.dataobject.DataSourceObjectProvider;
import org.jreserve.gui.misc.utils.actions.AbstractContextAwareAction;
import org.jreserve.gui.misc.utils.actions.RibbonRegistration;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.Node;
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
    id = "org.jreserve.gui.data.inport.ImportDataAction"
)
@ActionRegistration(
    displayName = "#CTL.ImportDataAction",
    lazy = false
)
@ActionReferences({
    @ActionReference(path = "Node/DataCategory/Actions", position = 300, separatorAfter = 350),
    @ActionReference(path = "Node/DataSource/Actions", position = 300, separatorAfter = 350)
})
@RibbonRegistration(
    path="Ribbon/TaskPanes/Edit/Data", 
    priority = RibbonRegistration.Priority.MEDIUM,
    position=200)
@Messages({
    "CTL.ImportDataAction=Import Data",
    "CTL.ImportDataAction.ToolTip=Import Data",
    "LBL.ImportDataAction.WizardTitle=Import Data"
})
public class ImportDataAction extends AbstractContextAwareAction {
    @StaticResource private final static String ICON_16 = "org/jreserve/gui/data/icons/import_data.png";   //NOI18
    @StaticResource private final static String ICON_32 = "org/jreserve/gui/data/icons/import_data32.png"; //NOI18
    
    public static ImportDataAction createSmall(Lookup lkp) {
        ImportDataAction action = new ImportDataAction(lkp);
        action.putValue(Action.LARGE_ICON_KEY, null);
        action.putValue(SHORT_DESCRIPTION, Bundle.CTL_ImportDataAction_ToolTip());
        return action;
    }
    
    private DataSource ds;
    private DataSourceObjectProvider dsop;
    
    public ImportDataAction() {
        this(Utilities.actionsGlobalContext());
    }
 
    public ImportDataAction(Lookup context) {
        super(context, Object.class);
        super.setDisplayName(Bundle.CTL_ImportDataAction());
        super.setSmallIconPath(ICON_16);
        super.setLargeIconPath(ICON_32);
    }

    @Override
    protected boolean shouldEnable(Lookup context) {
        ds = context.lookup(DataSource.class);
        dsop = context.lookup(DataSourceObjectProvider.class);
        if(dsop == null)
            dsop = lookupProvider(context.lookup(Node.class));
        return dsop != null;
    }
    
    private DataSourceObjectProvider lookupProvider(Node node) {
        if(node == null)
            return null;
        DataSourceObjectProvider p = node.getLookup().lookup(DataSourceObjectProvider.class);
        if(p != null)
            return p;
        return lookupProvider(node.getParentNode());
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new ImportDataAction(lkp);
    }

    @Override
    protected void performAction(ActionEvent evt) {
        WizardDescriptor wiz = new WizardDescriptor(new ImportDataWizardIterator(dsop, ds));
        wiz.setTitleFormat(new MessageFormat("{0} ({1})"));
        wiz.setTitle(Bundle.LBL_ImportDataAction_WizardTitle());
        DialogDisplayer.getDefault().notify(wiz);
    }
}
