package org.officelaf;

import org.netbeans.swing.tabcontrol.TabData;
import org.netbeans.swing.tabcontrol.TabDataModel;
import org.netbeans.swing.tabcontrol.TabDisplayer;
import org.netbeans.swing.tabcontrol.event.TabActionEvent;
import org.netbeans.swing.tabcontrol.plaf.AbstractViewTabDisplayerUI;
import org.netbeans.swing.tabcontrol.plaf.TabControlButton;
import org.netbeans.swing.tabcontrol.plaf.TabControlButtonFactory;
import org.netbeans.swing.tabcontrol.plaf.TabLayoutModel;
import org.openide.awt.HtmlRenderer;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Win XP-like user interface of view type tabs.
 *
 * @author Dafe Simonek
 */
public class OfficeViewTabDisplayerUIOld extends AbstractViewTabDisplayerUI {

    /*********** constants *************/
    /**
     * Space between text and left side of the tab
     */
    private static final int MAIN_ICON_PAD = 4;
    private static final int TXT_X_PAD = 4;
    private static final int TXT_Y_PAD = 3;
    private static final int ICON_X_PAD = 2;
    private static final int HIGHLIGHTED_RAISE = 1;
    /*********** static fields **********/
    /**
     * True when colors were already initialized, false otherwise
     */
    private static Map<Integer, String[]> buttonIconPaths;
    private static Color inactBgColor;
    private static Color actBgColor;

    public static final Color GRAY_76 = new Color(76, 76, 76);

    public static final Color BORDER_COLOR = new Color(0x898c95);

    // Blue focused border color
    public static final Color BORDER_FOCUSED_COLOR = new Color(0x3c7fb1);

    // Yellow focused border color
//    public static final Color BORDER_FOCUSED_COLOR = new Color(0xcead4c);

    public static final Color BORDER_HIGHLIGHT_COLOR = Color.WHITE;

    // Blue background gradient
    public static final Color BG1_FOCUSED_COLOR = new Color(0xf2f9fc);
    public static final Color BG2_FOCUSED_COLOR = new Color(0xf2f9fc);
    public static final Color BG3_FOCUSED_COLOR = new Color(0xe1f1f9);
    public static final Color BG4_FOCUSED_COLOR = new Color(0xd8ecf6);

    // Yellow background gradient
//    private static final Color BG1_FOCUSED_COLOR = new Color(0xfcfaeb);
//    private static final Color BG2_FOCUSED_COLOR = new Color(0xfcfaeb);
//    private static final Color BG3_FOCUSED_COLOR = new Color(0xfaefca);
//    private static final Color BG4_FOCUSED_COLOR = new Color(0xf3e5b1);

    public static final Color BG1_COLOR = new Color(0xe9e9e9);
    public static final Color BG2_COLOR = new Color(0xe5e5e5);
    public static final Color BG3_COLOR = new Color(0xd6d6d6);
    public static final Color BG4_COLOR = new Color(0xcccccc);

    /**
     * ******** instance fields ********
     */
    private Dimension prefSize;
    private Font font;
    private TabControlButton hidePin;
    protected JPanel buttonsPanel;

    private OfficeViewTabDisplayerUIOld(TabDisplayer displayer) {
        super(displayer);
        prefSize = new Dimension(100, 17);
    }

    public static ComponentUI createUI(JComponent c) {
        return new OfficeViewTabDisplayerUIOld((TabDisplayer) c);
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        initIcons();
        c.setOpaque(false);
        getControlButtons();
    }

    @Override
    public void uninstallUI(JComponent arg0) {
        super.uninstallUI(arg0);
        
        if(buttonsPanel != null) {
            displayer.remove(buttonsPanel);
            buttonsPanel = null;
        }
    }

    protected AbstractViewTabDisplayerUI.Controller createController() {
        return new OwnController();
    }

    public Dimension getPreferredSize(JComponent c) {
        FontMetrics fm = getTxtFontMetrics();
        int height = fm == null ? 17 : fm.getAscent() + 2 * fm.getDescent() + 3;
        Insets insets = c.getInsets();
        prefSize.height = height + insets.bottom + insets.top + 2;
        return prefSize;
    }
    
    protected void paintTabContent(Graphics g, int index, String text, int x, int y, int width, int height) {
        FontMetrics fm = getTxtFontMetrics();
        // setting font already here to compute string width correctly
        g.setFont(getTxtFont());
        // highlighted one is higher then others
        if (!isTabInFront(index) && isMoreThanOne()) {
            y += HIGHLIGHTED_RAISE;
            height -= HIGHLIGHTED_RAISE;
        }

        Icon icon = getDataModel().getTab(index).getIcon();
        boolean hasIcon = false;
        if (icon != null && (icon.getIconWidth() <= 16 && icon.getIconHeight() <= 16)) {
            hasIcon = true;
        }

        int txtWidth = width;
        int txtPad = TXT_X_PAD * 2 + (hasIcon ? icon.getIconWidth() + MAIN_ICON_PAD : 0);
        if (isSelected(index)) {
            Component buttons = getControlButtons();
            if (null != buttons) {
                Dimension buttonsSize = buttons.getPreferredSize();
                txtWidth = width - (buttonsSize.width + ICON_X_PAD + txtPad);
                buttons.setLocation(x + txtWidth + txtPad, y + (height - buttonsSize.height) / 2);
            }
        } else {
            txtWidth = width - (txtPad);
        }

        Font font = getTxtFont();
        Color textColor = isDimmed(index) ? Color.WHITE : Color.BLACK;

        if (hasIcon) {
            icon.paintIcon(null, g, x + MAIN_ICON_PAD, y + TXT_Y_PAD);
        }

        int txtLeft = TXT_X_PAD + (hasIcon ? MAIN_ICON_PAD + icon.getIconWidth() : 0);
        if(text != null) {
            HtmlRenderer.renderString(text, g, x + txtLeft, y + fm.getAscent() + TXT_Y_PAD, txtWidth, height, font,
                textColor, HtmlRenderer.STYLE_TRUNCATE, true);
        }
    }

    protected void paintTabBorder(Graphics g, int index, int x, int y, int width, int height) {
        // TODO attention state

        boolean selected  = isSelected(index);
        boolean focused   = selected && isActive();
        boolean attention = isAttention(index);
        boolean underMouse = isUnderMouse(index);
        boolean first = index == 0;
        boolean last = isLast(index);

        Graphics2D g2d = (Graphics2D) g;

        if (!selected) {
            y += 1;
            height -= 1;
        }

        // Draw bottom border
        g2d.setColor(BORDER_COLOR);
        if (!selected) {
            g2d.drawLine(x, y+height-1, x+width-1, y+height-1);
        } else {
            if (first && focused) {
                g2d.setColor(BORDER_FOCUSED_COLOR);
            }
            g2d.drawLine(x, y+height-1, x, y+height-1);
            if (!(last && focused)) {
                g2d.setColor(BORDER_COLOR);
            }
            g2d.drawLine(x+width-1, y+height-1, x+width-1, y+height-1);
        }

        if (focused || underMouse) {
            g2d.setColor(BORDER_FOCUSED_COLOR);
        }

        // Only draw the left border if we're first or selected
        int startX = x;
        if (first || selected) {
            startX = x+1;
            g2d.drawLine(x, y+2, x, y+height-2);
        }
        g2d.drawLine(startX, y+1, startX, y+1); // left corner

        int dx = !last && isSelected(index+1) ? 1 : 0;
        g2d.drawLine(startX+1, y, x+width-3+dx, y); // top
        g2d.drawLine(x+width-2+dx, y+1, x+width-2+dx, y+1); // right corner

        // Draw the right border if we're the last tab or if the next tab isn't selected
        if (last || !isSelected(index+1)) {
            g2d.drawLine(x+width-1, y+2, x+width-1, y+height-2);
        }

        if (!isDimmed(index)) {
            Composite normalComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
            g2d.setColor(underMouse ? BORDER_FOCUSED_COLOR : BORDER_HIGHLIGHT_COLOR);
            g2d.drawLine(startX, y+2, startX, y+height-2); // left
            g2d.drawLine(startX+1, y+1, x+width-3+dx, y+1); // top
            g2d.drawLine(x+width-2+dx, y+2, x+width-2+dx, y+height-2); // right
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.2f));
            g2d.drawLine(startX+1, y+2, startX+1, y+2); // left corner
            g2d.drawLine(x+width-3+dx, y+2, x+width-3+dx, y+2); // right corner
            g2d.setComposite(normalComposite);
        }
    }

    protected void paintTabBackground(Graphics g, int index, int x, int y, int width, int height) {
        boolean selected  = isSelected(index);
        boolean focused   = selected && isActive();
        boolean attention = isAttention(index);
        boolean first = index == 0;
        boolean last = isLast(index);

        if (first || selected) {
            width -= 1;
            x += 1;
        }
        if (!selected) {
            y += 1;
            height -= 2;
        }
        if (!(!last && isSelected(index+1))) {
            width -= 1;
        }

        Color[] gradient = focused ?
                new Color[] {BG1_FOCUSED_COLOR, BG2_FOCUSED_COLOR, BG3_FOCUSED_COLOR, BG4_FOCUSED_COLOR} :
                new Color[] {BG1_COLOR, BG2_COLOR, BG3_COLOR, BG4_COLOR};

        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(new LinearGradientPaint(x, y, x, y+height-1, new float[] {0, 0.49f, 0.5f, 1}, gradient));
        g2d.fillRect(x, y+1, width, height-1);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(GRAY_76);
        g2d.fillRect(0, 0, c.getWidth(), c.getHeight());

        TabDataModel dataModel = getDataModel();
        TabLayoutModel layoutModel = getLayoutModel();

        TabData tabData;
        int x, y, width, height;
        String text;

        for (int i = 0; i < dataModel.size(); i++) {
            boolean dimmed = isDimmed(i);

            // gather data
            tabData = dataModel.getTab(i);
            x = layoutModel.getX(i);
            y = layoutModel.getY(i);
            width = layoutModel.getW(i);
            height = layoutModel.getH(i);
            text = tabData.getText();

            BufferedImage img = null;
            Graphics2D bg = g2d;
            int origX = x;
            int origY = y;
            if (dimmed) {
                x = 0;
                y = 0;
                img = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
                bg = img.createGraphics();
                bg.setClip(0, 0, width, height); // have to set the clip as org.netbeans.awt.HtmlRenderer#_renderPlainString() (line 404) uses getClip() without checking for null values.
                float alpha = isActive() ? 0.7f : 0.5f;
                bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            }

            // perform paint
            if (bg.hitClip(x, y, width, height)) {
                paintTabBackground(bg, i, x, y, width, height);
                paintTabContent(bg, i, text, x, y, width, height);
                paintTabBorder(bg, i, x, y, width, height);
            }

            if (dimmed) {
                bg.dispose();
                g2d.drawImage(img, origX, origY, null);
            }
        }

        g.dispose();
    }

    /**
     * Override to bold font
     */
    protected Font getTxtFont() {
       if(font == null) {
            font = (Font)UIManager.get("windowTitleFont");
            if(font == null) {
                font = new Font("Dialog", Font.PLAIN, 12);
            } else {
                font = new Font(font.getName(), Font.PLAIN, 12);
            }
        }
        
        return font;
    }

    private boolean isDimmed(int index) {
        return !isSelected(index) && !isUnderMouse(index);
    }

    private boolean isUnderMouse(int index) {
        return ((OwnController) getController()).getMouseIndex() == index;
    }

    private boolean isTabInFront(int index) {
        return isSelected(index) && (isActive() || isMoreThanOne());
    }

    private boolean isMoreThanOne() {
        return getDataModel().size() > 1;
    }

    private boolean isLast(int index) {
        return getDataModel().size() - 1 == index;
    }
       
    private static void initIcons() {
        if (null == buttonIconPaths) {
            buttonIconPaths = new HashMap<Integer, String[]>(7);

            //close button
            String[] iconPaths = new String[4];
            iconPaths[TabControlButton.STATE_DEFAULT] = "org/netbeans/swing/tabcontrol/resources/xp_bigclose_enabled.png"; // NOI18N
            iconPaths[TabControlButton.STATE_PRESSED] = "org/netbeans/swing/tabcontrol/resources/xp_bigclose_pressed.png"; // NOI18N
            iconPaths[TabControlButton.STATE_DISABLED] = iconPaths[TabControlButton.STATE_DEFAULT];
            iconPaths[TabControlButton.STATE_ROLLOVER] = "org/netbeans/swing/tabcontrol/resources/xp_bigclose_rollover.png"; // NOI18N
            buttonIconPaths.put(TabControlButton.ID_CLOSE_BUTTON, iconPaths);

            //slide/pin button
            iconPaths = new String[4];
            iconPaths[TabControlButton.STATE_DEFAULT] = "org/netbeans/swing/tabcontrol/resources/xp_slideright_enabled.png"; // NOI18N
            iconPaths[TabControlButton.STATE_PRESSED] = "org/netbeans/swing/tabcontrol/resources/xp_slideright_pressed.png"; // NOI18N
            iconPaths[TabControlButton.STATE_DISABLED] = iconPaths[TabControlButton.STATE_DEFAULT];
            iconPaths[TabControlButton.STATE_ROLLOVER] = "org/netbeans/swing/tabcontrol/resources/xp_slideright_rollover.png"; // NOI18N
            buttonIconPaths.put(TabControlButton.ID_SLIDE_RIGHT_BUTTON, iconPaths);

            iconPaths = new String[4];
            iconPaths[TabControlButton.STATE_DEFAULT] = "org/netbeans/swing/tabcontrol/resources/xp_slideleft_enabled.png"; // NOI18N
            iconPaths[TabControlButton.STATE_PRESSED] = "org/netbeans/swing/tabcontrol/resources/xp_slideleft_pressed.png"; // NOI18N
            iconPaths[TabControlButton.STATE_DISABLED] = iconPaths[TabControlButton.STATE_DEFAULT];
            iconPaths[TabControlButton.STATE_ROLLOVER] = "org/netbeans/swing/tabcontrol/resources/xp_slideleft_rollover.png"; // NOI18N
            buttonIconPaths.put(TabControlButton.ID_SLIDE_LEFT_BUTTON, iconPaths);

            iconPaths = new String[4];
            iconPaths[TabControlButton.STATE_DEFAULT] = "org/netbeans/swing/tabcontrol/resources/xp_slidebottom_enabled.png"; // NOI18N
            iconPaths[TabControlButton.STATE_PRESSED] = "org/netbeans/swing/tabcontrol/resources/xp_slidebottom_pressed.png"; // NOI18N
            iconPaths[TabControlButton.STATE_DISABLED] = iconPaths[TabControlButton.STATE_DEFAULT];
            iconPaths[TabControlButton.STATE_ROLLOVER] = "org/netbeans/swing/tabcontrol/resources/xp_slidebottom_rollover.png"; // NOI18N
            buttonIconPaths.put(TabControlButton.ID_SLIDE_DOWN_BUTTON, iconPaths);

            iconPaths = new String[4];
            iconPaths[TabControlButton.STATE_DEFAULT] = "org/netbeans/swing/tabcontrol/resources/xp_pin_enabled.png"; // NOI18N
            iconPaths[TabControlButton.STATE_PRESSED] = "org/netbeans/swing/tabcontrol/resources/xp_pin_pressed.png"; // NOI18N
            iconPaths[TabControlButton.STATE_DISABLED] = iconPaths[TabControlButton.STATE_DEFAULT];
            iconPaths[TabControlButton.STATE_ROLLOVER] = "org/netbeans/swing/tabcontrol/resources/xp_pin_rollover.png"; // NOI18N
            buttonIconPaths.put(TabControlButton.ID_PIN_BUTTON, iconPaths);
        }
    }

    /**
     * @return A component that holds all control buttons (maximize/restor, 
     * slide/pin, close) that are displayed in the active tab or null if
     * control buttons are not supported.
     */
    protected Component getControlButtons() {
        if( null == buttonsPanel ) {
            buttonsPanel = new JPanel( null );
            buttonsPanel.setOpaque( false );

            //Border border = BorderFactory.createLineBorder(Color.RED);//createBorder();
            Border border = createBorder();
            buttonsPanel.setBorder(border);

            Insets insets = border.getBorderInsets(buttonsPanel);
            int width  = insets.left;
            int height = insets.top;
            

            //create autohide/pin button
            if( null != displayer.getWinsysInfo() ) {
                hidePin = TabControlButtonFactory.createSlidePinButton( displayer );
                buttonsPanel.add( hidePin  );
               
                Icon icon = hidePin.getIcon();
                hidePin.setBounds( width, 0, icon.getIconWidth(), icon.getIconHeight() );
                width += icon.getIconWidth();
            }

            //create close button
            TabControlButton btnClose = TabControlButtonFactory.createCloseButton( displayer );
            buttonsPanel.add( btnClose );

            Icon icon = btnClose.getIcon();
            if( 0 != width )
                width += ICON_X_PAD;
            btnClose.setBounds( width, 0, icon.getIconWidth(), icon.getIconHeight() );
            width += icon.getIconWidth();
            
            height += hidePin.getHeight() > btnClose.getHeight() ? hidePin.getHeight() : btnClose.getHeight();
            
            width  += insets.right;
            height += insets.bottom;

            Rectangle r = hidePin.getBounds();
            r.y =  height/2 - r.height/2;
            hidePin.setBounds(r);
            
            r = btnClose.getBounds();
            r.y =  height/2 - r.height/2;
            btnClose.setBounds(r);
            
            Dimension size = new Dimension( width, height );
            buttonsPanel.setMinimumSize( size );
            buttonsPanel.setSize( size );
            buttonsPanel.setPreferredSize( size );
            buttonsPanel.setMaximumSize( size );


            try {
                Field bthidePin = AbstractViewTabDisplayerUI.class.getDeclaredField("btnAutoHidePin");
                if(bthidePin != null) {
                    bthidePin.setAccessible(true);
                    bthidePin.set(this, hidePin);
                }
            } catch(Throwable e) {
                e.printStackTrace();
            }        
        }
        
        return buttonsPanel;
    }
        
   
    public Icon getButtonIcon(int buttonId, int buttonState) {
        Icon res = null;
        initIcons();
        
        if(buttonId == TabControlButton.ID_SLIDE_RIGHT_BUTTON) {
            return new DoubleArrowIcon(DoubleArrowIcon.Orientation.RIGHT);
        } else if(buttonId == TabControlButton.ID_SLIDE_LEFT_BUTTON) {
            return new DoubleArrowIcon(DoubleArrowIcon.Orientation.LEFT);
        } else if(buttonId == TabControlButton.ID_SLIDE_DOWN_BUTTON) {
            return new DoubleArrowIcon(DoubleArrowIcon.Orientation.DOWN);
        } else {
            String[] paths = buttonIconPaths.get(buttonId);
            if (null != paths && buttonState >= 0 && buttonState < paths.length) {
                res = TabControlButtonFactory.getIcon(paths[buttonState]);
            }
        }
        return res;
    }

    public void postTabAction(TabActionEvent e) {
        super.postTabAction(e);
        if (TabDisplayer.COMMAND_MAXIMIZE.equals(e.getActionCommand())) {
            ((OwnController) getController()).updateHighlight(-1);
        }
    }


    /**
     * Own close icon button controller
     */
    private class OwnController extends Controller {

        /**
         * holds index of tab in which mouse pointer was lastly located. -1
         * means mouse pointer is out of component's area
         */
        // TBD - should be part of model, not controller
        private int lastIndex = -1;

        /**
         * @return Index of tab in which mouse pointer is currently located.
         */
        public int getMouseIndex() {
            return lastIndex;
        }

        public boolean inControlButtonsRect(Point p) {
            if(null != buttonsPanel) {
                Point p2 = SwingUtilities.convertPoint(displayer, p, buttonsPanel);
                return buttonsPanel.contains(p2);
            }

            return false;
        }
        
        
        /**
         * Triggers visual tab header change when mouse enters/leaves tab in
         * advance to superclass functionality.
         */
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            Point pos = e.getPoint();
            if (!e.getSource().equals(displayer)) {
                pos = SwingUtilities.convertPoint((Component) e.getSource(), pos, displayer);
            }
            updateHighlight(getLayoutModel().indexOfPoint(pos.x, pos.y));
        }

        /**
         * Resets tab header in advance to superclass functionality
         */
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            if (!inControlButtonsRect(e.getPoint())) {
                updateHighlight(-1);
            }
        }

        /**
         * Invokes repaint of dirty region if needed
         */
        private void updateHighlight(int curIndex) {
            if (curIndex == lastIndex) {
                return;
            }
            // compute region which needs repaint
            TabLayoutModel tlm = getLayoutModel();
            int x, y, w, h;
            Rectangle repaintRect = null;
            if (curIndex != -1) {
                x = tlm.getX(curIndex);
                y = tlm.getY(curIndex);
                w = tlm.getW(curIndex);
                h = tlm.getH(curIndex);
                repaintRect = new Rectangle(x, y, w, h);
            }
            // due to model changes, lastIndex may become invalid, so check
            if ((lastIndex != -1) && (lastIndex < getDataModel().size())) {
                x = tlm.getX(lastIndex);
                y = tlm.getY(lastIndex);
                w = tlm.getW(lastIndex);
                h = tlm.getH(lastIndex);
                if (repaintRect != null) {
                    repaintRect =
                            repaintRect.union(new Rectangle(x, y, w, h));
                } else {
                    repaintRect = new Rectangle(x, y, w, h);
                }
            }
            // trigger repaint if needed, update index
            if (repaintRect != null) {
                getDisplayer().repaint(repaintRect);
            }
            lastIndex = curIndex;
        }

        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            mouseMoved(e);
        }
    } // end of OwnController

    private Border createBorder() {
        Border empty = BorderFactory.createEmptyBorder(4, 3, 4, 1);
        Border outer = new AbstractBorder() {
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g;
                Color oldColor = g2d.getColor();
                int startY = y + 2;
                int stopY = y + height - 3;

                g2d.setColor(new Color(145, 153, 164));
                g2d.drawLine(x, startY, x, stopY);
                g2d.setColor(new Color(221, 224, 227));
                g2d.drawLine(x + 1, startY, x + 1, stopY);

                //g2d.setColor(Color.RED);
                //g2d.drawRect(x, y, width-1, height-1);
                g2d.setColor(oldColor);
            }

            public Insets getBorderInsets(Component c) {
                return new Insets(0, 2, 0, 0);
            }

            public Insets getBorderInsets(Component c, Insets insets) {
                insets.left = 2;
                insets.top = 0;
                insets.right = 0;
                insets.bottom = 0;
                return insets;
            }
        };
        
        return BorderFactory.createCompoundBorder(outer, empty);
    }

    static Color getInactBgColor() {
        if (inactBgColor == null) {
            inactBgColor = (Color) UIManager.get("inactiveCaption");
            if (inactBgColor == null) {
                inactBgColor = new Color(204, 204, 204);
            }
        }
        return inactBgColor;
    }

    static Color getActBgColor() {
        if (actBgColor == null) {
            actBgColor = (Color) UIManager.get("activeCaption");
            if (actBgColor == null) {
                actBgColor = new Color(204, 204, 255);
            }
        }
        return actBgColor;
    }
}
