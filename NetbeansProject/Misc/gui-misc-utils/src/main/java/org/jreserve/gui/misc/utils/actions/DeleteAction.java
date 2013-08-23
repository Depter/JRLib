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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "File",
    id = "org.jreserve.gui.misc.utils.notifications.actions.DeleteAction"
)
@ActionRegistration(
    displayName = "#CTL.DeleteAction",
    iconBase = "org/jreserve/gui/misc/utils/delete.png"
)
@Messages({
    "CTL.DeleteAction=Delete"
})
public class DeleteAction implements ActionListener {
    private final static Logger logger = Logger.getLogger(DeleteAction.class.getName());
    
    private final static String PATH = 
        "Actions/File/org-jreserve-gui-misc-utils-notifications-actions-DeleteAction.instance"; //NOI18
    private static Action ACTION;
    
    public synchronized static Action getAction() {
        if(ACTION == null)
            ACTION = loadAction();
        return ACTION;
    }
    
    private static Action loadAction() {
        try {
            FileObject file = FileUtil.getConfigFile(PATH);
            DataObject dObj = DataObject.find(file);
            InstanceCookie ic = dObj.getLookup().lookup(InstanceCookie.class);
            if(ic != null)
                return (Action) ic.instanceCreate();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to load Action from: "+PATH, ex);
        }
        return null;
    }
    
    private List<Deletable> context;
    
    public DeleteAction(List<Deletable> context) {
        this.context = context;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        new DeleteDialog(context).showDialog();
    }
}
