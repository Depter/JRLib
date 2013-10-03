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
package org.jreserve.gui.misc.utils.actions.deletable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
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
    "MSG.DeleteWorker.DeleteError=Unable to delete object!",
    "MSG.DeleteWorker.TaskError=Unable to complete delete task!"
})
class DeleteWorker extends SwingWorker<Void, DeleteWorker.WorkerChunk> {

    private final List<Deletable> deletables;

    public DeleteWorker(List<Deletable> deletables) {
        this.deletables = new ArrayList<Deletable>(deletables);
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        int size = deletables.size();
        super.publish(new BoundChunk(0, size));
        for(int i=0; i<size; i++) {
            delete(deletables.get(i));
            super.publish(new ValueChunk(i));
        }
        super.publish(new ValueChunk(size));
        return null;
    }
    
    private void delete(Deletable deletable) {
        List<Deletable.Task> tasks = getTasks(deletable.getDeletedObject());
        if(handleDelete(deletable))
            for(Deletable.Task task : tasks)
                runTask(task);
    }
    
    private List<Deletable.Task> getTasks(Object obj) {
        List<Deletable.Task> tasks = new ArrayList<Deletable.Task>();
        Deletable.Client[] clients = getClients();
        addTasks(obj, tasks, clients);
        return tasks;
    }
    
    private Deletable.Client[] getClients() {
        Collection<? extends Deletable.Client> result;
        result = Lookup.getDefault().lookupAll(Deletable.Client.class);
        int size = result.size();
        return result.toArray(new Deletable.Client[size]);
    }
    
    private void addTasks(Object obj, List<Deletable.Task> tasks, Deletable.Client[] clinets) {
        for(Deletable.Client client : clinets)
            tasks.addAll(client.getTasks(obj));
        
        if(obj instanceof DataFolder) {
            DataFolder folder = (DataFolder) obj;
            Enumeration<DataObject> children = folder.children(true);
            while(children.hasMoreElements()) {
                DataObject child = children.nextElement();
                for(Deletable.Client client : clinets)
                    tasks.addAll(client.getTasks(child));
            }
        }
    }
    
    private boolean handleDelete(Deletable deletable) {
        try {
            deletable.delete();
            return true;
        } catch(Exception ex) {
            BubbleUtil.showException(Bundle.MSG_DeleteWorker_DeleteError(), ex);
            return false;
        }
    }
    
    private void runTask(Deletable.Task task) {
        try {
            task.objectDeleted();
        } catch (Exception ex) {
            BubbleUtil.showException(Bundle.MSG_DeleteWorker_TaskError(), ex);
        }
    }    
    
    static abstract class WorkerChunk {
        
        public abstract void setProgress(JProgressBar pBar);
    }
    
    private static class ValueChunk extends WorkerChunk {
        private final int value;

        private ValueChunk(int value) {
            this.value = value;
        }

        @Override
        public void setProgress(JProgressBar pBar) {
            pBar.setValue(value);
        }
    }
    
    private static class BoundChunk extends WorkerChunk {
        private final int min;
        private final int max;

        private BoundChunk(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public void setProgress(JProgressBar pBar) {
            pBar.setMinimum(min);
            pBar.setMaximum(max);
            pBar.setValue(min);
        }
    }
    
    private static class IndeterminateWorkerChunk extends WorkerChunk {
        private final boolean indeterminate;
        private final String msg;
        
        private IndeterminateWorkerChunk(boolean indeterminate) {
            this(indeterminate, null);
        }
        
        private IndeterminateWorkerChunk(boolean indeterminate, String msg) {
            this.indeterminate = indeterminate;
            this.msg = msg;
        }
        
        @Override
        public void setProgress(JProgressBar pBar) {
            pBar.setIndeterminate(indeterminate);
            pBar.setString(msg);
        }
    }
    
}
