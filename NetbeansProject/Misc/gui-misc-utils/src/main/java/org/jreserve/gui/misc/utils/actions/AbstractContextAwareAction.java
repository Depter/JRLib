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
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractContextAwareAction 
    extends AbstractAction 
    implements ContextAwareAction {
    
    private final Lookup context;
    private Lookup.Result<Object> lkpInfo;
    
    protected AbstractContextAwareAction() {
        this(Utilities.actionsGlobalContext());
    }
 
    protected AbstractContextAwareAction(Lookup context) {
        this.context = context;
    }
 
    @Override
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }
 
    void init() {
        if (lkpInfo != null)
            return;

        lkpInfo = context.lookupResult(Object.class);
        lkpInfo.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent le) {
                contextChanged();
            }
        });
        contextChanged();
    }
 
    private void contextChanged() {
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
