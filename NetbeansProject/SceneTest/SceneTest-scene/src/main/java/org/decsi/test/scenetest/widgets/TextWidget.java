/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.RoundRectangle2D;
import javax.swing.event.ChangeListener;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ChangeSupport;

/**
 *
 * @author Peter Decsi
 */
public class TextWidget<T> extends LabelWidget {
    
    private ChangeSupport cs = new ChangeSupport(this);
    private TextBridge<T> bridge;
    
    public TextWidget(JRLibScene scene, TextBridge<T> bridge) {
        this(scene, bridge, null);
    }
    
    public TextWidget(JRLibScene scene, TextBridge<T> bridge, T value) {
        super(scene);
        this.bridge = bridge;
        setAlignment(LabelWidget.Alignment.CENTER);
        
        JRLibWidgetUI ui = scene.getUI();
        setLabel(bridge.toString(value));
        setBorder(new TextBorder(scene));
        setForeground(ui.getControlForeground());
        setOpaque(false);
        
        getActions().addAction(scene.createWidgetHoverAction());
        getActions().addAction(ActionFactory.createInplaceEditorAction(new EditProvider()));
    }
    
    public void setControlEnabled(boolean enabled) {
        JRLibWidgetUI ui = ((JRLibScene)getScene()).getUI();
        setForeground(enabled? ui.getControlForeground() : ui.getControlDisabledForeground());
        super.setEnabled(enabled);
        repaint();
    }
    
    public boolean isControlEnabled() {
        return super.isEnabled();
    }
    
    public void setValue(T value) {
        setLabel(bridge.toString(value));
        cs.fireChange();
    }
    
    public T getValue() {
        String str = getLabel();
        return bridge.toValue(str);
    }
    
    public void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }

    @Override
    protected void notifyStateChanged(ObjectState previous, ObjectState current) {
        if(previous.isHovered() != current.isHovered())
            repaint();
    }
    
    private class EditProvider implements TextFieldInplaceEditor {

        @Override
        public boolean isEnabled(Widget widget) {
            return TextWidget.this.isEnabled();
        }

        @Override
        public String getText(Widget widget) {
            return getLabel();
        }

        @Override
        public void setText(Widget widget, String text) {
            try {
                bridge.toValue(text);
                setLabel(text);
                cs.fireChange();
            } catch (IllegalArgumentException ex) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
    
    private static class TextBorder implements Border {
        private final static int ARCH = 6;
        private final static Insets INSETS = new Insets(0, 0, 0, 0);
        private Widget widget;
        
        TextBorder(Widget widget) {
            this.widget = widget;
        }
  
        @Override
        public Insets getInsets () {
            return INSETS;
        }

        @Override
        public boolean isOpaque () {
            return true;
        }

        public void paint(Graphics2D gr, Rectangle bounds) {
            JRLibWidgetUI ui = ((JRLibScene)widget.getScene()).getUI();
            gr.setColor(widget.isEnabled()? ui.getTextEnabledBg() : ui.getTextDisabledBg());
            gr.fill(new RoundRectangle2D.Float (bounds.x, bounds.y, bounds.width, bounds.height, ARCH, ARCH));
                
            Color bc;
            if(widget.isEnabled()) {
                if(widget.getState().isHovered()) {
                    bc = ui.getControlBorderHoverColor();
                } else {
                    bc = ui.getControlBorderColor();
                }
            } else {
                bc = ui.getControlBorderDisabledColor();
            }
            gr.setColor(bc);
            gr.draw (new RoundRectangle2D.Float (bounds.x + 0.5f, bounds.y + 0.5f, bounds.width - 1, bounds.height - 1, ARCH, ARCH));
        }
        
    }
    
}
