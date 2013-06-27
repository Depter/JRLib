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
package org.jreserve.gui.misc.logging.settings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@OptionsPanelController.SubRegistration(
    location = "Advanced",
    displayName = "#AdvancedOption_DisplayName_Logging",
    keywords = "#AdvancedOption_Keywords_Logging",
    keywordsCategory = "Advanced/Logging"
)
@org.openide.util.NbBundle.Messages({
    "AdvancedOption_DisplayName_Logging=Logging", 
    "AdvancedOption_Keywords_Logging=log;logging"
})
public class LoggingOptionsPanelController extends OptionsPanelController {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private LoggingPanel panel;
    private boolean changed;
    
    @Override
    public void update() {
        getPanel().load();
        changed = false;
    }

    private LoggingPanel getPanel() {
        if (panel == null)
            panel = new LoggingPanel(this);
        return panel;
    }
    
    @Override
    public void applyChanges() {
        getPanel().store();
        changed = false;
    }

    @Override
    public void cancel() {
        //Do nothing
    }

    @Override
    public boolean isValid() {
        return getPanel().valid();
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public JComponent getComponent(Lookup lkp) {
        return getPanel();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    
    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
}
