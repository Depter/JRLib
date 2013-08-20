/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.officelaf.layerribbon;

import java.util.List;
import javax.swing.JSeparator;
import org.officelaf.api.ActionItem;
import org.officelaf.api.ActionItems;
import org.officelaf.api.RibbonComponentFactory;
import org.officelaf.spi.RibbonProvider;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.lookup.ServiceProvider;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service = RibbonProvider.class)
public class LayerRibbonProvider implements RibbonProvider {
    final static String MENU_CONTENT_PATH = "Ribbon/AppMenu"; // NOI18N
    final static String MENU_FOOTER_PATH = "Ribbon/AppMenuFooter"; // NOI18N
    final static String TASK_BAR_PATH = "Ribbon/TaskBar"; // NOI18N
    final static String TASK_PANE_PATH = "Ribbon/TaskPanes"; // NOI18N
    final static String HELP_PATH = "Ribbon/HelpButton";//NOI18

    private final static String APP_MENU_HEIGHT_ATTR = "minHeight";
    private final static String PROP_APP_MENU_HEIGHT = "ribbon.popupMinHeight";
    
    @Override
    public JRibbon createRibbon() {
        System.setProperty(PROP_APP_MENU_HEIGHT, ""+getMinAppMenuHeight());
        JRibbon ribbon = new JRibbon();
        RibbonComponentFactory factory = new RibbonComponentFactory();
        
        addAppMenu(ribbon);
        addTaskBar(ribbon, factory);
        addTaskPanes(ribbon, factory);
        addHelpButton(ribbon);
        return ribbon;
    }
    
    private static void addAppMenu(JRibbon ribbon) {
        LayerRibbonAppMenuProvider appMenuProvider = new LayerRibbonAppMenuProvider();
        RibbonApplicationMenu appMenu = appMenuProvider.createApplicationMenu();
        if (appMenu != null)
            ribbon.setApplicationMenu(appMenu);
    }
    
    private static void addTaskBar(JRibbon ribbon, RibbonComponentFactory factory) {
        List<? extends ActionItem> actions = ActionItems.forPath(TASK_BAR_PATH);
        for (ActionItem action : actions) {
            if (action.isSeparator()) {
                ribbon.addTaskbarComponent(new JSeparator(JSeparator.VERTICAL));
            } else {
                ribbon.addTaskbarComponent(factory.createTaskBarPresenter(action));
            }
        }
    }

    private void addTaskPanes(JRibbon ribbon, RibbonComponentFactory factory) {
        for (ActionItem item : ActionItems.forPath(TASK_PANE_PATH))
             ribbon.addTask(factory.createRibbonTask(item));
    }

    private void addHelpButton(JRibbon ribbon) {
        List<? extends ActionItem> actions = ActionItems.forPath(HELP_PATH);
        if (actions.size() > 0) {
            ribbon.configureHelp(actions.get(0).getIcon(), actions.get(0).getAction());
        }
    }
    
    private static int getMinAppMenuHeight() {
        FileObject folder = FileUtil.getConfigFile(MENU_CONTENT_PATH);
        if(folder == null)
            return 0;
    
        Object v = folder.getAttribute(APP_MENU_HEIGHT_ATTR);
        int height = (v instanceof Number)? ((Number)v).intValue() : 0;
        return height<0? 0 : height;
    }
}
