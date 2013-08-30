/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import java.awt.Dimension;
import java.awt.Rectangle;
import org.decsi.test.scenetest.scene.JRLibNode;
import org.decsi.test.scenetest.scene.JRLibPin;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.ResizeStrategy;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 */
public class JRLibNodeWidget extends Widget {
    
    private final static Dimension MIN_SIZE = new Dimension(128, 8);
    private static ResizeUtil RESIZE_UTIL = new ResizeUtil();
    
    private JRLibNode node;
    private Border border;
    private Border resizeBorder;
    private Widget header;
    private Widget imageWidget;
    private Widget titleWidget;
    
    private Widget pinSeparator;
    private JRLibWidgetUI ui;
    
    public JRLibNodeWidget(JRLibScene scene, JRLibWidgetUI ui, JRLibNode node) {
        super(scene);
        this.ui = ui;
        this.node = node;
        initWidget(scene);
        initActions(scene);
    }
    
    private void initWidget(JRLibScene scene) {
        setLayout(LayoutFactory.createVerticalFlowLayout());
        //setMinimumSize(MIN_SIZE);
        
        header = createHeaderWidget(scene);
        addChild(header);
        
        pinSeparator = new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL);
        addChild(pinSeparator);
        
        for(JRLibPin pin : node.getPins())
            addChild(pin.createPinWidget(scene, ui));
        
        ui.initUI(this);
        border = getBorder();
        resizeBorder = BorderFactory.createCompositeBorder(
                BorderFactory.createResizeBorder(8),
                border
                );
        border = BorderFactory.createCompositeBorder(
                BorderFactory.createEmptyBorder(8),
                border
                );
        setBorder(border);
    }
    
    private void initActions(JRLibScene scene) {
        getActions().addAction(scene.createSelectAction());
        getActions().addAction(ActionFactory.createResizeAction(RESIZE_UTIL, RESIZE_UTIL));
    }
    
    private Widget createHeaderWidget(Scene scene) {
        header = new Widget(scene);
        header.setLayout (LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 8));
        
        if(node.hasInput())
            header.addChild(new AnchorWidget(scene, true, ui), 0);
        
        imageWidget = new ImageWidget(super.getScene(), node.getImage());
        header.addChild(imageWidget, 0);

        titleWidget = new LabelWidget(scene, node.getTitle());
        titleWidget.setFont (ui.getHeaderFont(scene));
        titleWidget.setForeground(ui.getHeaderTitleColor());
        header.addChild(titleWidget, 1);

        if(node.hasOutput())
            header.addChild(new AnchorWidget(scene, false, ui), 0);
        
        return header;
    
    }
    
    Widget getHeaderWidget() {
        return header;
    }
    
    Widget getPinSeparator() {
        return pinSeparator;
    }
    
    public void attachPinWidget(Widget widget) {
        widget.setCheckClipping(true);
        addChild(widget);
    }

    @Override
    protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
        if(previousState.isSelected() == state.isSelected())
            return;
        setBorder(state.isSelected()? resizeBorder : border);
    }
    
    private static class ResizeUtil implements ResizeStrategy, ResizeProvider {
        @Override
        public Rectangle boundsSuggested(Widget widget, Rectangle original, Rectangle suggested, ResizeProvider.ControlPoint controlPoint) {
            int width = widget.getMinimumSize().width;
            width = Math.max(width, suggested.width);
            return new Rectangle(
                suggested.x, suggested.y, 
                width, original.height);
        }

        @Override
        public void resizingStarted(Widget widget) {
            Dimension min = widget.getMinimumSize();
            if(min == null) {
                min = widget.getPreferredBounds().getSize();
                //min.width += 18;
                widget.setMinimumSize(min);
            }
        }

        @Override
        public void resizingFinished(Widget widget) {
        }
    }
}
