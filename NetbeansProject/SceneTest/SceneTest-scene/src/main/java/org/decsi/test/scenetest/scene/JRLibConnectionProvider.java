/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.scene;

import java.awt.Point;
import org.decsi.test.scenetest.widgets.JRLibNodeWidget;
import org.decsi.test.scenetest.widgets.LabelPinWidget;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 */
class JRLibConnectionProvider implements ConnectProvider {

    private JRLibScene scene;

    JRLibConnectionProvider(JRLibScene scene) {
        this.scene = scene;
    }
    
    @Override
    public boolean isSourceWidget(Widget source) {
        return (source instanceof LabelPinWidget);
    }

    @Override
    public ConnectorState isTargetWidget(Widget source, Widget target) {
        return(source instanceof JRLibNodeWidget) &&
               (target instanceof JRLibNodeWidget) &&
               source != target?
                ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public boolean hasCustomTargetWidgetResolver(Scene scene) {
        return false;
    }

    @Override
    public Widget resolveTargetWidget(Scene scene, Point sceneLocation) {
        return null;
    }

    @Override
    public void createConnection(Widget source, Widget target) {
        System.out.println(source);
    }
    
}
