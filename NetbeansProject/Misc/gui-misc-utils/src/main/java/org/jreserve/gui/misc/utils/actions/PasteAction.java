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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.datatransfer.PasteType;

/**
 *
 * @author Peter Decsi
 */
@ActionID(
    category = "Edit",
    id = "org.jreserve.gui.misc.utils.actions.PasteAction"
)
@ActionRegistration(
    lazy = false,
    displayName = "#CTL_PasteAction"
)
@Messages({
    "CTL_PasteAction=Paste"
})
public class PasteAction extends AbstractContextAwareAction {

    @StaticResource private final static String IMG_16 = "org/jreserve/gui/misc/utils/paste.png"; //NOI18
    @StaticResource private final static String IMG_32 = "org/jreserve/gui/misc/utils/paste32.png";   //NOI18
    private final static Logger logger = Logger.getLogger(PasteAction.class.getName());
    
    private final Clipboard cb;
    private final FlavorListener cbListener = new FlavorListener() {
        @Override
        public void flavorsChanged(FlavorEvent e) {
            if(reactToClipboard)
                PasteAction.this.contextChanged();
        }
    };
    
    private boolean reactToClipboard = true;
    private PasteType[] pasteTypes;
    
    public PasteAction() {
        this(Utilities.actionsGlobalContext());
    }

    public PasteAction(Lookup context) {
        super(context, ClipboardUtil.Pasteable.class);
        setDisplayName(Bundle.CTL_PasteAction());
        setSmallIconPath(IMG_16);
        setLargeIconPath(IMG_32);
        
        cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        if(cb == null) {
            logger.warning("SystemClipboard not found! CopyAction will be disabled!");
        } else {
            cb.addFlavorListener(WeakListeners.create(FlavorListener.class, cbListener, cb));
        }
    }
    
    @Override
    protected boolean shouldEnable(Lookup context) {
        Transferable t = getTransferable();
        if(t == null)
            return false;
        
        ClipboardUtil.Pasteable pasteable = context.lookup(ClipboardUtil.Pasteable.class);
        if(pasteable == null)
            return false;
        pasteTypes = pasteable.getPasteTypes(t);
        
        return pasteTypes!=null && pasteTypes.length > 0;
    }
    
    private Transferable getTransferable() {
        return cb == null? null : cb.getContents(null);
    }

    @Override
    protected void performAction(ActionEvent evt) {
        reactToClipboard = false;
        Transferable t = null;
        for(PasteType pt : pasteTypes)
            t = performPaste(pt);
        reactToClipboard = true;
        
        if(t != null)
            cb.setContents(t, new StringSelection(""));
    }
    
    private Transferable performPaste(PasteType pt) {
        try {
            return pt.paste();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to perform paste!", ex);
            return null;
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new PasteAction();
    }
}
