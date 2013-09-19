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

package org.jreserve.gui.calculations.triangle;

import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.List;
import javax.swing.Action;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.actions.OpenAction;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimTriangleNode extends DataNode {

    @StaticResource final static String IMG = "org/jreserve/gui/calculations/icons/triangle.png";
    public final static String ACTION_PATH = "Node/ClaimTriangleNode/Actions";  //NOI18
    
    private final InstanceContent ic;
    
    public ClaimTriangleNode(ClaimTriangleDataObject obj) {
        this(obj, new InstanceContent());
    }
    
    private ClaimTriangleNode(ClaimTriangleDataObject obj, InstanceContent ic) {
        super(obj, Children.LEAF, 
              new ProxyLookup(obj.getLookup(), new AbstractLookup(ic))
        );
        
        this.ic = ic;
        ic.add(ClipboardUtil.createCopiable(obj));
        ic.add(ClipboardUtil.createCutable(obj));
        
        super.setDisplayName(obj.getName());
        super.setIconBaseWithExtension(IMG);
    }
    
    @Override
    public Node cloneNode() {
        return new ClaimTriangleNode((ClaimTriangleDataObject) getDataObject());
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = Utilities.actionsForPath(ACTION_PATH);
        return actions.toArray(new Action[actions.size()]);
    }
    
    @Override
    public Action getPreferredAction() {
        return OpenAction.get(OpenAction.class);
    }
    
    @Override
    public boolean canCut() {
        return true;
    }
    
    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public Transferable drag() throws IOException {
        return clipboardCut();
    }
}
