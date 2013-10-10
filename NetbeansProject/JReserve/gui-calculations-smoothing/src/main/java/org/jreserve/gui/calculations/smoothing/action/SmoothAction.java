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
package org.jreserve.gui.calculations.smoothing.action;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jreserve.gui.calculations.smoothing.Smoothable;
import org.jreserve.gui.calculations.smoothing.SmoothableCategory;
import org.jreserve.gui.calculations.smoothing.registration.SmoothableAdapter;
import org.jreserve.gui.calculations.smoothing.registration.SmoothableRegistry;
import org.jreserve.gui.misc.flamingo.api.ActionCommandButton;
import org.jreserve.gui.misc.flamingo.api.ActionMenuButton;
import org.jreserve.gui.misc.flamingo.api.ResizableIcons;
import org.jreserve.gui.misc.flamingo.api.RibbonPresenter;
import org.jreserve.gui.misc.utils.actions.AbstractContextAwareAction;
import org.jreserve.gui.misc.utils.actions.RibbonRegistration;
import org.jreserve.gui.misc.utils.widgets.EmptyIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;
import org.pushingpixels.flamingo.api.common.AbstractCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "Edit",
    id = "org.jreserve.gui.calculations.actions.SmoothAction"
)
@ActionRegistration(
    displayName = "#LBL.SmoothAction.Name",
    lazy = false
)
@RibbonRegistration(
    path="Ribbon/TaskPanes/Edit/Calculation", 
    priority = RibbonRegistration.Priority.TOP,
    position=200
)
@Messages({
    "LBL.SmoothAction.Name=Smooth"
})
public class SmoothAction extends AbstractContextAwareAction 
    implements RibbonPresenter.Button, Presenter.Popup {
    
    @StaticResource private final static String IMG_PATH = "org/jreserve/gui/calculations/smoothing/smoothing.png";
    
    private ActionCommandButton button;
    
    public SmoothAction() {
        this(Utilities.actionsGlobalContext());
    }
    
    public SmoothAction(Lookup context) {
        super(context, Object.class);
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
        JCommandPopupMenu menu = new JCommandPopupMenu();
        for(SmoothableAdapter adapter : getAdapters(context)) {
            if(adapter.separatorBefore())
                menu.addMenuSeparator();
            menu.addMenuButton(createPopupMenuPresenter(adapter, context));
            if(adapter.separatorAfter())
                menu.addMenuSeparator();
        }
        initButton().setPopupCallback(new PopUpCallback(menu));
    }
    
    private ActionCommandButton initButton() {
        if(button == null) {
            button = new ActionCommandButton(
                    IMG_PATH, Bundle.LBL_SmoothAction_Name(), 
                    this, JCommandButton.CommandButtonKind.POPUP_ONLY);
            button.setPopupCallback(new PopUpCallback());
        }
        return button;
    }
    
    private List<SmoothableAdapter> getAdapters(Lookup context) {
        SmoothableCategory category = context.lookup(SmoothableCategory.class);
        if(category == null)
            return Collections.EMPTY_LIST;
        return SmoothableRegistry.getAdapters(category.getCategory());
    }
    
    private JCommandMenuButton createPopupMenuPresenter(SmoothableAdapter adapter, Lookup ctx) {
        ResizableIcon icon = createIcon(adapter.getIconBase());
        String name = adapter.getName();
        Action action = new AdapterAction(adapter, ctx);
        return new ActionMenuButton(icon, name, action, JCommandButton.CommandButtonKind.ACTION_ONLY);
    }
    
    private ResizableIcon createIcon(String iconBase) {
        if(iconBase == null || iconBase.length() == 0)
            return ResizableIcons.binary(EmptyIcon.EMPTY_16, EmptyIcon.EMPTY_32);
        return ResizableIcons.fromResource(iconBase);
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
        return initButton();
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenu menu = new JMenu(Bundle.LBL_SmoothAction_Name());
        Lookup ctx = getContext();
        List<SmoothableAdapter> adapters = getAdapters(ctx);
        menu.setEnabled(!adapters.isEmpty());
        
        for(SmoothableAdapter adapter : adapters) {
            JMenuItem item = new JMenuItem(new AdapterAction(adapter, ctx));
            item.setText(adapter.getName());
            menu.add(item);
        }
        
        return menu;
    }
    
    private class PopUpCallback implements PopupPanelCallback {
        
        private JCommandPopupMenu menu;
        
        private PopUpCallback() {
            this(new JCommandPopupMenu());
        }
        
        private PopUpCallback(JCommandPopupMenu menu) {
            this.menu = menu;
        }
        
        @Override
        public JPopupPanel getPopupPanel(JCommandButton commandButton) {
            return menu;
        }
    
    }
    
    private class AdapterAction extends AbstractAction {
        
        private Smoothable smoothable;
        
        private AdapterAction(SmoothableAdapter adapter, Lookup context) {
            this.smoothable = adapter.getSmoothable();
            super.putValue(NAME, adapter.getName());
            super.setEnabled(smoothable.canSmooth(context));
            
            String iconBase = adapter.getIconBase();
            if(iconBase != null && iconBase.length() > 0)
                super.putValue(SMALL_ICON, ImageUtilities.loadImageIcon(iconBase, false));
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            smoothable.smooth(getContext());
        }
    }
}
