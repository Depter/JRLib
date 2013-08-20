package org.officelaf;

import org.openide.awt.StatusDisplayer;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Iterator;

/**
 * User: mikael
 * Date: Sep 4, 2006
 * Time: 2:30:30 PM
 *
 * Some code borrowed from MainWindow and StatusLine
 *
 */
public class StatusBar extends JPanel implements ChangeListener, Runnable {

    public static final Color GRAY_17    = new Color(17, 17, 17);
    public static final Color GRAY_72    = new Color(72, 72, 72);
    public static final Color GRAY_124   = new Color(124, 124, 124);
    public static final Color GRAY_13    = new Color(13, 13, 13);
    public static final Color GRAY_47    = new Color(47, 48, 48);
    public static final Color GRAY_69    = new Color(69, 69, 69);
    public static final Color GRAY_76    = new Color(76, 76, 76);
    public static final Color TOPLINE    = new Color(97, 106, 118);
    public static final Color TOP_67     = new Color(67, 71, 82);
    public static final Color TOP_62     = new Color(62, 65, 75);
    public static final Color TOP_60     = new Color(60, 64, 74);
    public static final Color MIDDLE_47  = new Color(47, 48, 48);
    public static final Color MIDDLE_60  = new Color(60, 60, 60);
    public static final Color DRAG_LIGHT = new Color(221, 224, 227);
    public static final Color DRAG_DARK  = new Color(145, 153, 164);

    private static int SURVIVING_TIME = Integer.getInteger("org.openide.awt.StatusDisplayer.DISPLAY_TIME", 5000);

    private static Lookup.Result<StatusLineElementProvider> result;
    private static JPanel innerIconsPanel;

    protected BufferedImage dragImage;
    protected BufferedImage verticalImage;
    protected Object clearing;
    private StatusDisplayer d = StatusDisplayer.getDefault();
    private JLabel displayer;

    public StatusBar() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 12));
        setPreferredSize(new Dimension(100, 20));
        setBackground(GRAY_47);
        setName("statusLine");

//        JLabel ready = new JLabel("Ready ");
//        ready.setForeground(Color.WHITE);

        displayer = new JLabel();
        displayer.setForeground(Color.WHITE);
//        displayer.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        add(displayer, BorderLayout.CENTER);
//        add(ready, BorderLayout.WEST);
        decoratePanel(this);
    }

    public void addNotify() {
        super.addNotify();
        run();
        d.addChangeListener(this);
    }

    public void removeNotify() {
        super.removeNotify();
        d.removeChangeListener(this);
    }

    public void stateChanged(ChangeEvent e) {
        if (SwingUtilities.isEventDispatchThread()) {
            run();
        } else {
            SwingUtilities.invokeLater(this);
        }
    }

    /** Called in event queue, should update the status text.
     */
    public void run() {
        String currentMsg = d.getStatusText();
        setForeground(UIManager.getColor("Label.foreground"));
        displayer.setText(currentMsg);
        if (SURVIVING_TIME != 0) {
            Object token = new Object();
            clearing = token;
            if (!"".equals(currentMsg)) {
                new Updater(token).schedule();
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(100, super.getPreferredSize().height);
    }

    public Dimension getMinimumSize() {
        return new Dimension(0, super.getMinimumSize().height);
    }

    static JPanel getStatusLineElements(JPanel panel) {
        if (result == null) {
            result = Lookup.getDefault().lookup(
                    new Lookup.Template<StatusLineElementProvider>(StatusLineElementProvider.class));
            result.addLookupListener(new StatusLineElementsListener(panel));
        }

        Collection<? extends StatusLineElementProvider> c = result.allInstances();
        if (c == null || c.isEmpty()) {
            return null;
        }

        JPanel icons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        icons.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 2));
        //icons.setBorder(BorderFactory.createLineBorder(Color.RED));
        icons.setOpaque(false);

        boolean some = false;
        Iterator<? extends StatusLineElementProvider> it = c.iterator();
        while (it.hasNext()) {
            StatusLineElementProvider o = it.next();
            Component comp = o.getStatusLineElement();
            if(comp != null) {
                some = true;
                if(comp instanceof JComponent) {
                    ((JComponent) comp).setOpaque(false);
                }

                setForeground(comp);
                icons.add(comp);
            }
        }
        
        return some ? icons : null;
    }

    protected static void setForeground(Component comp) {
        if(comp != null) comp.setForeground(Color.WHITE);
        if(comp instanceof Container) {
            for (Component child : ((Container)comp).getComponents()) {
                setForeground(child);
            }
        }
    }

    static private class StatusLineElementsListener implements LookupListener {

        private JPanel decoratingPanel;

        StatusLineElementsListener(JPanel decoratingPanel) {
            this.decoratingPanel = decoratingPanel;
        }

        public void resultChanged(LookupEvent ev) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    decoratePanel(decoratingPanel);
                }
            });
        }
    }

    private static void decoratePanel(JPanel panel) {
        if (innerIconsPanel != null) {
            panel.remove(innerIconsPanel);
        }

        innerIconsPanel = getStatusLineElements(panel);
        if (innerIconsPanel != null) {
            panel.add(innerIconsPanel, BorderLayout.EAST);
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintChildren(g);
        Graphics2D g2d = (Graphics2D) g;

        int w = getWidth() - 1;

        g2d.setColor(TOPLINE);
        g2d.drawLine(0, 0, w, 0);

        g2d.setPaint(new GradientPaint(0, 1, TOP_67, 0, 7, TOP_60));
        g2d.fillRect(0, 1, getWidth(), 7);
        g2d.setPaint(new GradientPaint(0, 8, MIDDLE_47, 0, 20, MIDDLE_60));
        g2d.fillRect(0, 8, getWidth(), 12);

        if (dragImage == null) {
            dragImage = createDragButton(g2d);
        }

        g2d.drawImage(dragImage, getWidth() - 11, getHeight() - 11, null);

//        if (verticalImage == null) {
//            verticalImage = createBar(g2d);
//        }
//
//        g2d.drawImage(verticalImage, 48, 2, null);
    }

    protected BufferedImage createDragButton(Graphics2D g2d) {
        BufferedImage retVal = g2d.getDeviceConfiguration().createCompatibleImage(11, 11, Transparency.BITMASK);
        g2d = retVal.createGraphics();

        g2d.setColor(DRAG_LIGHT);
        g2d.drawRect(9, 9, 1, 1);
        g2d.drawRect(5, 9, 1, 1);
        g2d.drawRect(1, 9, 1, 1);

        g2d.drawRect(9, 5, 1, 1);
        g2d.drawRect(5, 5, 1, 1);

        g2d.drawRect(9, 1, 1, 1);

        g2d.setColor(DRAG_DARK);
        g2d.drawRect(8, 8, 1, 1);
        g2d.drawRect(4, 8, 1, 1);
        g2d.drawRect(0, 8, 1, 1);

        g2d.drawRect(8, 4, 1, 1);
        g2d.drawRect(4, 4, 1, 1);

        g2d.drawRect(8, 0, 1, 1);

        return retVal;
    }

    protected BufferedImage createBar(Graphics2D g2d) {
        BufferedImage retVal = g2d.getDeviceConfiguration().createCompatibleImage(2, 20);
        g2d = retVal.createGraphics();

        g2d.setPaint(new LinearGradientPaint(0f, 0f, 0f, 19f, new float[]{.0f, .55f, 1.0f}, new Color[]{GRAY_76, GRAY_17, GRAY_72}));
        g2d.drawLine(0, 0, 0, 19);
        g2d.setPaint(new LinearGradientPaint(1f, 0f, 1f, 19f, new float[]{.0f, .55f, 1.0f}, new Color[]{Color.BLACK, GRAY_124, GRAY_13}));
        g2d.drawLine(1, 0, 1, 19);

        return retVal;
    }

    public void updateUI() {
        super.updateUI();
        Font f = UIManager.getFont("controlFont");
        if (f == null) {
            f = UIManager.getFont("Tree.font");
        }

        if (f != null) {
            setFont(f);
        }
    }


    
    private class Updater implements ActionListener {

        private Object token;
        private long startTime;
        private Timer controller;

        public Updater(Object token) {
            this.token = token;
        }

        public void schedule() {
            controller = new Timer(SURVIVING_TIME, this);
            controller.setDelay(100);
            controller.start();
        }

        public void actionPerformed(ActionEvent arg0) {
            if (clearing == token) {
                long t = System.currentTimeMillis();
                if (startTime != 0L) {
                    Color c = Color.WHITE;
                    if (c != null) {
                        int alpha = 256 * (int) (t - startTime) / 2000;
                        final Color color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 255 - Math.min(255, alpha));
                        StatusBar.this.displayer.setForeground(color);
                    }
                } else {
                    startTime = t;
                }

                if (t > startTime + 2000) {
                    controller.stop();
                }
            } else {
                controller.stop();
            }
        }
    };
}
