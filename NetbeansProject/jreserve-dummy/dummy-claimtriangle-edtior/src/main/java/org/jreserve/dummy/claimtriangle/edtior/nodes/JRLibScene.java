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
package org.jreserve.dummy.claimtriangle.edtior.nodes;

import org.jreserve.dummy.claimtriangle.edtior.nodes.widgets.JRLibNodeWidget;
import org.jreserve.dummy.claimtriangle.edtior.nodes.widgets.JRLibRouter;
import org.jreserve.dummy.claimtriangle.edtior.nodes.widgets.JRLibWidgetUI;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.graph.GraphPinScene;
import org.netbeans.api.visual.graph.layout.GridGraphLayout;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class JRLibScene extends GraphPinScene<JRLibNode, JRLibEdge, JRLibPin>{

    private LayerWidget backgroundLayer = new LayerWidget(this);
    private LayerWidget mainLayer = new LayerWidget(this);
    private LayerWidget connectionLayer = new LayerWidget(this);
    private LayerWidget upperLayer = new LayerWidget(this);
    
    private Router router = new JRLibRouter(); //RouterFactory.createDirectRouter();
    private SceneLayout sceneLayout;
    
    private JRLibWidgetUI ui = new JRLibWidgetUI();
    private WidgetAction connectAction = ActionFactory.createExtendedConnectAction(connectionLayer, new JRLibConnectionProvider(this));
    
    public JRLibScene() {
        setKeyEventProcessingType(EventProcessingType.FOCUSED_WIDGET_AND_ITS_PARENTS);
        initLayout();
        initLayers();
        initActions();
    }
    
    private void initLayout() {
        GridGraphLayout<JRLibNode, JRLibEdge> ggl = new GridGraphLayout<JRLibNode, JRLibEdge>();
        ggl.setChecker(true);
        sceneLayout = LayoutFactory.createSceneGraphLayout(this, ggl);
    }
    
    private void initLayers() {
        addChild(backgroundLayer);
        addChild(mainLayer);
        addChild(connectionLayer);
        addChild(upperLayer);
    }
    
    private void initActions() {
        getActions ().addAction (ActionFactory.createZoomAction ());
        getActions ().addAction (ActionFactory.createPanAction ());
        getActions ().addAction (ActionFactory.createRectangularSelectAction (this, backgroundLayer));
    }
    
    @Override
    protected Widget attachNodeWidget(JRLibNode node) {
        JRLibNodeWidget widget = new JRLibNodeWidget(this, ui, node);
        mainLayer.addChild(widget);
        createNodeActions(widget);
        
        return widget;
    }
    
    private void createNodeActions(JRLibNodeWidget widget) {
        widget.getActions().addAction(ActionFactory.createMoveAction());
    }

    @Override
    protected Widget attachPinWidget(JRLibNode node, JRLibPin pin) {
        if(pin.getId() < 0)
            return null;
        
        Widget widget = pin.createPin(this, ui);
        ((JRLibNodeWidget) findWidget (node)).attachPinWidget (widget);
        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(JRLibEdge edge) {
        ConnectionWidget widget = new ConnectionWidget(this);
        widget.setRouter(router);
        connectionLayer.addChild(widget);
        createConnectionActions(widget);
        return widget;
    }
    
    private void createConnectionActions(ConnectionWidget widget) {
        widget.getActions().addAction(ActionFactory.createReconnectAction(null));
        widget.getActions().addAction(createSelectAction());
    }

    @Override
    protected void attachEdgeSourceAnchor(JRLibEdge edge, JRLibPin oldSourcePin, JRLibPin sourcePin) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        JRLibNodeWidget sourceWidget = getPinNodeWidget(sourcePin);
        edgeWidget.setSourceAnchor(sourceWidget.getOutputAnchor());
    }
    
    private JRLibNodeWidget getPinNodeWidget(JRLibPin pin) {
        JRLibNode node = pin.getNode();
        return (JRLibNodeWidget) findWidget(node);
    }
    
    
    @Override
    protected void attachEdgeTargetAnchor(JRLibEdge edge, JRLibPin oldTargetPin, JRLibPin targetPin) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        JRLibNodeWidget targetWidget = getPinNodeWidget(targetPin);
        edgeWidget.setTargetAnchor(targetWidget.getInputAnchor());
    }
    
    public void layoutScene() {
        sceneLayout.invokeLayout();
    }
    
}
