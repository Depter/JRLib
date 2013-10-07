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
package org.jreserve.gui.calculations.actions;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jreserve.gui.calculations.api.smoothing.Smoothable;
import org.jreserve.gui.calculations.api.smoothing.SmoothableCategory;
import org.jreserve.gui.calculations.util.SmoothableAdapter;
import org.jreserve.gui.calculations.util.SmoothableRegistry;
import org.jreserve.gui.misc.flamingo.api.ActionCommandButton;
import org.jreserve.gui.misc.flamingo.api.ActionMenuButton;
import org.jreserve.gui.misc.flamingo.api.RibbonPresenter;
import org.jreserve.gui.misc.utils.actions.AbstractContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.pushingpixels.flamingo.api.common.AbstractCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.SmoothAction.Name=Smooth"
})
public class SmoothAction extends AbstractContextAwareAction implements RibbonPresenter.Button {
    
    private final static String IMG_PATH = "org/jreserve/gui/calculations/icons/smoothing.png";
    
    private final ActionCommandButton button;
    private final JCommandPopupMenu menu;
    
    public SmoothAction() {
        this(Utilities.actionsGlobalContext());
    }
    
    public SmoothAction(Lookup context) {
        super(context, SmoothableCategory.class);
        button = new ActionCommandButton(
                IMG_PATH, Bundle.LBL_SmoothAction_Name(), 
                this, JCommandButton.CommandButtonKind.POPUP_ONLY);
        menu = new JCommandPopupMenu();
        button.setPopupCallback(new PopupPanelCallback() {
            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                return menu;
            }
        });
    }
    
    @Override
    protected boolean shouldEnable(Lookup context) {
        if(getAdapters(context).isEmpty()) {
            return false;
        } else {
            updateMenu(context);
            return true;
        }
    }
    
    private void updateMenu(Lookup context) {
        menu.removeAll();
        for(SmoothableAdapter adapter : getAdapters(context))
            menu.addMenuButton(createPopupMenuPresenter(adapter));
    }
    
    private List<SmoothableAdapter> getAdapters(Lookup context) {
        SmoothableCategory category = context.lookup(SmoothableCategory.class);
        if(category == null)
            return Collections.EMPTY_LIST;
        return SmoothableRegistry.getAdapters(category.getCategory());
    }
    
    private JCommandMenuButton createPopupMenuPresenter(SmoothableAdapter adapter) {
        String iconBase = adapter.getIconBase();
        String name = adapter.getName();
        Action action = new AdapterAction(adapter);
        return new ActionMenuButton(iconBase, name, action, JCommandButton.CommandButtonKind.ACTION_ONLY);
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected void performAction(ActionEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new SmoothAction(actionContext);
    }

    @Override
    public AbstractCommandButton getRibbonButtonPresenter() {
        return button;
    }
    
    private class AdapterAction extends AbstractAction {
        
        private Smoothable smoothable;
        
        private AdapterAction(SmoothableAdapter adapter) {
            this.smoothable = adapter.getSmoothable();
            setEnabled(smoothable.canSmooth(getContext()));
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            smoothable.smooth(getContext());
        }
    }
}
