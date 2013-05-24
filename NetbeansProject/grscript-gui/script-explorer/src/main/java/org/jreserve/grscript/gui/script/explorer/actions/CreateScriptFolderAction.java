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
package org.jreserve.grscript.gui.script.explorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.grscript.gui.script.explorer.nodes.ScriptFolderNode;
import org.jreserve.grscript.gui.script.registry.ScriptFolder;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "File",
    id = "org.jreserve.grscript.gui.script.explorer.actions.CreateScriptFolderAction")
@ActionRegistration(
    displayName = "#CTL_CreateScriptFolderAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1300),
    @ActionReference(path = ScriptFolderNode.ACTION_PATH, position = 100)
})
@Messages("CTL_CreateScriptFolderAction=Create folder")
public final class CreateScriptFolderAction implements ActionListener {

    private final ScriptFolder parent;

    public CreateScriptFolderAction(ScriptFolder context) {
        this.parent = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        CreateScriptFolderDialog dialog = new CreateScriptFolderDialog(parent);
        dialog.showDialog();
    }
}
