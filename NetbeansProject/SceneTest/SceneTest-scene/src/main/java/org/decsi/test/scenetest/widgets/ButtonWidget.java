/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ButtonWidget extends LabelWidget{
    
    private ButtonSelectProvider selectProvider;
    
    public ButtonWidget(JRLibScene scene, String label) {
        super(scene, label);
        setForeground(scene.getUI().getControlForeground());
        setBorder(new ButtonBorder(this));
        
        selectProvider = new ButtonSelectProvider();
        getActions().addAction(ActionFactory.createSelectAction(selectProvider));
        getActions().addAction(scene.createWidgetHoverAction());
    }

    public void addActionListener(ActionListener listener) {
        selectProvider.addActionListener(listener);
    }

    public void removeActionListener(ActionListener listener) {
        selectProvider.removeActionListener(listener);
    }

    public void setActionCommand(String command) {
        selectProvider.setActionCommand(command);
    }

    public String getActionCommand() {
        return selectProvider.getActionCommand();
    }
    
    public void setButtonEnabled(boolean enabled) {
        if(isEnabled() != enabled) {
            JRLibWidgetUI ui = ((JRLibScene)getScene()).getUI();
            setForeground(enabled? ui.getControlForeground() : ui.getControlDisabledForeground());
            setEnabled(enabled);
            repaint();
        }
    }
    
    public boolean isButtonEnabled() {
        return isEnabled();
    }

    @Override
    protected void notifyStateChanged(ObjectState previous, ObjectState current) {
        if(previous.isHovered() != current.isHovered())
            repaint();
    }
    
    private static class ButtonSelectProvider implements SelectProvider {

        private List<ActionListener> listeners = new ArrayList<ActionListener>();
        private String actionCommand = "buttonAction";
        
        void addActionListener(ActionListener listener) {
            if(listener != null && !listeners.contains(listener))
                listeners.add(listener);
        }
        
        void removeActionListener(ActionListener listener) {
            listeners.remove(listener);
        }
        
        void setActionCommand(String command) {
            if(command == null)
                throw new NullPointerException("Action command can not be null!");
            this.actionCommand = command;
        }
        
        String getActionCommand() {
            return actionCommand;
        }
        
        @Override
        public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return widget.isEnabled();
        }

        @Override
        public void select(Widget widget, Point localLocation, boolean invertSelection) {
            ActionEvent evt = null;
            for(ActionListener listener : listeners.toArray(new ActionListener[listeners.size()])) {
                if(evt == null)
                    evt = new ActionEvent(widget, 0, actionCommand);
                listener.actionPerformed(evt);
            }
        }
    }
    
    private static class ButtonBorder implements Border {
        private final static int ARCH_WIDTH = 16;
        private final static Insets BORDER_INSETS = new Insets(2, ARCH_WIDTH, 2, ARCH_WIDTH);
        private Widget widget;

        public ButtonBorder(Widget widget) {
            this.widget = widget;
        }
        
        @Override
        public Insets getInsets() {
            return BORDER_INSETS;
        }

        @Override
        public void paint(Graphics2D gr, Rectangle bounds) {
            if(bounds.width < 2*ARCH_WIDTH) {
                paintSmall(gr, bounds);
            } else {
                paintRound(gr, bounds);
            }
        }
        
        private void paintSmall(Graphics2D gr, Rectangle bounds) {
            int x = bounds.x;
            int y = bounds.y;
            int w = bounds.width;
            int h = bounds.height;
            
            if(widget.isOpaque()) {
                gr.setPaint(widget.getBackground());
                gr.fillRect(x+2, y+2, w-4, h-4);
            }
            
            gr.setPaint(getBorderColor());
            gr.drawRect(x+1, y+1, w-2, h-2);
            
            if(widget.getState().isHovered()) {
                gr.setPaint(getHoverColor());
                gr.drawRect(x, y, w, h);
            }
        }
        
        private Color getBorderColor() {
            JRLibWidgetUI ui = ((JRLibScene)widget.getScene()).getUI();
            if(widget.isEnabled())
                return ui.getControlBorderColor();
            return ui.getControlBorderDisabledColor();
        }
        
        private Color getHoverColor() {
            JRLibWidgetUI ui = ((JRLibScene)widget.getScene()).getUI();
            return ui.getControlBorderHoverColor();
        }
        
        private void paintRound(Graphics2D gr, Rectangle bounds) {
            int x = bounds.x;
            int y = bounds.y;
            int w = bounds.width;
            int h = bounds.height;
            
            if(widget.isOpaque()) {
                gr.setPaint(widget.getBackground());
                gr.drawRoundRect(x+1, y+1, w-2, h-2, ARCH_WIDTH-2, h-2);
            }
            
            gr.setPaint(getBorderColor());
            gr.drawRoundRect(x+1, y+1, w-2, h-2, ARCH_WIDTH-2, h-2);
            
            if(widget.isEnabled() && widget.getState().isHovered()) {
                gr.setPaint(getHoverColor());
                gr.drawRoundRect(x, y, w, h, ARCH_WIDTH, h);
            }
        }
        
        @Override
        public boolean isOpaque() {
            return true;
        }
    }
}
