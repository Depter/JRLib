package org.jreserve.grscript.gui.script.function.explorer.nodes;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jreserve.grscript.gui.script.function.explorer.FunctionItem;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FunctionItemNode extends AbstractNode {
    
    public final static String ACTION_PATH = "Scripts/Actions/Nodes/FunctionItemNode";
    private final static Image FUNCTION_IMG = ImageUtilities.loadImage(FunctionFolderNode.IMG_PATH+"function.png");
    private final static Image PROPERTY_IMG = ImageUtilities.loadImage(FunctionFolderNode.IMG_PATH+"property.png");
    private FunctionItem item;
    private HelpCtx help;
    
    public FunctionItemNode(FunctionItem item) {
        super(Children.LEAF, Lookups.singleton(item));
        this.item = item;
        setDisplayName(item.getSigniture());
        help = item.getHelpCtx();
    }

    @Override
    public Image getIcon(int type) {
        return item.isProperty()? PROPERTY_IMG : FUNCTION_IMG;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return help;
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = new ArrayList<Action>(Utilities.actionsForPath(ACTION_PATH));
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Action getPreferredAction() {
        return new OpenHelpAction();
    }
    
    private class OpenHelpAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            help.display();
        }
    }
}
