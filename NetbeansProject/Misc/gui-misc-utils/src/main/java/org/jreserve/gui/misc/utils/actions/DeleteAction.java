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
package org.jreserve.gui.misc.utils.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.awt.ActionID;
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
    id = "org.jreserve.gui.misc.utils.notifications.actions.DeleteAction"
)
@ActionRegistration(
    displayName = "#CTL.DeleteAction",
    lazy = false
)
@Messages({
    "CTL.DeleteAction=Delete"
})
public class DeleteAction extends AbstractContextAwareAction {

    public static DeleteAction createSmall(Lookup context) {
        DeleteAction action = new DeleteAction();
        action.putValue(Action.LARGE_ICON_KEY, null);
        return action;
    }
    
    @StaticResource private final static String SMALL_ICON = "org/jreserve/gui/misc/utils/delete.png";   //NOI18
    @StaticResource private final static String LARGE_ICON = "org/jreserve/gui/misc/utils/delete32.png"; //NOI18
    
    public DeleteAction() {
        this(Utilities.actionsGlobalContext());
    }
    
    public DeleteAction(Lookup ctx) {
        super(ctx);
        putValue(Action.NAME, Bundle.CTL_DeleteAction());
        putValue(Action.LARGE_ICON_KEY, ImageUtilities.loadImageIcon(LARGE_ICON, false));
        putValue(Action.SMALL_ICON, ImageUtilities.loadImageIcon(SMALL_ICON, false));
    }
    
    @Override
    protected boolean shouldEnable(Lookup context) {
        return !context.lookupAll(Deletable.class).isEmpty();
    }

    @Override
    protected void performAction(ActionEvent evt) {
        new DeleteDialog(getAll(Deletable.class)).showDialog();
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new DeleteAction(actionContext);
    }
}
