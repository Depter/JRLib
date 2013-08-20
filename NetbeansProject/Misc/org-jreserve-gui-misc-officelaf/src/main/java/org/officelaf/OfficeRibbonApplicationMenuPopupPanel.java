package org.officelaf;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.image.ColorConvertOp;
import org.pushingpixels.flamingo.api.common.AbstractCommandButton;
import org.pushingpixels.flamingo.api.common.CommandButtonDisplayState;
import org.pushingpixels.flamingo.api.common.CommandButtonLayoutManager;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonPanel;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.RolloverActionListener;
import org.pushingpixels.flamingo.api.common.icon.FilteredResizableIcon;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryFooter;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;
import org.pushingpixels.flamingo.internal.ui.ribbon.appmenu.CommandButtonLayoutManagerMenuTileLevel1;
import org.pushingpixels.flamingo.internal.ui.ribbon.appmenu.JRibbonApplicationMenuButton;
import org.pushingpixels.flamingo.internal.ui.ribbon.appmenu.JRibbonApplicationMenuPopupPanelSecondary;
import org.pushingpixels.flamingo.internal.utils.FlamingoUtilities;

public class OfficeRibbonApplicationMenuPopupPanel extends JPopupPanel {
    private static final Color BORDER_COLOR = new Color(0x434241);
    private static final Color BORDER_HIGHLIGHT1_COLOR = new Color(0x6d6c6c);
    private static final Color BORDER_HIGHLIGHT2_COLOR = new Color(0x6b6c71);
    private static final Color FOOTER_GRADIENT_1 = new Color(0x434652);
    private static final Color FOOTER_GRADIENT_2 = new Color(0x3b3e44);
    private static final Color FOOTER_GRADIENT_3 = new Color(0x2f2f2f);
    private static final Color FOOTER_GRADIENT_4 = new Color(0x404040);
    private static final Color FOOTER_HIGHLIGHT = new Color(0x4a4a4a);
    private static final Color TOP_HIGHLIGHT_GRADIENT_1 = new Color(0x636262);
    private static final Color TOP_HIGHLIGHT_GRADIENT_2 = new Color(0x676667);
    private static final Color MAIN_FILL = new Color(0x5a595a);

    private static final Color[] GRADIENTS = new Color[]{
            FOOTER_GRADIENT_1,
            FOOTER_GRADIENT_2,
            FOOTER_GRADIENT_3,
            FOOTER_GRADIENT_4
    };

    protected JCommandButtonPanel panelLevel1;
    protected JPanel panelLevel2;
    protected JPanel footerPanel;

    protected RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback defaultPrimaryCallback;

    protected static final CommandButtonDisplayState MENU_TILE_LEVEL_1 = new CommandButtonDisplayState(
            "Ribbon application menu tile level 1", 32) {
        @Override
        public CommandButtonLayoutManager createLayoutManager(
                AbstractCommandButton commandButton) {
            return new CommandButtonLayoutManagerMenuTileLevel1();
        }
    };

    protected static class LineBorder extends AbstractBorder {
        private Color outer;
        private Color inner;
        private Insets padding;

        public LineBorder(Color outer, Color inner) {
            this(outer, inner, new Insets(0, 0, 0, 0));
        }

        public LineBorder(Color outer, Color inner, Insets padding) {
            this.outer = outer;
            this.inner = inner;
            this.padding = padding;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, super.getBorderInsets(c));
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = 2 + padding.left;
            insets.top = 2 + padding.top;
            insets.right = 2 + padding.right;
            insets.bottom = 2 + padding.bottom;
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(outer);
            g.drawRect(x, y, width - 1, height - 1);
            g.setColor(inner);
            g.drawRect(x + 1, y + 1, width - 3, height - 3);
        }
    }

    public OfficeRibbonApplicationMenuPopupPanel(final JRibbonApplicationMenuButton button, RibbonApplicationMenu ribbonAppMenu) {
        this.setLayout(new BorderLayout());
        this.setBorder(new LineBorder(BORDER_COLOR, BORDER_HIGHLIGHT1_COLOR, new Insets(14, 2, 0, 2)) {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                super.paintBorder(c, g, x, y, width, height);

                // draw the application menu button
                JRibbonApplicationMenuButton rendererButton = new JRibbonApplicationMenuButton(button.getRibbon());
                rendererButton.setIcon(button.getIcon());
                rendererButton.getPopupModel().setRollover(false);
                rendererButton.getPopupModel().setPressed(true);
                rendererButton.getPopupModel().setArmed(true);
                rendererButton.getPopupModel().setPopupShowing(true);

                CellRendererPane buttonRendererPane = new CellRendererPane();
                Point buttonLoc = button.getLocationOnScreen();
                Point panelLoc = c.getLocationOnScreen();

                buttonRendererPane.setBounds(panelLoc.x - buttonLoc.x,
                        panelLoc.y - buttonLoc.y, button.getWidth(), button
                                .getHeight());
                buttonRendererPane.paintComponent(g, rendererButton,
                        (Container) c, -panelLoc.x + buttonLoc.x, -panelLoc.y
                                + buttonLoc.y, button.getWidth(), button
                                .getHeight(), true);
            }
        });

        this.defaultPrimaryCallback = new RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback() {
            @Override
            public void menuEntryActivated(JPanel targetPanel) {
                targetPanel.removeAll();
                targetPanel.revalidate();
                targetPanel.repaint();
            }
        };

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new LineBorder(BORDER_HIGHLIGHT2_COLOR, BORDER_COLOR));

        this.panelLevel1 = new JCommandButtonPanel(MENU_TILE_LEVEL_1);
        this.panelLevel1.setUI(new OfficeCommandButtonPanelUI() {
            @Override
            protected Insets getGroupInsets() {
                return new Insets(0, 0, 0, 0);
            }

            @Override
            protected int getLayoutGap() {
                return 0;
            }
        });
        this.panelLevel1.setBackground(new Color(250,250,250));
        this.panelLevel1.setMaxButtonColumns(1);
        this.panelLevel1.setBorder(BorderFactory.createEmptyBorder());

        this.panelLevel1.addButtonGroup("main");
        this.panelLevel1.setToShowGroupLabels(false);
        if (ribbonAppMenu != null) {
            for (final java.util.List<RibbonApplicationMenuEntryPrimary> entryList : ribbonAppMenu.getPrimaryEntries()) {
                for(final RibbonApplicationMenuEntryPrimary menuEntry : entryList) {
                    final JCommandMenuButton commandButton = new JCommandMenuButton(
                            menuEntry.getText(), menuEntry.getIcon());
                            commandButton.setCommandButtonKind(menuEntry.getEntryKind());
                            commandButton.addActionListener(menuEntry.getMainActionListener());
                    if (menuEntry.getSecondaryGroupCount() == 0) {
                        // if there are no secondary menu items, register the application
                        // rollover callback to populate the second level panel
                        commandButton.addRolloverActionListener(new RolloverActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                defaultPrimaryCallback.menuEntryActivated(panelLevel2);
                            }
                        });
                    } else {
                        // register a core callback to populate the second level panel with secondary menu items
                        final RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback coreCallback = new RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback() {
                            @Override
                            public void menuEntryActivated(JPanel targetPanel) {
                                targetPanel.removeAll();
                                targetPanel.setLayout(new BorderLayout());
                                JRibbonApplicationMenuPopupPanelSecondary secondary = new JRibbonApplicationMenuPopupPanelSecondary(menuEntry) {
                                    @Override
                                    public void removeNotify() {
                                        super.removeNotify();
                                        commandButton.getPopupModel().setPopupShowing(false);
                                    }
                                };
                                JScrollPane scrollPane = new JScrollPane(secondary, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                Dimension prefSize = secondary.getPreferredSize();
                                secondary.setPreferredSize(new Dimension(targetPanel.getPreferredSize().width - scrollPane.getVerticalScrollBar().getPreferredSize().width, prefSize.height));
                                targetPanel.add(scrollPane, BorderLayout.CENTER);
                                targetPanel.revalidate();
                            }
                        };

                        commandButton.addRolloverActionListener(
                            new RolloverActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    coreCallback.menuEntryActivated(panelLevel2);
                                    // emulate showing the popup so the button remains "selected"
                                    commandButton.getPopupModel().setPopupShowing(true);
                                }
                        });
                    }
                    commandButton.setDisplayState(MENU_TILE_LEVEL_1);
                    commandButton.setHorizontalAlignment(SwingUtilities.LEADING);
                    commandButton.setPopupOrientationKind(JCommandButton.CommandButtonPopupOrientationKind.SIDEWARD);
                    commandButton.setEnabled(menuEntry.isEnabled());
                    this.panelLevel1.addButtonToLastGroup(commandButton);
                }
            }
        }

        mainPanel.add(this.panelLevel1, BorderLayout.WEST);

        this.panelLevel2 = new JPanel();
        this.panelLevel2.setBorder(new Border() {
            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(0, 1, 0, 0);
            }

            @Override
            public boolean isBorderOpaque() {
                return true;
            }

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                //g.setColor(FlamingoUtilities.getColor(Color.gray, "Label.disabledForeground"));
                g.setColor(new Color(197,197,197));
                g.drawLine(x, y, x, y + height);
            }
        });
        this.panelLevel2.setPreferredSize(new Dimension(30 * FlamingoUtilities
                .getFont(this.panelLevel1, "Ribbon.font", "Button.font",
                        "Panel.font").getSize() - 30, 10));

        mainPanel.add(this.panelLevel2, BorderLayout.CENTER);

        this.add(mainPanel, BorderLayout.CENTER);

        GridBagLayout gridbag = new GridBagLayout();
        this.footerPanel = new JPanel(gridbag) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new LinearGradientPaint(0, 0, 0, getHeight() - 3, new float[]{0, 0.45f, 0.46f, 1}, GRADIENTS));
                g2d.fillRect(0, 0, getWidth(), getHeight() - 2);
                g2d.setColor(FOOTER_HIGHLIGHT);
                g2d.fillRect(0, getHeight() - 2, getWidth(), 2);
            }

        };
        this.footerPanel.setBorder(new EmptyBorder(2, 0, 1, 1));


        GridBagConstraints c = new GridBagConstraints();
        Insets buttonSpacing = new Insets(0, 6, 0, 0);

        // create a horizontal strut to right align the buttons
        c.weightx = 1.0;
        this.footerPanel.add(new JLabel(" "), c);
        c.weightx = 0;

        if (ribbonAppMenu != null) {
            for (int i = 0; i < ribbonAppMenu.getFooterEntries().size(); i++) {
                RibbonApplicationMenuEntryFooter footerEntry = ribbonAppMenu.getFooterEntries().get(i);
                JCommandButton commandFooterButton = new JCommandButton(
                        footerEntry.getText(), footerEntry.getIcon());
                commandFooterButton.setDisabledIcon(new FilteredResizableIcon(
                        footerEntry.getIcon(), new ColorConvertOp(ColorSpace
                                .getInstance(ColorSpace.CS_GRAY), null)));
                commandFooterButton.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_ONLY);
                commandFooterButton.addActionListener(footerEntry.getMainActionListener());
                commandFooterButton
                        .setDisplayState(CommandButtonDisplayState.MEDIUM);
                commandFooterButton.setFlat(false);
                commandFooterButton.setEnabled(footerEntry.isEnabled());
                c.gridx = i + 1;
                if (i > 0) {
                    c.insets = buttonSpacing;
                }
                this.footerPanel.add(commandFooterButton, c);
            }
        }

        this.add(this.footerPanel, BorderLayout.SOUTH);
    }



    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        // Draw top gradient
        g2d.setPaint(new LinearGradientPaint(0, 0, 0, 6, new float[]{0, 1}, new Color[]{
                TOP_HIGHLIGHT_GRADIENT_1,
                TOP_HIGHLIGHT_GRADIENT_2
        }));
        g2d.fillRect(0, 0, width, 6);

        // Fill remainder
        g2d.setPaint(new LinearGradientPaint(0, 8, 0, height - 3, new float[]{0, 1}, new Color[]{
                MAIN_FILL,
                FOOTER_GRADIENT_1,
        }));
        g2d.fillRect(0, 6, width, height - 6);
    }
}
