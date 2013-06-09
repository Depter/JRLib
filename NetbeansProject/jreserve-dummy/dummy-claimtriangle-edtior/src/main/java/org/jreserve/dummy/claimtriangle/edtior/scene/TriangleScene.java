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
package org.jreserve.dummy.claimtriangle.edtior.scene;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.jreserve.dummy.claimtriangle.edtior.GeometryEditorPanel;
import org.jreserve.dummy.claimtriangle.edtior.InputDataEditorPanel;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleScene extends Scene {

    public TriangleScene() {
        LayerWidget baseLayer = new LayerWidget(this);
        LayerWidget connectionLayer = new LayerWidget(this);
        
        
        Widget dataWidget = createWidget("Data", new InputDataEditorPanel());
        dataWidget.setPreferredLocation(new Point(10, 50));
        baseLayer.addChild(dataWidget);
        
        Widget geometryWidget = createWidget("Geometry", new GeometryEditorPanel());
        geometryWidget.setPreferredLocation(new Point(10, 250));
        baseLayer.addChild(geometryWidget);
        
        //LabelWidget helloLabel1 = new LabelWidget(this, "hello1");
        //helloLabel1.setPreferredLocation(new Point(10, 50));
        //baseLayer.addChild(helloLabel1);

//        LabelWidget helloLabel2 = new LabelWidget(this, "hello2");
//        helloLabel2.setPreferredLocation(new Point(100, 50));
//        baseLayer.addChild(helloLabel2);
//
//        ConnectionWidget connectionWidget = new ConnectionWidget(this);
//        connectionWidget.setTargetAnchorShape(AnchorShape.TRIANGLE_HOLLOW);
//        connectionWidget.setSourceAnchor(AnchorFactory.createRectangularAnchor(dataWidget));
//        connectionWidget.setTargetAnchor(AnchorFactory.createRectangularAnchor(helloLabel2));
//        connectionLayer.addChild(connectionWidget);

        addChild(baseLayer);
        addChild(connectionLayer);

    }
    
    private Widget createWidget(String title, Component component) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        
        JLabel label = new JLabel(title);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        panel.add(label, BorderLayout.NORTH);
        
        panel.add(component, BorderLayout.CENTER);
        
        ComponentWidget widget = new ComponentWidget(this, panel);
        widget.setBorder(createRoundedBorder());
        return widget;
    }
    
    private Border createRoundedBorder() {
        return org.netbeans.api.visual.border.BorderFactory.createRoundedBorder(
                10, 10, Color.BLACK, Color.GRAY);
    }
}
