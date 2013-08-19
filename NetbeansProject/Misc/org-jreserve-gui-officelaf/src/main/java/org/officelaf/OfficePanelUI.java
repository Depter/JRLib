package org.officelaf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

/**
 * @author mikael
 */
public class OfficePanelUI extends BasicPanelUI {
    private enum Type {
        MAIN_BG,
        TRANSPARENT,
        SOLID,
        DEFAULT
    }

    private static final Color GRAY_10 = new Color(10, 10, 10);
    public static final Color GRAY_76 = new Color(76, 76, 76);

    private static final OfficePanelUI DEFAULT = new OfficePanelUI();
    private static final OfficePanelUI TRANSPARENT = new OfficePanelUI(Type.TRANSPARENT);
    private static final OfficePanelUI SOLID = new OfficePanelUI(Type.SOLID);

    private static Boolean mainBgIsSet = false;

    private Type type = Type.DEFAULT;

    public OfficePanelUI(Type type) {
        this.type = type;
    }

    public OfficePanelUI() {
    }

    public static ComponentUI createUI(final JComponent c) {
        ComponentUI retVal = DEFAULT;

        String name = c.getClass().getName();
        if (name.compareTo("org.netbeans.core.windows.view.ui.MultiSplitPane") == 0 ||
                name.compareTo("org.netbeans.core.windows.view.EditorView$EditorAreaComponent") == 0 ||
                name.compareTo("org.netbeans.core.windows.view.ui.slides.SlideBarContainer$VisualPanel") == 0) {
            if (!mainBgIsSet) {
                c.addHierarchyListener(new HierarchyListener() {
                    public void hierarchyChanged(HierarchyEvent e) {
                        if (c.getParent() != null && c.getParent() instanceof JPanel && !mainBgIsSet) {
                            ((JPanel) c.getParent()).setUI(new OfficePanelUI(Type.MAIN_BG));
                            mainBgIsSet = true;
                        }
                    }
                });
            }
            //retVal = name.compareTo("org.netbeans.core.windows.view.EditorView$EditorAreaComponent") == 0 ? SOLID : TRANSPARENT;
            retVal = SOLID;
        }

        /*
          MultiSplitPane
          org.netbeans.core.windows.view.EditorView$EditorAreaComponent
          org.netbeans.core.windows.view.ui.MainWindow$1
          org.netbeans.core.windows.view.ui.DefaultSplitContainer$ModePanel
          org.netbeans.core.windows.view.ui.slides.ResizeGestureRecognizer$GlassPane
          org.netbeans.core.windows.view.ui.slides.SlideBarContainer$VisualPanel
        */

        return retVal;
    }

    protected void installDefaults(JPanel p) {
        super.installDefaults(p);

        if (type == Type.TRANSPARENT) {
            p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            p.setOpaque(false);
        } else if (type == Type.SOLID) {
            p.setBackground(GRAY_76);
            p.setOpaque(true);
        }
    }

    public void update(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        switch (type) {
            case MAIN_BG:
                g2d.setColor(GRAY_76);
//                g2d.setPaint(new LinearGradientPaint(0, 0, 0, c.getHeight(), new float[] {0, 1}, new Color[] {GRAY_76, GRAY_10}));
                g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
                break;

            case TRANSPARENT:
                break;

            default:
                if (c.isOpaque()) {
                    g2d.setColor(c.getBackground());
                    g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
                }
        }
        paint(g, c);
    }
}
