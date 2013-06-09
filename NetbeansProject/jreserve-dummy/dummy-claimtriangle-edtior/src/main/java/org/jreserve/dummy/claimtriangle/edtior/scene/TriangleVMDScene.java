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

import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.util.Collections;
import javax.swing.JComponent;
import org.jreserve.dummy.claimtriangle.edtior.GeometryEditorPanel;
import org.jreserve.dummy.claimtriangle.edtior.InputDataEditorPanel;
import org.netbeans.api.visual.vmd.VMDGraphScene;
import org.netbeans.api.visual.vmd.VMDNodeWidget;
import org.netbeans.api.visual.vmd.VMDPinWidget;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleVMDScene extends VMDGraphScene {
    private final static String TREE_HOME = "org/jreserve/dummy/projecttree/resources/";
    private final static String HOME = "org/jreserve/dummy/claimtriangle/edtior/";
    private final static Image DATA_SOURCE = ImageUtilities.loadImage(TREE_HOME+"database.png"); // NOI18N
    private final static Image GEOMETRY = ImageUtilities.loadImage(HOME+"ruler_triangle.png"); // NOI18N

    private static int NODE_ID;
    private static int EDGE_ID;
    
    public TriangleVMDScene() {
        String data = createNode(this, 100, 20, DATA_SOURCE, "Data Source");
        createPin(this, data, "dataPin", new InputDataEditorPanel());
        
        String geometry = createNode(this, 100, 220, GEOMETRY, "Geometry");
        createPin(this, geometry, "geometryPin", new GeometryEditorPanel());
        createEdge(this, "dataPin", geometry);
    }
    

    private static String createNode (VMDGraphScene scene, int x, int y, Image image, String name) {
        String nodeID = "node" + (NODE_ID++);
        VMDNodeWidget widget = (VMDNodeWidget) scene.addNode (nodeID);
        widget.setPreferredLocation (new Point (x, y));
        widget.setNodeProperties (image, name, null, null);
        scene.addPin (nodeID, nodeID + VMDGraphScene.PIN_ID_DEFAULT_SUFFIX);
        return nodeID;
    }
    
    private static void createPin (VMDGraphScene scene, String nodeID, String pinID, JComponent component) {
        VMDPinWidget pin = (VMDPinWidget) scene.addPin (nodeID, pinID);
        //pin.setProperties (name, null);
        pin.removeChild(pin.getPinNameWidget());
        
        component.setOpaque(false);
        ComponentWidget widget = new ComponentWidget(scene, component);
        pin.addChild(widget);
    }
    
    private static void createEdge (VMDGraphScene scene, String sourcePinID, String targetNodeID) {
        String edgeID = "edge" + EDGE_ID++;
        scene.addEdge (edgeID);
        scene.setEdgeSource (edgeID, sourcePinID);
        scene.setEdgeTarget (edgeID, targetNodeID + VMDGraphScene.PIN_ID_DEFAULT_SUFFIX);
    }
}
