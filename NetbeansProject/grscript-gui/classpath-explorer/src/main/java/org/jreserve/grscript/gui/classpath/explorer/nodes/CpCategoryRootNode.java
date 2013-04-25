package org.jreserve.grscript.gui.classpath.explorer.nodes;

import java.util.ArrayList;
import java.util.Collections;
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
public class CpCategoryRootNode extends AbstractNode {

    public final static String ACTION_PATH = "Classpath/Actions/Node/Root/Custom";
    private CpRootType type;
    
    public CpCategoryRootNode(CpRootType type) {
        super(Children.create(new CpChildFactory(type), true));
        setDisplayName(type.getUserName());
        this.type = type;
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = new ArrayList<Action>(getTypeActions(context));
        return actions.toArray(new Action[actions.size()]);
    }
    
    private List<? extends Action> getTypeActions(boolean context) {
        if(CpRootType.CUSTOM == type)
            return Utilities.actionsForPath(ACTION_PATH);
        else
            return Collections.EMPTY_LIST;
    }
}
