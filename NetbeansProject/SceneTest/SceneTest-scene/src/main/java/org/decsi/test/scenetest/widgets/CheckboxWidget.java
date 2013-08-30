/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
public class CheckboxWidget extends Widget implements SelectProvider {
    
    private final static String IMG_BASE = "org/decsi/test/scenetest/checkbox";
    private final static Image IMG = ImageUtilities.loadImage(IMG_BASE+".png");
    private final static Image IMG_SELECTED = ImageUtilities.loadImage(IMG_BASE+"_selected.png");
    private final static Image IMG_DISABLED = ImageUtilities.loadImage(IMG_BASE+"_disabled.png");
    private final static Image IMG_SELECTED_DISABLED = ImageUtilities.loadImage(IMG_BASE+"_selected_disabled.png");
    
    private final ActionSupport as = new ActionSupport(this);
    private ImageWidget image;
    private LabelWidget label;
    private boolean selected = false;
    
    public CheckboxWidget(JRLibScene scene) {
        this(scene, null);
    }

    public CheckboxWidget(JRLibScene scene, String label) {
        this(scene, label, true);
    }
    
    public CheckboxWidget(JRLibScene scene, String label, boolean rightAlign) {
        super(scene);
        setLayout(LayoutFactory.createHorizontalFlowLayout (LayoutFactory.SerialAlignment.CENTER, 5));
        
        image = new ImageWidget(scene, getImage());
        this.label = new LabelWidget(scene, label);
        
        if(rightAlign) {
            addChild(image, 0);
            addChild(this.label, 1);
        } else {
            addChild(this.label, 1);
            addChild(image, 0);
        }
        setBorder(new ImgBorder(this));
        
        getActions().addAction(ActionFactory.createSelectAction(this));
        getActions().addAction(scene.createWidgetHoverAction());
    }
    
    private Image getImage() {
        if(isEnabled()) {
            return selected? IMG_SELECTED : IMG;
        } else {
            return selected? IMG_SELECTED_DISABLED : IMG_DISABLED;
        }
    }
    
    public void setControlEnabled(boolean enabled) {
        if(enabled != super.isEnabled()) {
            JRLibWidgetUI ui = ((JRLibScene)getScene()).getUI();
            label.setForeground(enabled? ui.getControlForeground() : ui.getControlDisabledForeground());
            super.setEnabled(enabled);
            image.setImage(getImage());
        }
    }
    
    public boolean isControlEnabled() {
        return super.isEnabled();
    }
    
    public void setSelected(boolean selected) {
        if(this.selected != selected) {
            this.selected = selected;
            image.setImage(getImage());
            as.fireAction();
        }
    }
    
    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
        return true;
    }

    @Override
    public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
        return isEnabled();
    }

    @Override
    public void select(Widget widget, Point localLocation, boolean invertSelection) {
        setSelected(!selected);
        as.fireAction();
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

    @Override
    protected void notifyStateChanged(ObjectState previous, ObjectState current) {
        if(previous.isHovered() != current.isHovered())
            repaint();
    }
    
    private static class ImgBorder implements Border {
        
        private final static Insets INSETS = new Insets(0, 0, 0, 0);
        private final CheckboxWidget widget;

        ImgBorder(CheckboxWidget widget) {
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
            if(widget.isEnabled() && widget.getState().isHovered()) {
                JRLibWidgetUI ui = ((JRLibScene)widget.getScene()).getUI();
                gr.setColor(ui.getControlBorderHoverColor());
                gr.draw(bounds);
            }
        }
    }
}
