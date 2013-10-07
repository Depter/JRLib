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
package org.jreserve.gui.calculations.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.jreserve.gui.calculations.api.edit.Excludeable;
import org.jreserve.gui.misc.utils.actions.AbstractContextAwareAction;
import org.jreserve.gui.misc.utils.actions.RibbonRegistration;
import org.netbeans.api.annotations.common.StaticResource;
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
    category = "Edit",
    id = "org.jreserve.gui.calculations.actions.ExcludeAction"
)
@ActionRegistration(
    displayName = "#LBL.ExcludeAction.Name",
    lazy = false
)
@RibbonRegistration(
    path="Ribbon/TaskPanes/Edit/Calculation", 
    priority = RibbonRegistration.Priority.TOP,
    position=100)
@Messages({
    "LBL.ExcludeAction.Name=Exclude"
})
public class ExcludeAction extends AbstractContextAwareAction {
    @StaticResource private final static String ICON_16 = "org/jreserve/gui/calculations/icons/exclude.png";   //NOI18
    @StaticResource private final static String ICON_32 = "org/jreserve/gui/calculations/icons/exclude32.png"; //NOI18
    
    private Excludeable excludeable;
    
    public ExcludeAction() {
        this(Utilities.actionsGlobalContext());
    }
    
    public ExcludeAction(Lookup context) {
        super(context, Excludeable.class);
        super.setDisplayName(Bundle.LBL_ExcludeAction_Name());
        super.setSmallIconPath(ICON_16);
        super.setLargeIconPath(ICON_32);
    }
    
    @Override
    protected boolean shouldEnable(Lookup context) {
        excludeable = super.getFirst(Excludeable.class);
        if(excludeable == null)
            return false;
        return excludeable.canExclude(context);
    }

    @Override
    protected void performAction(ActionEvent evt) {
        excludeable.exclude(super.getContext());
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new ExcludeAction(actionContext);
    }
}
