package org.officelaf;
import java.awt.Color;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import org.officelaf.util.UIBootstrapValue;
import org.openide.util.ImageUtilities;

/**
 *
 * @author mikael
 */
public class OfficeNimbusLFCustoms extends OfficeLFCustoms {

    public OfficeNimbusLFCustoms() {
    }

    @Override
    protected Object[] additionalKeys() {
        return super.additionalKeys();
    }

    @Override
    public Object[] createApplicationSpecificKeysAndValues() {
        UIBootstrapValue editorTabsUI = new VistaEditorColorings ("org.officelaf.OfficeEditorTabDisplayerUI");
        Object viewTabsUI = editorTabsUI.createShared("org.officelaf.OfficeViewTabDisplayerUIOld");
        Image explorerIcon = ImageUtilities.loadImage("org/netbeans/swing/plaf/resources/vista_folder.png");
        Object propertySheetValues = new OfficePropertySheetColorings();

        Object[] uiDefaults = {
            EDITOR_TAB_DISPLAYER_UI,     editorTabsUI,
            VIEW_TAB_DISPLAYER_UI,       viewTabsUI,
            SLIDING_TAB_DISPLAYER_UI,    "org.officelaf.OfficeSlidingTabDisplayerUI",

            SCROLLPANE_BORDER_COLOR,     new Color(127, 157, 185),
            SCROLLPANE_BORDER_COLOR,     Color.RED,
            DESKTOP_BORDER,              new EmptyBorder(0, 5, 4, 6),
            SCROLLPANE_BORDER,           null,//UIManager.get("ScrollPane.border"),
            EXPLORER_STATUS_BORDER,      new StatusLineBorder(StatusLineBorder.TOP),
            EXPLORER_FOLDER_ICON ,       explorerIcon,
            EXPLORER_FOLDER_OPENED_ICON, explorerIcon,

            EDITOR_STATUS_LEFT_BORDER,   new StatusLineBorder(StatusLineBorder.TOP | StatusLineBorder.RIGHT),
            EDITOR_STATUS_RIGHT_BORDER,  new StatusLineBorder(StatusLineBorder.TOP | StatusLineBorder.LEFT),
            EDITOR_STATUS_INNER_BORDER,  new StatusLineBorder(StatusLineBorder.TOP | StatusLineBorder.LEFT | StatusLineBorder.RIGHT),
            EDITOR_STATUS_ONLYONEBORDER, new StatusLineBorder(StatusLineBorder.TOP),

            EDITOR_TOOLBAR_BORDER,       new EditorToolbarBorder(),
            OUTPUT_SELECTION_BACKGROUND, new Color (164, 180, 255),

            EDITOR_TAB_OUTER_BORDER,     new BorderUIResource(BorderFactory.createEmptyBorder()),
            VIEW_TAB_OUTER_BORDER,       new BorderUIResource(BorderFactory.createEmptyBorder()),

            PROPERTYSHEET_BOOTSTRAP,     propertySheetValues,

            DESKTOP_BACKGROUND,          new ColorUIResource(255,76,76),
            WORKPLACE_FILL, Color.BLUE,

            DESKTOP_SPLITPANE_BORDER, BorderFactory.createEmptyBorder(4, 0, 0, 0),
            SLIDING_BUTTON_UI,        "org.netbeans.swing.tabcontrol.plaf.WinVistaSlidingButtonUI",

            EDITOR_TABBED_CONTAINER_UI, "org.officelaf.OfficeTabbedContainerUI",

            // progress component related
            "nbProgressBar.Foreground",                 new Color(49, 106, 197),
            "nbProgressBar.Background",                 Color.WHITE,
            "nbProgressBar.popupDynaText.foreground",   new Color(115, 115, 115),
            "nbProgressBar.popupText.background",       new Color(249, 249, 249),        
            "nbProgressBar.popupText.foreground",       UIManager.getColor("TextField.foreground"),
            "nbProgressBar.popupText.selectBackground", UIManager.getColor("List.selectionBackground"),
            "nbProgressBar.popupText.selectForeground", UIManager.getColor("List.selectionForeground"),                    
            PROGRESS_CANCEL_BUTTON_ICON,                ImageUtilities.loadImage("org/netbeans/swing/plaf/resources/vista_mini_close_enabled.png"),
            PROGRESS_CANCEL_BUTTON_ROLLOVER_ICON,       ImageUtilities.loadImage("org/netbeans/swing/plaf/resources/vista_mini_close_over.png"),
            PROGRESS_CANCEL_BUTTON_PRESSED_ICON,        ImageUtilities.loadImage("org/netbeans/swing/plaf/resources/vista_mini_close_pressed.png"),

            //UI Delegates for the tab control
            EDITOR_TAB_DISPLAYER_UI,   "org.netbeans.swing.tabcontrol.plaf.NimbusEditorTabDisplayerUI", //NOI18N
            VIEW_TAB_DISPLAYER_UI,     "org.netbeans.swing.tabcontrol.plaf.NimbusViewTabDisplayerUI", //NOI18N
            SLIDING_TAB_BUTTON_UI,     "org.netbeans.swing.tabcontrol.plaf.SlidingTabDisplayerButtonUI", //NOI18N
            SLIDING_BUTTON_UI,         "org.netbeans.swing.tabcontrol.plaf.NimbusSlidingButtonUI", //NOI18N
            SCROLLPANE_BORDER,         new JScrollPane().getViewportBorder(),

            //Borders for the tab control
            EDITOR_TAB_OUTER_BORDER,   BorderFactory.createEmptyBorder(),
            EDITOR_TAB_CONTENT_BORDER, new MatteBorder(0, 1, 1, 1, UIManager.getColor("nimbusBorder")), //NOI18N
            EDITOR_TAB_TABS_BORDER,    BorderFactory.createEmptyBorder(),

            VIEW_TAB_OUTER_BORDER,     BorderFactory.createEmptyBorder(),
            VIEW_TAB_CONTENT_BORDER,   new MatteBorder(0, 1, 1, 1, UIManager.getColor("nimbusBorder")), //NOI18N
            VIEW_TAB_TABS_BORDER,      BorderFactory.createEmptyBorder(),
        
        }; //NOI18N
        
        //Workaround for JDK 1.5.0 bug 5080144 - Disabled JTextFields stay white
        //XPTheme uses Color instead of ColorUIResource
        convert ("TextField.background"); //NOI18N
        convert ("TextField.inactiveBackground"); //NOI18N
        convert ("TextField.disabledBackground");  //NOI18N
        //return super.createApplicationSpecificKeysAndValues();
        return uiDefaults;
    }

    @Override
    public Object[] createLookAndFeelCustomizationKeysAndValues() {
        return super.createLookAndFeelCustomizationKeysAndValues();
    }


}
