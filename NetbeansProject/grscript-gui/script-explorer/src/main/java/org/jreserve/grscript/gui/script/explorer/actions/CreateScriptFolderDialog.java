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

import org.jreserve.grscript.gui.script.registry.ScriptFolder;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.CreateScriptDialog.Title=Create folder",
    "# {0} - name",
    "MSG.CreateScriptDialog.Name.Exists=Name \"{0}\" already used!"
})
class CreateScriptFolderDialog extends ScriptFolderPanel {
    
    CreateScriptFolderDialog(ScriptFolder parent) {
        super(parent);
    }
    
    @Override
    protected String getDialogTitle() {
        return Bundle.LBL_ScriptFolderPanel_Name();
    }

    @Override
    protected boolean isInputValid(String name) {
        return super.isInputValid(name) && isNewName(name);
    }
    
    private boolean isNewName(String name) {
        for(ScriptFolder child : folder.getFolders()) {
            if(child.getName().equalsIgnoreCase(name)) {
                setErrorMsg(Bundle.MSG_CreateScriptDialog_Name_Exists(child.getName()));
                return false;
            }
        }
        return true;
    }

    @Override
    protected void dialogOk() {
        folder.addFolder(new ScriptFolder(getNameText()));
    }
}
