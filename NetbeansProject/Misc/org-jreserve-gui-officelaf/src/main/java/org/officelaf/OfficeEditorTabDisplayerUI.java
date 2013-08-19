package org.officelaf;

import org.netbeans.swing.tabcontrol.TabDisplayer;
import org.netbeans.swing.tabcontrol.plaf.BasicScrollingTabDisplayerUI;
import org.netbeans.swing.tabcontrol.plaf.TabCellRenderer;
import org.netbeans.swing.tabcontrol.plaf.TabControlButton;
import org.netbeans.swing.tabcontrol.plaf.TabControlButtonFactory;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class OfficeEditorTabDisplayerUI extends BasicScrollingTabDisplayerUI {
    private static final Rectangle scratch5 = new Rectangle();
    private static Map<Integer, String[]> buttonIconPaths;

    public OfficeEditorTabDisplayerUI(final TabDisplayer displayer) {
        super (displayer);
    }

    public static ComponentUI createUI(JComponent c) {
        return new OfficeEditorTabDisplayerUI ((TabDisplayer) c);
    }

    public Dimension getPreferredSize(JComponent c) {
        int prefHeight = 23;
        Graphics g = BasicScrollingTabDisplayerUI.getOffscreenGraphics();
        if (g != null) {
            FontMetrics fm = g.getFontMetrics(displayer.getFont());
            Insets ins = getTabAreaInsets();
            prefHeight = Math.max(prefHeight, fm.getHeight() + ins.top + ins.bottom + 6);
        }
        return new Dimension(displayer.getWidth(), prefHeight);
    }

    @Override
    public void paintBackground(Graphics g) {
        g.setColor (new Color(76,76,76));
        g.fillRect (0, 0, displayer.getWidth(), displayer.getHeight());
    }

    @Override
    protected void paintAfterTabs(Graphics g) {
        Rectangle r = new Rectangle();
        getTabsVisibleArea(r);
        r.width = displayer.getWidth();

        int y = displayer.getHeight();
        int tabsWidth = getTabsAreaWidth();

        g.setColor(OfficeViewTabDisplayerUIOld.BORDER_COLOR);

        //Find the last visible tab
        int last = scroll().getLastVisibleTab(tabsWidth);
        if (last >= 0) {
            //If it's onscreen (usually will be unless there are no tabs,
            //find the edge of the last tab - it may be scrolled)
            getTabRect(last, scratch5);
            last = scratch5.x + scratch5.width;
        }
        //Draw the dark line under the controls button area that closes the
        //tabs bottom margin on top
        g.drawLine(last, y - 1, displayer.getWidth(), y - 1);
    }

    @Override
    protected TabCellRenderer createDefaultRenderer() {
        return new OfficeEditorTabCellRenderer2();
    }

    private static void initIcons() {
        if( null == buttonIconPaths ) {
            buttonIconPaths = new HashMap<Integer, String[]>(7);
            
            //left button
            String[] iconPaths = new String[4];
            iconPaths[TabControlButton.STATE_DEFAULT] = "org/netbeans/swing/tabcontrol/resources/vista_scrollleft_enabled.png"; // NOI18N
            iconPaths[TabControlButton.STATE_DISABLED] = "org/netbeans/swing/tabcontrol/resources/vista_scrollleft_disabled.png"; // NOI18N
            iconPaths[TabControlButton.STATE_ROLLOVER] = "org/netbeans/swing/tabcontrol/resources/vista_scrollleft_rollover.png"; // NOI18N
            iconPaths[TabControlButton.STATE_PRESSED] = "org/netbeans/swing/tabcontrol/resources/vista_scrollleft_pressed.png"; // NOI18N
            buttonIconPaths.put( TabControlButton.ID_SCROLL_LEFT_BUTTON, iconPaths );
            
            //right button
            iconPaths = new String[4];
            iconPaths[TabControlButton.STATE_DEFAULT] = "org/netbeans/swing/tabcontrol/resources/vista_scrollright_enabled.png"; // NOI18N
            iconPaths[TabControlButton.STATE_DISABLED] = "org/netbeans/swing/tabcontrol/resources/vista_scrollright_disabled.png"; // NOI18N
            iconPaths[TabControlButton.STATE_ROLLOVER] = "org/netbeans/swing/tabcontrol/resources/vista_scrollright_rollover.png"; // NOI18N
            iconPaths[TabControlButton.STATE_PRESSED] = "org/netbeans/swing/tabcontrol/resources/vista_scrollright_pressed.png"; // NOI18N
            buttonIconPaths.put( TabControlButton.ID_SCROLL_RIGHT_BUTTON, iconPaths );
            
            //drop down button
            iconPaths = new String[4];
            iconPaths[TabControlButton.STATE_DEFAULT] = "org/netbeans/swing/tabcontrol/resources/vista_popup_enabled.png"; // NOI18N
            iconPaths[TabControlButton.STATE_DISABLED] = iconPaths[TabControlButton.STATE_DEFAULT];
            iconPaths[TabControlButton.STATE_ROLLOVER] = "org/netbeans/swing/tabcontrol/resources/vista_popup_rollover.png"; // NOI18N
            iconPaths[TabControlButton.STATE_PRESSED] = "org/netbeans/swing/tabcontrol/resources/vista_popup_pressed.png"; // NOI18N
            buttonIconPaths.put( TabControlButton.ID_DROP_DOWN_BUTTON, iconPaths );
            
            //maximize/restore button
            iconPaths = new String[4];
            iconPaths[TabControlButton.STATE_DEFAULT] = "org/netbeans/swing/tabcontrol/resources/vista_maximize_enabled.png"; // NOI18N
            iconPaths[TabControlButton.STATE_DISABLED] = iconPaths[TabControlButton.STATE_DEFAULT];
            iconPaths[TabControlButton.STATE_ROLLOVER] = "org/netbeans/swing/tabcontrol/resources/vista_maximize_rollover.png"; // NOI18N
            iconPaths[TabControlButton.STATE_PRESSED] = "org/netbeans/swing/tabcontrol/resources/vista_maximize_pressed.png"; // NOI18N
            buttonIconPaths.put( TabControlButton.ID_MAXIMIZE_BUTTON, iconPaths );
            
            iconPaths = new String[4];
            iconPaths[TabControlButton.STATE_DEFAULT] = "org/netbeans/swing/tabcontrol/resources/vista_restore_enabled.png"; // NOI18N
            iconPaths[TabControlButton.STATE_DISABLED] = iconPaths[TabControlButton.STATE_DEFAULT];
            iconPaths[TabControlButton.STATE_ROLLOVER] = "org/netbeans/swing/tabcontrol/resources/vista_restore_rollover.png"; // NOI18N
            iconPaths[TabControlButton.STATE_PRESSED] = "org/netbeans/swing/tabcontrol/resources/vista_restore_pressed.png"; // NOI18N
            buttonIconPaths.put( TabControlButton.ID_RESTORE_BUTTON, iconPaths );
        }
    }

    @Override
    public Icon getButtonIcon(int buttonId, int buttonState) {
        Icon res = null;
        initIcons();
        String[] paths = buttonIconPaths.get( buttonId );
        if( null != paths && buttonState >=0 && buttonState < paths.length ) {
            res = TabControlButtonFactory.getIcon( paths[buttonState] );
        }
        return res;
    }
}
