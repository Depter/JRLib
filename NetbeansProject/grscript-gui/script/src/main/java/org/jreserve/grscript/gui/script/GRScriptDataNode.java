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
