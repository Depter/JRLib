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
import org.jreserve.dummy.claimtriangle.edtior.scene.TriangleGraphScene.ComponentNode;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleGraphScene extends GraphScene<TriangleGraphScene.ComponentNode, Object>{

    private static int NODE_INDEX = 0;
    private static int EDGE_INDEX = 0;
    
    private LayerWidget mainLayer;

    public TriangleGraphScene() {
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);
        
        Widget data = addNode(new ComponentNode(new InputDataEditorPanel(), "Data Source"));
        data.setPreferredLocation(new Point(50, 50));
        
        Widget geometry = addNode(new ComponentNode(new GeometryEditorPanel(), "Geometry"));
        geometry.setPreferredLocation(new Point(50, 250));
        
        getActions().addAction(ActionFactory.createZoomAction());
    }

    @Override
    protected Widget attachNodeWidget(ComponentNode node) {
        Widget widget = createWidget(node);
        widget.getActions().addAction(ActionFactory.createMoveAction());
        mainLayer.addChild(widget);
        return widget;
    }
    
    private Widget createWidget(ComponentNode node) {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel label = new JLabel(node.title);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        panel.add(label, BorderLayout.NORTH);
        
        panel.add(node.component, BorderLayout.CENTER);
        
        ComponentWidget widget = new ComponentWidget(this, panel);
        widget.setBorder(createRoundedBorder());
        return widget;
    }
    
    private Border createRoundedBorder() {
        return org.netbeans.api.visual.border.BorderFactory.createRoundedBorder(
                3, 3, Color.BLACK, Color.GRAY);
    }

    @Override
    protected Widget attachEdgeWidget(Object edge) {
        return null;
    }

    @Override
    protected void attachEdgeSourceAnchor(Object edge, ComponentNode oldSourceNode, ComponentNode sourceNode) {
    }

    @Override
    protected void attachEdgeTargetAnchor(Object edge, ComponentNode oldTargetNode, ComponentNode targetNode) {
    }
    
    public static class ComponentNode {
        private final Component component;
        private final String title;
        private final int index;

        ComponentNode(Component component, String title) {
            this.component = component;
            this.title = title;
            this.index = NODE_INDEX++;
        }

        public Component getComponent() {
            return component;
        }

        public String getTitle() {
            return title;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public int hashCode() {
            return index;
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof ComponentNode) &&
                   index == ((ComponentNode)o).index;
        }
    }
    
    public static class ComponentEdge {
        private int idFrom;
        private int idTo;
        private int index;

        public ComponentEdge(ComponentNode from, ComponentNode to) {
            idFrom = from.index;
            idTo = to.index;
            index = EDGE_INDEX++;
        }

        public int getIdFrom() {
            return idFrom;
        }

        public int getIdTo() {
            return idTo;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public int hashCode() {
            return index;
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof ComponentEdge) &&
                   index == ((ComponentEdge)o).index;
        }
    }
}
