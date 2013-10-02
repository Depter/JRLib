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
package org.jreserve.gui.misc.renameable.dialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.jreserve.gui.misc.renameable.RenameClient;
import org.jreserve.gui.misc.renameable.RenameTask;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.AbstractWorker.LoadingTasks=Loading tasks...",
    "# {0} - path",
    "MSG.AbstractWorker.ErrorTask=Unable process rename of file ''{0}''!"
})
abstract class AbstractWorker extends SwingWorker<Void, Object> {

    protected final boolean indeterminate;
    private final JProgressBar pBar;

    AbstractWorker(JProgressBar pBar) {
        this.pBar = pBar;
        this.indeterminate = pBar.isIndeterminate();
    }
    
    protected List<RenameTask> getTasks(DataObject obj) {
        List<RenameTask> tasks = new ArrayList<RenameTask>();
        RenameClient[] clients = getClients();
        addTasks(obj, tasks, clients);
        return tasks;
    }
    
    private void addTasks(DataObject obj, List<RenameTask> tasks, RenameClient[] clients) {
        for(RenameClient client : clients)
            tasks.addAll(client.renamed(obj));
        
        if(obj instanceof DataFolder) {
            DataFolder folder = (DataFolder) obj;
            Enumeration<DataObject> children = folder.children(true);
            while(children.hasMoreElements()) {
                DataObject child = children.nextElement();
                for(RenameClient client : clients)
                    tasks.addAll(client.renamed(child));
            }
        }
    }
    
    private RenameClient[] getClients() {
        Collection<? extends RenameClient> result;
        result = Lookup.getDefault().lookupAll(RenameClient.class);
        int size = result.size();
        return result.toArray(new RenameClient[size]);
    }

    protected void runTasks(List<RenameTask> tasks, DataObject obj) {
        int size = tasks.size();
        setIndeterminate(false);
        setBounds(0, size+1);
        for(int i=0; i<size; i++) {
            runTask(tasks.get(i), obj);
            setValue(i+1);
        }
    }
    
    private void runTask(RenameTask task, DataObject obj) {
        try {
            task.renamed();
        } catch (IOException ex) {
            String path = obj.getPrimaryFile().getPath();
            BubbleUtil.showException(Bundle.MSG_AbstractWorker_ErrorTask(path), ex);
        }
    }
    
    protected void cleanUp() {
        System.gc();
        setIndeterminate(indeterminate);
    }
    
    protected final void setIndeterminate(final boolean indeterminate) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                pBar.setIndeterminate(indeterminate);
            }
        });
    }
    
    protected final void setMessage(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                pBar.setString(msg);
            }
        });
    }
    
    protected final void setBounds(final int min, final int max) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                pBar.setMinimum(min);
                pBar.setMaximum(max);
            }
        });
    }
    
    protected final void setValue(final int value) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                pBar.setValue(value);
            }
        });
    }
}
