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

package org.jreserve.gui.data.api.wizard;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import org.jreserve.gui.data.dataobject.DataSourceObjectProvider;
import org.jreserve.gui.misc.utils.actions.AbstractContextAwareAction;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCreateDataSourceAction extends AbstractContextAwareAction {

    protected AbstractCreateDataSourceAction(Lookup context) {
        super(context, DataSourceObjectProvider.class);
    }
    
    @Override
    protected boolean shouldEnable(Lookup context) {
        return context.lookup(DataSourceObjectProvider.class) != null;
    }

    @Override
    protected void performAction(ActionEvent evt) {
        AbstractDataSourceWizardIterator it = createIterator();
        WizardDescriptor wiz = new WizardDescriptor(it);
        it.initializeFrom(getContext());
        wiz.setTitleFormat(new MessageFormat("{0} ({1})"));
        wiz.setTitle(getWizardTitle());
        DialogDisplayer.getDefault().notify(wiz);        
    }
    
    protected abstract AbstractDataSourceWizardIterator createIterator();
    
    protected abstract String getWizardTitle();
}
