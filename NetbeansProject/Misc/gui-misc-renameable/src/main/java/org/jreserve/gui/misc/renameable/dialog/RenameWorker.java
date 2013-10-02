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
import javax.swing.JProgressBar;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - path",
    "MSG.RenameWorker.ErrorRename=Unable to rename file ''{0}''!"
})
class RenameWorker extends AbstractWorker {
    
    private final String name;
    private final DataObject obj;
    private final String path;
    
    RenameWorker(DataObject obj, String name, JProgressBar pBar) {
        super(pBar);
        this.name = name;
        this.obj = obj;
        this.path = obj.getPrimaryFile().getPath();
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        setIndeterminate(true);
        if(renameObject()) {
            setMessage(Bundle.MSG_AbstractWorker_LoadingTasks());
            runTasks(getTasks(obj), obj);
            cleanUp();
        }
        return null;
    }

    private boolean renameObject() throws IOException {
        try {
            obj.rename(name);
            return true;
        } catch (IOException ex) {
            BubbleUtil.showException(Bundle.MSG_RenameWorker_ErrorRename(path), ex);
            return false;
        }
    }
}
