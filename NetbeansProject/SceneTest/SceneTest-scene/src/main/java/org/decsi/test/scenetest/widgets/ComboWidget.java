/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 */
public class ComboWidget extends Widget {

    private ActionSupport as = new ActionSupport(this);
    private ComboBoxModel model;
    private TextBridge bridge;
    private LabelWidget labelWidget;
    private ComboButton button;
    
    public ComboWidget(JRLibScene scene, ComboBoxModel model, TextBridge bridge) {
        super(scene);
        this.model = model;
        this.bridge = bridge;
        
        model.addListDataListener(new ModelListener());
//        ButtonListener listener = new ButtonListener();
        setLayout(LayoutFactory.createHorizontalFlowLayout (LayoutFactory.SerialAlignment.CENTER, 0));
        labelWidget = new LabelWidget(scene, bridge.toString(model.getSelectedItem()));
        labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
        labelWidget.setBorder(new ComboLabelBorder(labelWidget));
        //labelWidget.getActions().addAction(ActionFactory.createInplaceEditorAction(new EditProvider()));
        labelWidget.setBackground(scene.getUI().getTextEnabledBg());
        labelWidget.setOpaque(true);
        super.addChild(labelWidget, 1);
        
        button = new ComboButton(scene);
        button.addActionListener(new ButtonListener());
        super.addChild(button, 0);
        
        int aw = ComboButtonBorder.ARCH_WIDTH;
        setMinimumSize(new Dimension(aw, aw));
        Dimension ld = labelWidget.getPreferredSize();
        if(ld == null)
            labelWidget.setPreferredSize(new Dimension(100, aw));
        else {
            labelWidget.setPreferredSize(new Dimension(ld.width, aw));
            button.setPreferredSize(new Dimension(aw, ld.height));
        }
    }
    
    
    public void addActionListener(ActionListener listener) {
        as.addActionListener(listener);
    }
    
    public void removeActionListener(ActionListener listener) {
        as.removeActionListener(listener);
    }

    public String getActionCommand() {
        return as.getActionCommand();
    }

    public void setActionCommand(String actionCommand) {
        as.setActionCommand(actionCommand);
    }
    
    private void fireAction() {
        as.fireAction();
    }
    
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
    
    private class ModelListener implements ListDataListener {

        @Override
        public void intervalAdded(ListDataEvent e) {
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            Object o = model.getSelectedItem();
            labelWidget.setLabel(bridge.toString(o));
            fireAction();
        }
    }
    
    private static class ComboButton extends Widget implements SelectProvider {
    
        private ActionSupport as = new ActionSupport(this);
        
        private ComboButton(JRLibScene scene) {
            super(scene);
            setOpaque(false);
            setBorder(new ComboButtonBorder(this));
            getActions().addAction(scene.createWidgetHoverAction());
            getActions().addAction(ActionFactory.createSelectAction(this));
            setMinimumSize(new Dimension(ComboButtonBorder.ARCH_WIDTH, ComboButtonBorder.ARCH_WIDTH));
        }

        @Override
        protected void notifyStateChanged(ObjectState previous, ObjectState current) {
            if(previous.isHovered() != current.isHovered())
                repaint();
        }

        @Override
        public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return widget.isEnabled();
        }

        void addActionListener(ActionListener listener) {
            as.addActionListener(listener);
        }

        void removeActionListener(ActionListener listener) {
            as.removeActionListener(listener);
        }
        
        @Override
        public void select(Widget widget, Point localLocation, boolean invertSelection) {
            as.fireAction();
        }
    }
    
    private static class ComboButtonBorder implements Border {
        
        private final static int ARCH_WIDTH = 16;
        private final static double ARROW_MARGIN = 0.2;
        private final static Insets INSETS = new Insets(0, 0, 0, 0);
        
        private final Widget widget;

        private ComboButtonBorder(Widget widget) {
            this.widget = widget;
        }
        
        @Override
        public Insets getInsets() {
            return INSETS;
        }

        @Override
        public boolean isOpaque() {
            return true;
        }

        @Override
        public void paint(Graphics2D gr, Rectangle bounds) {
            Shape border = getBorderShape(bounds);
            if(widget.isOpaque()) {
                gr.setPaint(widget.getBackground());
                gr.fill(border);
            }
            
            gr.setPaint(getForeground());
            gr.draw(border);
            gr.fill(getArrowShape(bounds));
        }
        
            
        Shape getBorderShape(Rectangle bounds) {
            if(bounds.width < ARCH_WIDTH)
                return bounds;
            return createBorderShape(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        
        Shape createBorderShape(int x, int y, int w, int h) {
            Path2D path = new Path2D.Float();
            path.moveTo(x+w-ARCH_WIDTH, y);
            path.quadTo(x+w, y, x+w, y+h/2);
            path.quadTo(x+w, y+h, x+w-ARCH_WIDTH, y+h);
            path.lineTo(x, y+h);
            path.lineTo(x, y);
            path.closePath();
            return path;
        }

        private Color getForeground() {
            JRLibWidgetUI ui = ((JRLibScene)widget.getScene()).getUI();
            if(widget.isEnabled()) {
                if(widget.getState().isHovered())
                    return ui.getControlBorderHoverColor();
                return ui.getControlBorderColor();
            } else {
                return ui.getControlBorderDisabledColor();
            }
        }
        
        Shape getArrowShape(Rectangle bounds) {
            double x = bounds.x;
            double y = bounds.y;
            double w = bounds.width;
            double h = bounds.height;
            double mH = h * ARROW_MARGIN;
            double mW = w * ARROW_MARGIN;
                    
            Path2D path = new Path2D.Double();
            path.moveTo(x+mW, y+mH);
            //path.lineTo(x+mW, y+h-mH);
            //path.lineTo(x+w-mW, y+w/2d);
            
            path.moveTo(x+w-mW, y+mH);
            path.moveTo(x+w/2d, y+h-mH);
            
            path.closePath();
            return path;
        }
    }
    
    private static class ComboLabelBorder implements Border {
        private final static Insets INSETS = new Insets(1, 0, 1, 0);
        
        private final Widget widget;

        private ComboLabelBorder(Widget widget) {
            this.widget = widget;
        }
        
        @Override
        public Insets getInsets() {
            return INSETS;
        }

        @Override
        public boolean isOpaque() {
            return true;
        }

        @Override
        public void paint(Graphics2D gr, Rectangle bounds) {
            JRLibWidgetUI ui = ((JRLibScene)widget.getScene()).getUI();
            if(widget.isEnabled())
                gr.setColor(ui.getControlBorderColor());
            else
                gr.setColor(ui.getControlBorderDisabledColor());
            gr.drawLine(bounds.x             , bounds.y, 
                        bounds.x+bounds.width, bounds.y);
            gr.drawLine(bounds.x             , bounds.y+bounds.height, 
                        bounds.x+bounds.width, bounds.y+bounds.height);
        }
    }
    
}
