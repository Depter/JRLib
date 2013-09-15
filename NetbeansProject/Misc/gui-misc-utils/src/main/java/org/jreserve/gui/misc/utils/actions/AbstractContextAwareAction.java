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
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.util.ContextAwareAction;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractContextAwareAction 
    extends AbstractAction 
    implements ContextAwareAction {
    
    private final Lookup context;
    private final Class clazz;
    private Lookup.Result<Object> lkpInfo;
    private final LookupListener listener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent ev) {
            contextChanged();
        }
    };
    
    protected AbstractContextAwareAction() {
        this(Object.class);
    }
    
    protected AbstractContextAwareAction(Class clazz) {
        this(Utilities.actionsGlobalContext(), clazz);
    }
 
    protected AbstractContextAwareAction(Lookup context, Class clazz) {
        this.context = context;
        this.clazz = clazz==null? Object.class : clazz;
    }

    protected final void setDisplayName(String name) {
        putValue(NAME, name);
    }
    
    protected final void setSmallIconPath(String path) {
        putValue(SMALL_ICON, ImageUtilities.loadImageIcon(path, false));
    }
    
    protected final void setLargeIconPath(String path) {
        putValue(LARGE_ICON_KEY, ImageUtilities.loadImageIcon(path, false));
    }
    
    @Override
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }
 
    protected final void init() {
        if (lkpInfo != null)
            return;
        
        lkpInfo = context.lookupResult(clazz);
        lkpInfo.addLookupListener(WeakListeners.create(LookupListener.class, listener, lkpInfo));
        contextChanged();
    }
 
    protected final void contextChanged() {
        setEnabled(shouldEnable(context));
    }
    
    protected abstract boolean shouldEnable(Lookup context);
    
    protected final Lookup getContext() {
        return context;
    }
    
    protected final <T> T getFirst(Class<T> clazz) {
        return context.lookup(clazz);
    }
    
    protected final <T> List<T> getAll(Class<T> clazz) {
        return new ArrayList<T>(context.lookupAll(clazz));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        init();
        performAction(e);
    }
    
    protected abstract void performAction(ActionEvent evt);
}
