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
package org.jreserve.grscript.gui.script.function.explorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.grscript.gui.script.function.explorer.FunctionItem;
import org.jreserve.grscript.gui.script.function.explorer.nodes.FunctionItemNode;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "Help",
    id = "org.jreserve.grscript.gui.script.function.explorer.actions.OpenHelpAction"
)
@ActionRegistration(displayName = "#CTL_OpenHelp")
@ActionReferences({
    @ActionReference(path = FunctionItemNode.ACTION_PATH, position = 1000)
})
@Messages("CTL_OpenHelp=Help")
public final class OpenHelpAction implements ActionListener {
    private final FunctionItem item;

    public OpenHelpAction(FunctionItem item) {
        this.item = item;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        item.getHelpCtx().display();
    }
}
