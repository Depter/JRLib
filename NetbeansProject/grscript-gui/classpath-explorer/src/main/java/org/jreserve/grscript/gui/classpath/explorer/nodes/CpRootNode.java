package org.jreserve.grscript.gui.classpath.explorer.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CpRootNode extends AbstractNode {

    public final static String ACTION_PATH = "Classpath/Actions/Node/Root";
    
    public CpRootNode() {
        super(Children.create(new CpRootChildren(), false));
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = new ArrayList<Action>(Arrays.asList(super.getActions(context)));
        actions.addAll(Utilities.actionsForPath(ACTION_PATH));
        return actions.toArray(new Action[actions.size()]);
    }
}
