/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.JButton;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 */
public class ButtonWidget extends Widget {
    
    private JButton button;
    
    public ButtonWidget(Scene scene, String text) {
        super(scene);
        button = new JButton(text);
        recalcPrefferedSize();
        getActions().addAction(scene.createWidgetHoverAction());
        getActions().addAction(ActionFactory.createSelectAction(new ButtonSelectProvider()));
    }
    
    public void addActionListener(ActionListener listener) {
        button.addActionListener(listener);
    }
    
    public void removeActionListener(ActionListener listener) {
        button.removeActionListener(listener);
    }

    public String getActionCommand() {
        return button.getActionCommand();
    }
    
    public void setActionCommand(String command) {
        button.setActionCommand(command);
    }
    
    private void recalcPrefferedSize() {
        Dimension size = button.getPreferredSize();
        Icon icon = button.getIcon();
        if(icon != null) {
            size.setSize(size.width, icon.getIconHeight()+2);
        } else {
            size.setSize(size.width, Math.min(18, size.height));
        }
        button.setPreferredSize(size);
    }
    
    protected  Rectangle calculateClientArea () {
        return new Rectangle(button.getPreferredSize());
    }
 
    protected void  paintWidget () {
        button.setSize(getBounds().getSize());
        button.paint(getGraphics ());
    }    

    protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
        boolean shouldRepaint = false;
        if(previousState.isHovered ()  != state.isHovered ()) {
            hoverChanged(state.isHovered());
            shouldRepaint = true;
        }
        
        if(shouldRepaint)
            repaint();
    }
    
    private void hoverChanged(boolean isOver) {
        MouseEvent evt = null;
        for(MouseListener ml : button.getMouseListeners()) {
            if(evt == null)
                evt = createMouseEvent();
            if(isOver)
                ml.mouseEntered(evt);
            else
                ml.mouseExited(evt);
        }
    }
    
    private MouseEvent createMouseEvent() {
        return new MouseEvent(button, 0, System.currentTimeMillis(), 0, 0, 0, 0, false, MouseEvent.NOBUTTON);
    }
    
    private class ButtonSelectProvider implements SelectProvider {
        
        private ButtonSelectProvider() {
        }
        
        @Override
        public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return true;
        }

        @Override
        public void select(Widget widget, Point localLocation, boolean invertSelection) {
            ActionEvent evt = null;
            for(ActionListener listener : button.getActionListeners()) {
                if(evt == null)
                    evt = new ActionEvent(ButtonWidget.this, 0, button.getActionCommand());
                listener.actionPerformed(evt);
            }
        }
    }
}
