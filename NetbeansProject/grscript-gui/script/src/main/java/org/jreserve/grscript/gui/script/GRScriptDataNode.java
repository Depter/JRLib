package org.jreserve.grscript.gui.script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GRScriptDataNode extends DataNode {

    public final static String ACTION_PATH = "Scripts/Actions/Nodes/GRScriptNode";
    
    public GRScriptDataNode(DataObject obj) {
        super(obj, Children.LEAF);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> layerActions = new ArrayList<Action>(Utilities.actionsForPath(ACTION_PATH));
        Action[] superActions = super.getActions(context);
        
        if(superActions!=null && superActions.length > 0) {
            layerActions.addAll(0, Arrays.asList(superActions));
            if(layerActions.size() > superActions.length)
                layerActions.add(superActions.length, null);
        }
        
        return layerActions.toArray(new Action[layerActions.size()]);
    }

    
}
