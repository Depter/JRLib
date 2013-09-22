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

package org.jreserve.gui.data.dataobject.node;

import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.List;
import javax.swing.Action;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.dataobject.DataSourceDataObject;
import org.jreserve.jrlib.gui.data.DataType;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.actions.OpenAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.Utilities;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;
import org.openide.nodes.Node;
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
        path = DataSourceNode.ACTION_PATH,
        id = @ActionID(category = "Project", id = "org.netbeans.modules.project.ui.NewFile$WithSubMenu"),
        position = 100, separatorAfter = 150),
    @ActionReference(
        path = DataSourceNode.ACTION_PATH,
        id = @ActionID(category = "File", id = "org.jreserve.gui.data.inport.ImportDataAction"),
        position = 200),
    @ActionReference(
        path = DataSourceNode.ACTION_PATH,
        id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
        position = 300, separatorAfter = 350),
    @ActionReference(
        path = DataSourceNode.ACTION_PATH,
        id = @ActionID(category = "Edit", id = "org.jreserve.gui.misc.utils.actions.CopyAction"),
        position = 400),
    @ActionReference(
        path = DataSourceNode.ACTION_PATH,
        id = @ActionID(category = "Edit", id = "org.jreserve.gui.misc.utils.actions.CutAction"),
        position = 500, separatorAfter = 550),
    @ActionReference(
        path = DataSourceNode.ACTION_PATH,
        id = @ActionID(category = "File", id = "org.jreserve.gui.misc.utils.notifications.actions.DeleteAction"), 
        position = 600)
})
public class DataSourceNode extends DataNode {

    @StaticResource public final static String IMG_TRIANGLE = "org/jreserve/gui/data/icons/database_triangle.png";
    @StaticResource public final static String IMG_VECTOR = "org/jreserve/gui/data/icons/database_vector.png";
    public final static String ACTION_PATH = "Node/DataSourceNode/Actions";  //NOI18
    
    private final InstanceContent ic;
    
    public DataSourceNode(DataSourceDataObject obj) {
        this(obj, new InstanceContent());
    }
    
    private DataSourceNode(DataSourceDataObject obj, InstanceContent ic) {
        super(obj, Children.LEAF, 
              new ProxyLookup(obj.getLookup(), new AbstractLookup(ic))
        );
        
        this.ic = ic;
        ic.add(ClipboardUtil.createCopiable(obj));
        ic.add(ClipboardUtil.createCutable(obj));
        
        super.setDisplayName(obj.getName());
        if(getLookup().lookup(DataSource.class).getDataType() == DataType.TRIANGLE) {
            super.setIconBaseWithExtension(IMG_TRIANGLE);
        } else {
            super.setIconBaseWithExtension(IMG_VECTOR);
        }
    }
    
    @Override
    public Node cloneNode() {
        return new DataSourceNode((DataSourceDataObject) getDataObject());
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
    
    @Override
    public void setName(String name) {
        super.setName(name);
        setDisplayName(name);
    }
}
