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

import java.awt.Point;
import org.jreserve.dummy.claimtriangle.edtior.nodes.widgets.JRLibNodeWidget;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class JRLibConnectionProvider implements ConnectProvider {

    private JRLibScene scene;

    JRLibConnectionProvider(JRLibScene scene) {
        this.scene = scene;
    }
    
    @Override
    public boolean isSourceWidget(Widget source) {
        return (source instanceof JRLibNodeWidget);
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
