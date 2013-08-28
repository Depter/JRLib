/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.scene;

import org.decsi.test.scenetest.widgets.JRLibNodeWidget;
import org.decsi.test.scenetest.widgets.JRLibWidgetUI;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.graph.GraphPinScene;
import org.netbeans.api.visual.graph.layout.GridGraphLayout;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.router.Router;
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
    private Router router = new JRLibSceneRouter();
    private SceneLayout sceneLayout;

    private JRLibWidgetUI ui = new JRLibWidgetUI();
    private WidgetAction connectAction = ActionFactory.createExtendedConnectAction(connectionLayer, new JRLibConnectionProvider(this));
     
    public JRLibScene() {
        setKeyEventProcessingType(EventProcessingType.FOCUSED_WIDGET_AND_ITS_PARENTS);
        addLayers();
        createSceneActions();
        createSceneLayout();
    }
    
    private void addLayers() {
        addChild(backgroundLayer);
        addChild(mainLayer);
        addChild(connectionLayer);
        addChild(upperLayer);
    }
    
    private void createSceneActions() {
        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createPanAction());
        getActions().addAction(ActionFactory.createRectangularSelectAction(this, backgroundLayer));
    }
    
    private void createSceneLayout() {
        GridGraphLayout<JRLibNode, JRLibEdge> ggl = new GridGraphLayout<JRLibNode, JRLibEdge>();
        ggl.setChecker(true);
        sceneLayout = LayoutFactory.createSceneGraphLayout(this, ggl);
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
        Widget widget = pin.createPinWidget(this, ui);
        ((JRLibNodeWidget) findWidget (node)).attachPinWidget(widget);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
//        JRLibNodeWidget sourceWidget = getPinNodeWidget(sourcePin);
//        edgeWidget.setSourceAnchor(sourceWidget.getOutputAnchor());
    }

    @Override
    protected void attachEdgeTargetAnchor(JRLibEdge edge, JRLibPin oldTargetPin, JRLibPin targetPin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
