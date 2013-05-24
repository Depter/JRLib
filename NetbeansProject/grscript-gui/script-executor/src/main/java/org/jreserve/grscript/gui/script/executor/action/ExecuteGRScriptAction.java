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
package org.jreserve.grscript.gui.script.executor.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.grscript.gui.notificationutil.BubbleUtil;
import org.jreserve.grscript.gui.script.GRScriptDataObject;
import org.jreserve.grscript.gui.script.GRScriptDataNode;
import org.jreserve.grscript.gui.script.executor.GRScriptExecutor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "File",
    id = "org.jreserve.grscript.gui.script.executor.action.ExecuteGRScriptAction")
@ActionRegistration(
        displayName = "#CTL_ExecuteGRScriptAction",
        iconBase = "org/jreserve/grscript/gui/script/executor/action/run.png"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1380),
    @ActionReference(path = GRScriptDataNode.ACTION_PATH, position = 100),
    @ActionReference(path = "Toolbars/Script", position = 100),
    @ActionReference(path = "Shortcuts", name = "F5")
})
@Messages({
    "CTL_ExecuteGRScriptAction=Run script",
    "MSG.ExecuteGRScriptAction.Error=Unable to execute script..."
})
public class ExecuteGRScriptAction implements ActionListener {

    private GRScriptDataObject context;
    
    public ExecuteGRScriptAction(GRScriptDataObject context) {
        this.context = context;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            GRScriptExecutor.executeScript(context);
        } catch (Exception ex) {
            BubbleUtil.showException(Bundle.MSG_ExecuteGRScriptAction_Error(), ex);
        }
    }
}
