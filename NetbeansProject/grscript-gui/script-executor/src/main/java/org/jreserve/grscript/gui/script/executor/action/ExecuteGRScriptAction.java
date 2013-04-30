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
        displayName = "#CTL_ExecuteGRScriptAction")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1380),
    @ActionReference(path = GRScriptDataNode.ACTION_PATH, position = 100)
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
