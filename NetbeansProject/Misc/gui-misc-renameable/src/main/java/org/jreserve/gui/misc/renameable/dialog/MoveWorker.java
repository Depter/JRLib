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
import java.util.List;
import javax.swing.JProgressBar;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - path",
    "MSG.MoveWorker.ErrorMove=Unable to move file ''{0}''!"
})
class MoveWorker extends AbstractWorker {

    private final DataFolder folder;
    private final List<DataObject> sources;
    
    MoveWorker(DataFolder folder, List<DataObject> sources, JProgressBar pBar) {
        super(pBar);
        this.folder = folder;
        this.sources = sources;
    }

    @Override
    protected Void doInBackground() throws Exception {
        setIndeterminate(true);
        
        setBounds(0, sources.size());
        int index = 0;
        setValue(index);
        for(DataObject source : sources) {
            if(handleOperation(source))
                runTasks(getTasks(source), source);
            setValue(index++);
        }
        
        cleanUp();
        return null;
    }

    private boolean handleOperation(DataObject source) {
        try {
            source.move(folder);
            return true;
        } catch(IOException ex) {
            String path = source.getPrimaryFile().getPath();
            BubbleUtil.showException(Bundle.MSG_MoveWorker_ErrorMove(path), ex);
            return false;
        }
    }
    
}
