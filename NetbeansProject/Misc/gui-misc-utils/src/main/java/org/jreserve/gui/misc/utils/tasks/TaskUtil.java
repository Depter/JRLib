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
package org.jreserve.gui.misc.utils.tasks;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TaskUtil {
    
    private final static Logger logger = Logger.getLogger(TaskUtil.class.getName());
    private final static String PROP_CAPACITY = "taskutil.capacity";
    
    private static RequestProcessor INSTANCE;
    
    public synchronized static RequestProcessor getRP() {
        if(INSTANCE == null) {
            int capacity = Math.max(1, getCapacity());
            logger.log(Level.CONFIG, "TaskUtil capacity set to {0}.", capacity);
            INSTANCE = new RequestProcessor(TaskUtil.class.getName(), capacity);
        }
        return INSTANCE;
    }
    
    private static int getCapacity() {
        String prop = System.getProperty(PROP_CAPACITY);
        if(prop == null || prop.length()==0) {
            return getCoreCapacity();
        } else {
            try {
                logger.log(Level.FINE, "Parsing sytem property ''{0}'' as capacity.", prop);
                return Integer.parseInt(prop);
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Sytem property ''{0}'' can not be parsed as an integer.", prop);
                return getCoreCapacity();
            }
        }
    }
    
    private static int getCoreCapacity() {
        return Runtime.getRuntime().availableProcessors();
    }
    
    public static <T> void execute(Callable<T> task, SwingCallback<T> callBack) {
        execute(task, callBack, (ProgressHandle)null);
    }
    
    public static <T> void execute(Callable<T> task, SwingCallback<T> callBack, String phTitle) {
        ProgressHandle ph = phTitle==null? null : ProgressHandleFactory.createHandle(phTitle);
        execute(task, callBack, ph);
    }

    public static <T> void execute(Callable<T> task, SwingCallback<T> callBack, ProgressHandle ph) {
        RequestProcessor rp = getRP();
        rp.post(new CallableExecutor(task, callBack, ph));
    }
    
    public static void executeSwingCallback(Runnable task, final TaskListener listener) {
        if(listener == null)
            throw new NullPointerException("TaskListener is null!");
        execute(task, new SwingTaskListener(listener));
    }
    
    public static void execute(Runnable task) {
        execute(task, null);
    }
    
    public static void execute(Runnable task, TaskListener listener) {
        Task t = getRP().create(task);
        if(listener != null)
            t.addTaskListener(listener);
        getRP().submit(t);
    }
    
    private TaskUtil() {
    }   
    
    
    private static class SwingTaskListener implements TaskListener {
        
        private final TaskListener listener;

        private SwingTaskListener(TaskListener listener) {
            this.listener = listener;
        }
        
        @Override
        public void taskFinished(final Task task) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    listener.taskFinished(task);
                }
            });
        }
        
    }
}
