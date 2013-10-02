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

package org.jreserve.gui.misc.utils.actions;

import java.awt.Image;
import java.beans.BeanInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import javax.swing.Icon;
import org.jreserve.gui.misc.utils.actions.DeletableDataObject.Client;
import org.jreserve.gui.misc.utils.actions.DeletableDataObject.Task;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - path",
    "MSG.AbstractDeletableDataObject.ErrorDelete=Unable to delete file ''{0}''!",
    "# {0} - path",
    "MSG.AbstractDeletableDataObject.ErrorTask=Unable process deletion of file ''{0}''!"
})
public abstract class AbstractDeletableDataObject implements DeletableDataObject {

    protected final DataObject obj;
    
    public AbstractDeletableDataObject(DataObject obj) {
        this.obj = obj;
    }
    
    @Override
    public DataObject getDataObject() {
        return obj;
    }
    
    @Override
    public void delete() throws Exception {
        List<Task> tasks = getTasks();
        if(handleDelete())
            for(Task task : tasks)
                runTask(task);
    }
    
    protected List<Task> getTasks() {
        List<Task> tasks = new ArrayList<Task>();
        Client[] clients = getClients();
        addTasks(obj, tasks, clients);
        return tasks;
    }
    
    private Client[] getClients() {
        Collection<? extends Client> result;
        result = Lookup.getDefault().lookupAll(Client.class);
        int size = result.size();
        return result.toArray(new Client[size]);
    }
    
    private void addTasks(DataObject obj, List<Task> tasks, Client[] clinets) {
        for(Client client : clinets)
            tasks.addAll(client.getTasks(obj));
        
        if(obj instanceof DataFolder) {
            DataFolder folder = (DataFolder) obj;
            Enumeration<DataObject> children = folder.children(true);
            while(children.hasMoreElements()) {
                DataObject child = children.nextElement();
                for(Client client : clinets)
                    tasks.addAll(client.getTasks(child));
            }
        }
    }

    private boolean handleDelete() {
        try {
            obj.delete();
            return true;
        } catch(IOException ex) {
            String path = obj.getPrimaryFile().getPath();
            BubbleUtil.showException(Bundle.MSG_AbstractDeletableDataObject_ErrorDelete(path), ex);
            return false;
        }
    }
    
    private void runTask(Task task) {
        try {
            task.deleted();
        } catch (IOException ex) {
            String path = obj.getPrimaryFile().getPath();
            BubbleUtil.showException(Bundle.MSG_AbstractDeletableDataObject_ErrorTask(path), ex);
        }
    }    
}
