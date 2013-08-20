package org.officelaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import org.pushingpixels.flamingo.internal.ui.ribbon.BasicRibbonUI;

public class OfficeRibbonUI extends BasicRibbonUI {
    protected static final Color background = new Color(0x535353);
    protected static final Color taskAreaBg1 = new Color(0xd7dadf);
    protected static final Color taskAreaBg2 = new Color(0xc1c6cf);
    protected static final Color taskAreaBg3 = new Color(0xb4bbc5);
    protected static final Color taskAreaBg4 = new Color(0xe7f0f1);
    protected static final Color gray = new Color(0xbebebe);
    protected static final Color gray_0 = new Color(0xbe, 0xbe, 0xbe, 0);
    protected static final Color gray2 = new Color(0xebebeb);
    protected static final Color gray3 = new Color(0xd3d3d3);
    protected static final Color gray_110 = new Color(0xbe, 0xbe, 0xbe, 110);
    protected static final Color black_69 = new Color(0, 0, 0, 69);
    protected static final Color black_48 = new Color(0, 0, 0, 48);
    protected static final Color black_0 = new Color(0, 0, 0, 0);

    protected BufferedImage taskBackgroundCache;

    /**
     * The space between the tab buttons in the task bar.
     */
    protected int tabSpacing = 1;


    /*
     * (non-Javadoc)
     *
     * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
     */
    public static ComponentUI createUI(JComponent c) {
        return new OfficeRibbonUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        this.ribbon.setOpaque(Boolean.FALSE);
    }

    @Override
    protected void installComponents() {
        super.installComponents();
        this.ribbon.remove(this.taskBarPanel);
        this.taskBarPanel = new TaskbarPanel();
        this.taskBarPanel.setName("JRibbon Task Bar");
        this.taskBarPanel.setLayout(createTaskbarLayoutManager());
        this.ribbon.add(this.taskBarPanel);

        this.bandScrollablePanel.setOpaque(Boolean.FALSE);
        for (Component component : this.bandScrollablePanel.getComponents()) {
            if (component instanceof JScrollPane) {
                ((JScrollPane) component).setOpaque(false);
                ((JScrollPane) component).getViewport().setOpaque(false);
            }
        }

        this.taskToggleButtonsScrollablePanel.setOpaque(Boolean.FALSE);
        for (Component component : this.taskToggleButtonsScrollablePanel.getComponents()) {
            if (component instanceof JScrollPane) {
                ((JScrollPane) component).setOpaque(false);
                ((JScrollPane) component).getViewport().setOpaque(false);
            }
        }
    }

    @Override
    protected LayoutManager createTaskbarLayoutManager() {
        return new TaskbarLayout();
    }

    @Override
    protected BandHostPanel createBandHostPanel() {
        BandHostPanel panel = super.createBandHostPanel();
        panel.setOpaque(Boolean.FALSE);
        return panel;
    }

    @Override
    protected TaskToggleButtonsHostPanel createTaskToggleButtonsHostPanel() {
        BasicRibbonUI.TaskToggleButtonsHostPanel panel = super.createTaskToggleButtonsHostPanel();
        panel.setOpaque(Boolean.FALSE);
        return panel;
    }

    @Override
    public int getTaskbarHeight() {
        return 28;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        this.paintBackground(g);

        Insets ins = c.getInsets();
        int extraHeight = getTaskToggleButtonHeight();
        if (!this.isUsingTitlePane())
            extraHeight += getTaskbarHeight();
        this.paintTaskArea(g, 0, ins.top + extraHeight-1, c.getWidth(), c
                .getHeight()
                - extraHeight - ins.top);
    }

    @Override
    protected void paintBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(background);
        int y = getTaskbarHeight() + ribbon.getInsets().top - 1;
        g2d.fillRect(0, y, ribbon.getWidth(), ribbon.getHeight()-y);
    }

    @Override
    protected void paintTaskArea(Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;

        if(taskBackgroundCache == null || width != taskBackgroundCache.getWidth() || height != taskBackgroundCache.getHeight()) {
            taskBackgroundCache = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);

            Graphics2D ig = taskBackgroundCache.createGraphics();

            int r = width - 1;
            int b = height - 1;
            int s = 3; // shadow size

            b -= s; // make space for the shadow

            // Paint the top background gradient
            ig.setPaint(new LinearGradientPaint(0, 1, 0, 17, new float[] {0, 1}, new Color[] {taskAreaBg1, taskAreaBg2}));
            ig.fillRect(1, 1, width-2, 16);

            // Paint the bottom background gradient
            ig.setPaint(new LinearGradientPaint(0, 17, 0, b, new float[] {0, 1}, new Color[] {taskAreaBg3, taskAreaBg4}));
            ig.fillRect(1, 17, width-2, height-18-s);

            // Paint the border
            float[] fractions = {0, 1};
            Color[] borderColors = {gray_0, gray};
            ig.setPaint(new LinearGradientPaint(0, 0, 4, 0, fractions, borderColors));
            ig.drawLine(0, 0, r-5, 0); // top line with fadeout at the left
            ig.drawLine(0, b, r-5, b); // bottom line with fadeout at the left
            ig.setPaint(new LinearGradientPaint(r, 0, r-4, 0, fractions, borderColors));
            ig.drawLine(r-4, 0, r, 0); // top line right fadeout
            ig.drawLine(r-4, b, r, b); // bottom line right fadeout
            ig.setPaint(new LinearGradientPaint(0, 0, 0, 4, fractions, borderColors));
            ig.drawLine(0, 0, 0, b-5); // left line with fadeout at the top
            ig.drawLine(r, 0, r, b-5); // right line with fadeout at the top
            ig.setPaint(new LinearGradientPaint(0, b, 0, b-4, fractions, borderColors));
            ig.drawLine(0, b-4, 0, b); // left line bottom fadeout
            ig.drawLine(r, b-4, r, b); // right line bottom fadeout

            // Paint the bottom inner shadow
            ig.setColor(gray2);
            ig.drawLine(1, b-2, r-1, b-2);
            ig.setColor(gray3);
            ig.drawLine(2, b-1, r-2, b-1);

            // Paint the corners of the border
            ig.setColor(gray);
            ig.drawLine(1, 1, 1, 1); // tl
            ig.drawLine(1, b-1, 1, b-1); // bl
            ig.drawLine(r-1, 1, r-1, 1); // tr
            ig.drawLine(r-1, b-1, r-1, b-1); // br
            ig.setColor(gray_110);
            ig.drawLine(1, 2, 1, 2); // tl
            ig.drawLine(2, 1, 2, 1);
            ig.drawLine(r-1, 2, r-1, 2); // tr
            ig.drawLine(r-2, 1, r-2, 1);
            ig.drawLine(r-1, b-2, r-1, b-2); // br
            ig.drawLine(r-2, b-1, r-2, b-1);
            ig.drawLine(1, b-2, 1, b-2); // bl
            ig.drawLine(2, b-1, 2, b-1);

            // Paint the bottom shadow
            b += s;
            ig.setPaint(new LinearGradientPaint(0, b-2, 4, b-2, fractions, new Color[] {black_0, black_69}));
            ig.drawLine(0, b-2, r-5, b-2);
            ig.setPaint(new LinearGradientPaint(r, b-2, r-4, b-2, fractions, new Color[] {black_0, black_69}));
            ig.drawLine(r-4, b-2, r, b-2);
            ig.setPaint(new LinearGradientPaint(0, b-1, 4, b-1, fractions, new Color[] {black_0, black_48}));
            ig.drawLine(0, b-1, r-5, b-1);
            ig.setPaint(new LinearGradientPaint(r, b-1, r-4, b-1, fractions, new Color[] {black_0, black_48}));
            ig.drawLine(r-4, b-1, r, b-1);

            ig.dispose();
        }

        g2d.drawImage(taskBackgroundCache, x, y, null);
    }

    private class TaskbarPanel extends JPanel {
        private final Image bgLeft;
        private final Image bg;
        private final Image bgRight;

        /**
         * Creates the new taskbar panel.
         */
        public TaskbarPanel() {
            super();
            this.setOpaque(false);
            this.setBorder(new EmptyBorder(1, 0, 1, 0));

            Toolkit toolkit = Toolkit.getDefaultToolkit();
            bg      = toolkit.getImage(getClass().getResource("images/toolbar_bg.png"));
            bgLeft  = toolkit.getImage(getClass().getResource("images/toolbar_bg_left.png"));
            bgRight = toolkit.getImage(getClass().getResource("images/toolbar_bg_right.png"));
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        @Override
        protected void paintComponent(Graphics g) {
            if (this.getComponentCount() == 0) {
                return;
            }

            int minX = this.getWidth();
            int maxX = 0;
            for (int i = 0; i < this.getComponentCount(); i++) {
                Component taskBarComp = this.getComponent(i);
                minX = Math.min(minX, taskBarComp.getX());
                maxX = Math.max(maxX, taskBarComp.getX()
                        + taskBarComp.getWidth());
            }

            int lw = bgLeft.getWidth(this);
            int h = bg.getHeight(this);

            g.drawImage(bgLeft, minX-lw, 0, this);
            g.drawImage(bgRight, maxX, 0, this);
            g.drawImage(bg, minX, 0, maxX-minX, h, this);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension result = super.getPreferredSize();
            return new Dimension(result.width + result.height / 2,
                    result.height);
        }
    }


    private class TaskbarLayout implements LayoutManager {
        /*
         * (non-Javadoc)
         *
         * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
         * java.awt.Component)
         */
        @Override
        public void addLayoutComponent(String name, Component c) {
        }

        /*
         * (non-Javadoc)
         *
         * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
         */
        @Override
        public void removeLayoutComponent(Component c) {
        }

        /*
         * (non-Javadoc)
         *
         * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
         */
        @Override
        public Dimension preferredLayoutSize(Container c) {
            Insets ins = c.getInsets();
            int pw = 0;
            int gap = getBandGap();
            for (Component regComp : ribbon.getTaskbarComponents()) {
                pw += regComp.getPreferredSize().width;
                pw += gap;
            }
            return new Dimension(pw + ins.left + ins.right, getTaskbarHeight()
                    /*+ ins.top + ins.bottom*/);
        }

        /*
         * (non-Javadoc)
         *
         * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
         */
        @Override
        public Dimension minimumLayoutSize(Container c) {
            return this.preferredLayoutSize(c);
        }

        /*
         * (non-Javadoc)
         *
         * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
         */
        @Override
        public void layoutContainer(Container c) {
            Insets ins = c.getInsets();
            int gap = getBandGap();

            int x = ins.left + 1 + applicationMenuButton.getX()
                    + applicationMenuButton.getWidth();

            for (Component regComp : ribbon.getTaskbarComponents()) {
                int pw = regComp.getPreferredSize().width;
                regComp.setBounds(x, 2, pw, c.getHeight() - ins.top
                        - ins.bottom - 4);
                x += (pw + gap);
            }
        }
    }
}
