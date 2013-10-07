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
import javax.swing.SwingUtilities;
import org.netbeans.api.progress.ProgressHandle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CallableExecutor<T> implements Runnable {

    private final Callable<T> callable;
    private final SwingCallback<T> swingCallBack;
    private final ProgressHandle ph;
    
    CallableExecutor(Callable<T> callable, SwingCallback<T> swingCallBack, ProgressHandle ph) {
        this.callable = callable;
        this.swingCallBack = swingCallBack;
        this.ph = ph;
    }
    
    @Override
    public void run() {
        showProgressBar();
        
        T result = null;
        Exception exception = null;
        try {
            result = callable.call();
        } catch (Exception ex) {
            exception = ex;
        }
        
        hideProgressBar();
        if(swingCallBack != null) {
            if(exception == null)
                swingFinnished(result);
            else
                swingException(exception);
        }
    }
    
    private void showProgressBar() {
        if(ph != null) {
            ph.start();
            ph.switchToIndeterminate();
        }
    }
    
    private void swingFinnished(final T result) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingCallBack.finnished(result);
            }
        });
    }
    
    private void swingException(final Exception ex) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingCallBack.finnishedWithException(ex);
            }
        });
    }
    
    private void hideProgressBar() {
        if(ph != null)
            ph.finish();
    }
}
