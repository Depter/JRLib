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

package org.jreserve.gui.calculations.claimtriangle.impl;

import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import org.jreserve.gui.misc.renameable.Renameable;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.actions.OpenAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
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
@ActionReferences({
    @ActionReference(
        path = ClaimTriangleNode.ACTION_PATH,
        id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
        position = 300, separatorAfter = 350),
    @ActionReference(
        path = ClaimTriangleNode.ACTION_PATH,
        id = @ActionID(category = "File", id = "org.jreserve.gui.misc.renameable.action.RenameAction"),
        position = 400),
    @ActionReference(
        path = ClaimTriangleNode.ACTION_PATH,
        id = @ActionID(category = "Edit", id = "org.jreserve.gui.misc.utils.actions.CopyAction"),
        position = 500),
    @ActionReference(
        path = ClaimTriangleNode.ACTION_PATH,
        id = @ActionID(category = "Edit", id = "org.jreserve.gui.misc.utils.actions.CutAction"),
        position = 600, separatorAfter = 650),
    @ActionReference(
        path = ClaimTriangleNode.ACTION_PATH,
        id = @ActionID(category = "File", id = "org.jreserve.gui.misc.utils.notifications.actions.DeleteAction"), 
        position = 700)
})
class ClaimTriangleNode extends DataNode {

    @StaticResource final static String IMG = "org/jreserve/gui/calculations/claimtriangle/triangle.png";
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
        ic.add(new ClaimTriangleRenameable());
        
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
    public boolean canRename() {
        return false;
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
    
    @Override
    public String getDisplayName() {
        return super.getDataObject().getName();
    }
    
    @Override
    public void setName(String name) {
        super.setName(name);
        setDisplayName(name);
    }
    
    
    private class ClaimTriangleRenameable implements Renameable {
        @Override
        public DataObject getObject() {
            return getLookup().lookup(DataObject.class);
        }

        private Displayable getDelegate() {
            return getLookup().lookup(Displayable.class);
        }
        
        @Override
        public Icon getIcon() {
            return getDelegate().getIcon();
        }

        @Override
        public String getDisplayName() {
            return getDelegate().getDisplayName();
        }
    }
    
}
