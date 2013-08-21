package org.officelaf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import org.openide.util.ImageUtilities;
import org.pushingpixels.flamingo.api.common.CommandButtonLayoutManager;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.model.PopupButtonModel;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.internal.ui.ribbon.appmenu.BasicRibbonApplicationMenuButtonUI;
import org.pushingpixels.flamingo.internal.ui.ribbon.appmenu.JRibbonApplicationMenuButton;

public class OfficeRibbonApplicationMenuButtonUI extends BasicRibbonApplicationMenuButtonUI {

    public static ComponentUI createUI(JComponent c) {
        JRibbon ribbon = getRibbonFromComponent(c);
        return new OfficeRibbonApplicationMenuButtonUI(ribbon);
    }
        
    private static JRibbon getRibbonFromComponent(JComponent c) {
        if(c instanceof JRibbon)
            return (JRibbon)c;
        if(c instanceof JRibbonApplicationMenuButton)
            return ((JRibbonApplicationMenuButton)c).getRibbon();
        throw new IllegalArgumentException("Can not obtain JRibbon from: "+c);
    }
    
    private OfficeRibbonApplicationMenuButtonUI(JRibbon ribbon) {
        super(ribbon);
    }

    @Override
    protected void installComponents() {
        super.installComponents();

        final JRibbonApplicationMenuButton appMenuButton = (JRibbonApplicationMenuButton) this.commandButton;
        appMenuButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(final JCommandButton commandButton) {
                if (appMenuButton.getParent() instanceof JRibbon) {
                    final JRibbon ribbon = (JRibbon) appMenuButton.getParent();
                    RibbonApplicationMenu ribbonMenu = ribbon.getApplicationMenu();
                    final OfficeRibbonApplicationMenuPopupPanel menuPopupPanel = new OfficeRibbonApplicationMenuPopupPanel(
                            appMenuButton, ribbonMenu);
                    menuPopupPanel.setCustomizer(new JPopupPanel.PopupPanelCustomizer() {

                        @Override
                        public Rectangle getScreenBounds() {
                            int x = ribbon.getLocationOnScreen().x;
                            int y = commandButton.getLocationOnScreen().y
                                    + commandButton.getSize().height / 2
                                    + 2;

                            // make sure that the menu popup stays
                            // in bounds
                            Rectangle scrBounds = commandButton.getGraphicsConfiguration().getBounds();
                            int pw = menuPopupPanel.getPreferredSize().width;
                            if ((x + pw) > (scrBounds.x + scrBounds.width)) {
                                x = scrBounds.x + scrBounds.width - pw;
                            }
                            int ph = menuPopupPanel.getPreferredSize().height;
                            if ((y + ph) > (scrBounds.y + scrBounds.height)) {
                                y = scrBounds.y + scrBounds.height - ph;
                            }
                            
                            int height = Math.max(menuPopupPanel.getPreferredSize().height, getMinPopupHeight());
                            return new Rectangle(
                                    x,
                                    y,
                                    menuPopupPanel.getPreferredSize().width,
                                    height);
                        }
                    });
                    return menuPopupPanel;
                }
                return null;
            }
        });
    }
    
    private int getMinPopupHeight() {
        String prop = System.getProperty("ribbon.popupMinHeight");
        if(prop == null || prop.length() == 0)
            return 0;
        try {
            return Integer.parseInt(prop);
        } catch (Exception ex) {
            return 0;
        }
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g.create();
        Insets ins = c.getInsets();
        Rectangle backgroundRect = new Rectangle(ins.left, ins.top, c.getWidth()
                - ins.left - ins.right, c.getHeight() - ins.top - ins.bottom);
        this.paintButtonBackground(g2d, backgroundRect);
        CommandButtonLayoutManager.CommandButtonLayoutInfo layoutInfo = this.layoutManager.getLayoutInfo(this.commandButton, g);
        commandButton.putClientProperty("icon.bounds", layoutInfo.iconRect);
        this.paintButtonIcon(g2d, backgroundRect);
        g2d.dispose();
    }

    private final static String IMG_APP_BUTTON = "org/officelaf/images/appbutton.png"; //NOI18
    private final static String IMG_APP_BUTTON_DOWN = "org/officelaf/images/appbutton_down.png"; //NOI18
    private final static String IMG_APP_BUTTON_OVER = "org/officelaf/images/appbutton_over.png"; //NOI18
    private static final ImageIcon normal = ImageUtilities.loadImageIcon(IMG_APP_BUTTON, false);
    private static final ImageIcon over = ImageUtilities.loadImageIcon(IMG_APP_BUTTON_OVER, false);
    private static final ImageIcon down = ImageUtilities.loadImageIcon(IMG_APP_BUTTON_DOWN, false);

//    static {
//        // TODO These need to be provided somehow
//        normal = new ImageIcon(OfficeRibbonApplicationMenuButtonUI.class.getResource("images/exie_officebutton.png"));
//        over   = new ImageIcon(OfficeRibbonApplicationMenuButtonUI.class.getResource("images/exie_officebutton_over.png"));
//        down   = new ImageIcon(OfficeRibbonApplicationMenuButtonUI.class.getResource("images/exie_officebutton_down.png"));
//    }

    @Override
    protected void configureRenderer() {
        // not using the renderer, but instantiating it to prevent NPEs in the super class
        this.buttonRendererPane = new CellRendererPane();
        this.rendererButton = new JButton("");
    }

    @Override
    protected void unconfigureRenderer() {
        this.buttonRendererPane = null;
        this.rendererButton = null;
    }

    @Override
    protected void paintButtonBackground(Graphics graphics, Rectangle toFill) {
        // No background
    }

    @Override
    protected Icon getIconToPaint() {
        PopupButtonModel model = this.applicationMenuButton.getPopupModel();
        Icon icon;
        if (model.isPressed() || model.isPopupShowing()) {
            icon = down;
        } else if (model.isRollover()) {
            icon = over;
        } else {
            icon = normal;
        }
        return icon;
    }
}
