/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.dummy.claimtriangle.edtior.nodes.widgets;

import java.awt.Dimension;
import java.awt.Font;
import org.jreserve.dummy.claimtriangle.edtior.nodes.JRLibNode;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class JRLibNodeWidget extends Widget {
    
    private final static Dimension MIN_SIZE = new Dimension(128, 8);
    
    private JRLibNode node;
    private Widget header;
    private Widget imageWidget;
    private Widget titleWidget;
    
    private Anchor inputAnchor;
    private Anchor outputAnchor;
    
    private Widget pinSeparator;
    private JRLibWidgetUI ui;
    
    public JRLibNodeWidget(Scene scene, JRLibWidgetUI ui, JRLibNode node) {
        super(scene);
        this.ui = ui;
        this.node = node;
        initWidget(scene);
        ui.initUI(this);
    }
    
    private void initWidget(Scene scene) {
        
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setMinimumSize(MIN_SIZE);
        
        header = createHeaderWidget(scene);
        addChild(header);
        
        pinSeparator = new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL);
        addChild(pinSeparator);
        
        inputAnchor = new JRLibAnchor(this, true);
        outputAnchor = new JRLibAnchor(this, false);
    }
    
    private Widget createHeaderWidget(Scene scene) {
        header = new Widget(scene);
        header.setLayout (LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 8));
        imageWidget = new ImageWidget(super.getScene(), node.getImage());
        header.addChild (imageWidget);
        
        titleWidget = new LabelWidget(scene, node.getTitle());
        titleWidget.setFont (ui.getHeaderFont(scene));
        titleWidget.setForeground(ui.getHeaderTitleColor());
        header.addChild(titleWidget);
        
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
    
    public Anchor getInputAnchor() {
        return inputAnchor;
    }
    
    public Anchor getOutputAnchor() {
        return outputAnchor;
    }
}
