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
package org.jreserve.gui.misc.renameable.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.gui.misc.renameable.Renameable;
import org.jreserve.gui.misc.renameable.dialog.RenameDialog;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "File",
    id = "org.jreserve.gui.misc.renameable.action.RenameAction"
)
@ActionRegistration(displayName = "#CTL_RenameAction")
@Messages("CTL_RenameAction=Rename")
public final class RenameAction implements ActionListener {

    private final Renameable context;

    public RenameAction(Renameable context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        RenameDialog.rename(context);
    }
}
