package org.decsi.test.scenetest.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ChangeSupport;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SpinnerWidget extends Widget {
    
    private SpinnerModel model;
    private TextBridge bridge;
    private SpinnerButton prevButton;
    private SpinnerButton nextButton;
    private LabelWidget labelWidget;
    private ChangeSupport cs = new ChangeSupport(this);
    
    public SpinnerWidget(JRLibScene scene, SpinnerModel model, TextBridge bridge) {
        super(scene);
        this.model = model;
        this.bridge = bridge;
        
        model.addChangeListener(new ModelListener());
        ButtonListener listener = new ButtonListener();
        setLayout(LayoutFactory.createHorizontalFlowLayout (LayoutFactory.SerialAlignment.CENTER, 0));
        prevButton = new SpinnerButton(scene, SpinnerButtonBorder.ArrowType.LEFT);
        prevButton.addActionListener(listener);
        super.addChild(prevButton, 0);
        
        labelWidget = new LabelWidget(scene, bridge.toString(model.getValue()));
        labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
        labelWidget.setBorder(new SpinnerLabelBorder(labelWidget));
        labelWidget.getActions().addAction(ActionFactory.createInplaceEditorAction(new EditProvider()));
        labelWidget.setBackground(scene.getUI().getTextEnabledBg());
        labelWidget.setOpaque(true);
        super.addChild(labelWidget, 1);
        
        nextButton = new SpinnerButton(scene, SpinnerButtonBorder.ArrowType.RIGHT);
        nextButton.addActionListener(listener);
        super.addChild(nextButton, 0);
        
        int aw = SpinnerButtonBorder.ARCH_WIDTH;
        setMinimumSize(new Dimension(aw*2, aw));
        Dimension ld = labelWidget.getPreferredSize();
        if(ld == null)
            labelWidget.setPreferredSize(new Dimension(100, aw));
        else {
            labelWidget.setPreferredSize(new Dimension(ld.width, aw));
            prevButton.setPreferredSize(new Dimension(aw, ld.height));
            nextButton.setPreferredSize(new Dimension(aw, ld.height));
        }
    }
    
    public void setSpinnerEnabled(boolean enabled) {
        prevButton.setEnabled(enabled);
        nextButton.setEnabled(enabled);
        labelWidget.setEnabled(enabled);
        
        JRLibWidgetUI ui = ((JRLibScene)getScene()).getUI();
        labelWidget.setForeground(enabled? ui.getControlForeground() : ui.getControlDisabledForeground());
        labelWidget.setBackground(enabled? ui.getTextEnabledBg() : ui.getTextDisabledBg());
        
        super.setEnabled(enabled);
        repaint();
    }
    
    public boolean isSpinnerEnabled() {
        return super.isEnabled();
    }
    
    public void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }
    
    private void fireChange() {
        cs.fireChange();
    }
    
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object value = (prevButton == e.getSource())?
                model.getPreviousValue() : model.getNextValue();
            if(value != null)
                model.setValue(value);
        }
    }
    
    private class ModelListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            labelWidget.setLabel(bridge.toString(model.getValue()));
            fireChange();
        }
    }
    
    private class EditProvider implements TextFieldInplaceEditor {

        @Override
        public boolean isEnabled(Widget widget) {
            return SpinnerWidget.this.isEnabled();
        }

        @Override
        public String getText(Widget widget) {
            return labelWidget.getLabel();
        }

        @Override
        public void setText(Widget widget, String text) {
            try {
                Object value = bridge.toValue(text);
                model.setValue(value);
            } catch (IllegalArgumentException ex) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
    
    private static class SpinnerButton extends Widget implements SelectProvider {
    
        private ActionSupport as = new ActionSupport(this);
        
        private SpinnerButton(JRLibScene scene, SpinnerButtonBorder.ArrowType type) {
            super(scene);
            setOpaque(false);
            setBorder(new SpinnerButtonBorder(this, type));
            getActions().addAction(scene.createWidgetHoverAction());
            getActions().addAction(ActionFactory.createSelectAction(this));
            setMinimumSize(new Dimension(SpinnerButtonBorder.ARCH_WIDTH, SpinnerButtonBorder.ARCH_WIDTH));
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
    
    private static class SpinnerLabelBorder implements Border {
        private final static Insets INSETS = new Insets(1, 0, 1, 0);
        
        private final Widget widget;

        private SpinnerLabelBorder(Widget widget) {
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
    
    private static class SpinnerButtonBorder implements Border {
        
        private final static int ARCH_WIDTH = 16;
        private final static double ARROW_MARGIN = 0.2;
        private final static Insets INSETS = new Insets(0, 0, 0, 0);
        
        static enum ArrowType {
            LEFT {
                Shape createBorderShape(int x, int y, int w, int h) {
                    Path2D path = new Path2D.Float();
                    path.moveTo(x+ARCH_WIDTH, y);
                    path.quadTo(x, y, x, y+h/2);
                    path.quadTo(x, y+h, x+ARCH_WIDTH, y+h);
                    path.lineTo(x+w, y+h);
                    path.lineTo(x+w, y);
                    path.closePath();
                    return path;
                }
                
                Shape getArrowShape(Rectangle bounds) {
                    double x = bounds.x;
                    double y = bounds.y;
                    double w = bounds.width;
                    double h = bounds.height;
                    double mH = h * ARROW_MARGIN;
                    double mW = w * ARROW_MARGIN;
                    
                    Path2D path = new Path2D.Double();
                    path.moveTo(x+w - mW, y+mH);
                    path.lineTo(x+w - mW, y+h-mH);
                    path.lineTo(x+mW, y+w/2d);
                    path.closePath();
                    return path;
                }
            }, 
            RIGHT {
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
                
                Shape getArrowShape(Rectangle bounds) {
                    double x = bounds.x;
                    double y = bounds.y;
                    double w = bounds.width;
                    double h = bounds.height;
                    double mH = h * ARROW_MARGIN;
                    double mW = w * ARROW_MARGIN;
                    
                    Path2D path = new Path2D.Double();
                    path.moveTo(x+mW, y+mH);
                    path.lineTo(x+mW, y+h-mH);
                    path.lineTo(x+w-mW, y+w/2d);
                    path.closePath();
                    return path;
                }
            };
            
            Shape getBorderShape(Rectangle bounds) {
                if(bounds.width < ARCH_WIDTH)
                    return bounds;
                return createBorderShape(bounds.x, bounds.y, bounds.width, bounds.height);
            }
            
            abstract Shape createBorderShape(int x, int y, int w, int h);
            
            abstract Shape getArrowShape(Rectangle bounds);
        }
        
        private final Widget widget;
        private final ArrowType type;

        private SpinnerButtonBorder(Widget widget, ArrowType type) {
            this.widget = widget;
            this.type = type;
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
            Shape border = type.getBorderShape(bounds);
            if(widget.isOpaque()) {
                gr.setPaint(widget.getBackground());
                gr.fill(border);
            }
            
            gr.setPaint(getForeground());
            gr.draw(border);
            gr.fill(type.getArrowShape(bounds));
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
    }
    
}
