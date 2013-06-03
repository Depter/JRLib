package org.jreserve.gui.misc.officelaf;

import org.netbeans.swing.plaf.LFCustoms;
import org.netbeans.swing.plaf.util.GuaranteedValue;
import org.netbeans.swing.plaf.util.UIBootstrapValue;
import org.netbeans.swing.plaf.util.UIUtils;
import org.openide.windows.TopComponent;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

/**
 *
 * @author mikael
 */
public class OfficeLFCustoms extends LFCustoms {
    private static final String TAB_FOCUS_FILL_UPPER        = "tab_focus_fill_upper";        //NOI18N    
    private static final String TAB_FOCUS_FILL_DARK_LOWER   = "tab_focus_fill_dark_lower";   //NOI18N    
    private static final String TAB_FOCUS_FILL_BRIGHT_LOWER = "tab_focus_fill_bright_lower"; //NOI18N 
    
    private static final String TAB_UNSEL_FILL_DARK_UPPER   = "tab_unsel_fill_dark_upper";   //NOI18N
    private static final String TAB_UNSEL_FILL_BRIGHT_UPPER = "tab_unsel_fill_bright_upper"; //NOI18N  
    private static final String TAB_UNSEL_FILL_DARK_LOWER   = "tab_unsel_fill_dark_lower";   //NOI18N
    private static final String TAB_UNSEL_FILL_BRIGHT_LOWER = "tab_unsel_fill_bright_lower"; //NOI18N  

    private static final String TAB_SEL_FILL = "tab_sel_fill"; //NOI18N
    
    private static final String TAB_MOUSE_OVER_FILL_BRIGHT_UPPER = "tab_mouse_over_fill_bright_upper"; //NOI18N
    private static final String TAB_MOUSE_OVER_FILL_DARK_UPPER   = "tab_mouse_over_fill_dark_upper";   //NOI18N
    private static final String TAB_MOUSE_OVER_FILL_BRIGHT_LOWER = "tab_mouse_over_fill_bright_lower"; //NOI18N
    private static final String TAB_MOUSE_OVER_FILL_DARK_LOWER   = "tab_mouse_over_fill_dark_lower";   //NOI18N

    private static final String TAB_BORDER       = "tab_border";        //NOI18N      
    private static final String TAB_SEL_BORDER   = "tab_sel_border";    //NOI18N
    private static final String TAB_BORDER_INNER = "tab_border_inner";  //NOI18N      
    
    static final String SCROLLPANE_BORDER_COLOR  = "scrollpane_border"; //NOI18N

    public Object[] createLookAndFeelCustomizationKeysAndValues() {
        int fontsize = 11;
        Integer in = (Integer) UIManager.get(CUSTOM_FONT_SIZE); //NOI18N
        if (in != null) {
            fontsize = in;
        }

        return new Object[] {
            // Work around a bug in windows which sets the text area font to
            // "MonoSpaced", causing all accessible dialogs to have monospaced text
            "TextArea.font", new GuaranteedValue ("Label.font", new Font("Dialog", Font.PLAIN, fontsize)),

            EDITOR_ERRORSTRIPE_SCROLLBAR_INSETS, new Insets(17, 0, 17, 0),
        };
    }

    public Object[] createApplicationSpecificKeysAndValues () {
        UIBootstrapValue editorTabsUI = new VistaEditorColorings ("org.jreserve.gui.misc.officelaf.OfficeEditorTabDisplayerUI");
        Object viewTabsUI = editorTabsUI.createShared("org.jreserve.gui.misc.officelaf.OfficeViewTabDisplayerUIOld");
        Image explorerIcon = UIUtils.loadImage("org/netbeans/swing/plaf/resources/vista_folder.png");
        Object propertySheetValues = new OfficePropertySheetColorings();

        Object[] uiDefaults = {
            EDITOR_TAB_DISPLAYER_UI,     editorTabsUI,
            VIEW_TAB_DISPLAYER_UI,       viewTabsUI,
            SLIDING_TAB_DISPLAYER_UI,    "org.jreserve.gui.misc.officelaf.OfficeSlidingTabDisplayerUI",

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

            EDITOR_TABBED_CONTAINER_UI, "org.jreserve.gui.misc.officelaf.OfficeTabbedContainerUI",

            // progress component related
            "nbProgressBar.Foreground",                 new Color(49, 106, 197),
            "nbProgressBar.Background",                 Color.WHITE,
            "nbProgressBar.popupDynaText.foreground",   new Color(115, 115, 115),
            "nbProgressBar.popupText.background",       new Color(249, 249, 249),        
            "nbProgressBar.popupText.foreground",       UIManager.getColor("TextField.foreground"),
            "nbProgressBar.popupText.selectBackground", UIManager.getColor("List.selectionBackground"),
            "nbProgressBar.popupText.selectForeground", UIManager.getColor("List.selectionForeground"),                    
            PROGRESS_CANCEL_BUTTON_ICON,                UIUtils.loadImage("org/netbeans/swing/plaf/resources/vista_mini_close_enabled.png"),
            PROGRESS_CANCEL_BUTTON_ROLLOVER_ICON,       UIUtils.loadImage("org/netbeans/swing/plaf/resources/vista_mini_close_over.png"),
            PROGRESS_CANCEL_BUTTON_PRESSED_ICON,        UIUtils.loadImage("org/netbeans/swing/plaf/resources/vista_mini_close_pressed.png")
        }; //NOI18N
        
        //Workaround for JDK 1.5.0 bug 5080144 - Disabled JTextFields stay white
        //XPTheme uses Color instead of ColorUIResource
        convert ("TextField.background"); //NOI18N
        convert ("TextField.inactiveBackground"); //NOI18N
        convert ("TextField.disabledBackground");  //NOI18N

        return uiDefaults;
    }
    
    /*
     * Takes a UIManager color key and ensures that it is stored as a 
     * ColorUIResource, not a Color. 
     */
    protected static void convert (String key) {
        Color c = UIManager.getColor(key);
        if (c != null && !(c instanceof ColorUIResource)) {
            UIManager.put (key, new ColorUIResource(c));
        }
    }
    
    protected Object[] additionalKeys() {
        Object[] kv     = new VistaEditorColorings("").createKeysAndValues();
        Object[] kv2    = new OfficePropertySheetColorings().createKeysAndValues();
        Object[] result = new Object[(kv.length / 2) + (kv2.length / 2)];
        int ct = 0;
        for (int i=0; i < kv.length; i+=2) {
            result[ct] = kv[i];
            ct++;
        }
        for (int i=0; i < kv2.length; i+=2) {
            result[ct] = kv2[i];
            ct++;
        }
        return result;
    }    

    protected class VistaEditorColorings extends UIBootstrapValue.Lazy {
        public VistaEditorColorings (String name) {
            super (name);
        }

        public Object[] createKeysAndValues() {
            return new Object[] {
            //Tab control - XXX REPLACE WITH RelativeColor - need to figure out base
            //colors for each color selected & focused
            TAB_FOCUS_FILL_UPPER,        new Color(242, 249, 252),
            TAB_FOCUS_FILL_BRIGHT_LOWER, new Color(225, 241, 249),
            TAB_FOCUS_FILL_DARK_LOWER,   new Color(216, 236, 246),
            
            //no selection, no focus
            TAB_UNSEL_FILL_BRIGHT_UPPER, new Color(235,235,235),
            TAB_UNSEL_FILL_DARK_UPPER,   new Color(229, 229, 229),
            TAB_UNSEL_FILL_BRIGHT_LOWER, new Color(214,214,214),
            TAB_UNSEL_FILL_DARK_LOWER,   new Color(203, 203, 203),
            
            //selected, no focus
            TAB_SEL_FILL, new Color(244,244,244),
            
            //no selection, mouse over
            TAB_MOUSE_OVER_FILL_BRIGHT_UPPER, new Color(223,242,252),
            TAB_MOUSE_OVER_FILL_DARK_UPPER,   new Color(214,239,252),
            TAB_MOUSE_OVER_FILL_BRIGHT_LOWER, new Color(189,228,250),
            TAB_MOUSE_OVER_FILL_DARK_LOWER,   new Color(171,221,248),
            
            TAB_BORDER,       new Color(137,140,149),
            TAB_SEL_BORDER,   new Color(60,127,177),
            TAB_BORDER_INNER, new Color(255,255,255),

            //Borders for the tab control
            EDITOR_TAB_OUTER_BORDER,   BorderFactory.createEmptyBorder(),
            EDITOR_TAB_CONTENT_BORDER, BorderFactory.createEmptyBorder(),
            EDITOR_TAB_TABS_BORDER,    BorderFactory.createEmptyBorder(),

            VIEW_TAB_OUTER_BORDER, BorderFactory.createEmptyBorder(),
            VIEW_TAB_CONTENT_BORDER,   new ViewTabContentBorder(),
            VIEW_TAB_TABS_BORDER,      BorderFactory.createEmptyBorder()
            };
        }
    }

    protected static class ViewTabContentBorder extends AbstractBorder {
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 1, 1, 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = 0;
            insets.left = insets.bottom = insets.right = 1;
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            TopComponent active = TopComponent.getRegistry().getActivated();
            if (active != null && ((Container) c).isAncestorOf(active)) {
                g.setColor(OfficeViewTabDisplayerUIOld.BORDER_FOCUSED_COLOR);
            } else {
                g.setColor(OfficeViewTabDisplayerUIOld.BORDER_COLOR);
            }
            
            g.drawLine(x, y, x, y+height-1);
            g.drawLine(x+width-1, y, x+width-1, y+height-1);
            g.drawLine(x, y+height-1, x+width-1, y+height-1);
        }
    }

    protected class OfficePropertySheetColorings extends UIBootstrapValue.Lazy {
        public OfficePropertySheetColorings () {
            super ("propertySheet");  //NOI18N
        }

        public Object[] createKeysAndValues() {
            return new Object[] {
                PROPSHEET_SELECTION_BACKGROUND,    new Color(49,106,197),
                PROPSHEET_SELECTION_FOREGROUND,    Color.WHITE,
                PROPSHEET_SET_BACKGROUND,          new Color(213,213,213),
                PROPSHEET_SET_FOREGROUND,          Color.BLACK,
                PROPSHEET_SELECTED_SET_BACKGROUND, new Color(49,106,197),
                PROPSHEET_SELECTED_SET_FOREGROUND, Color.WHITE,
                PROPSHEET_DISABLED_FOREGROUND,     new Color(161,161,146),
                PROPSHEET_BUTTON_FOREGROUND,       Color.BLACK,
            };
        }
    }
}
