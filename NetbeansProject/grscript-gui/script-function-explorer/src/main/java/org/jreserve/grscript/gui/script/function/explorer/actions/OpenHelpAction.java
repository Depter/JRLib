package org.jreserve.grscript.gui.script.function.explorer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.grscript.gui.script.function.explorer.FunctionItem;
import org.jreserve.grscript.gui.script.function.explorer.nodes.FunctionItemNode;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
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
