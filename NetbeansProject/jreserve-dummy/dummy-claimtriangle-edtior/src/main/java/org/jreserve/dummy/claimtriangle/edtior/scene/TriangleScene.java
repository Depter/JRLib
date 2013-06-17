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

import java.awt.Image;
import org.jreserve.dummy.claimtriangle.edtior.nodes.JRLibEdge;
import org.jreserve.dummy.claimtriangle.edtior.nodes.JRLibNode;
import org.jreserve.dummy.claimtriangle.edtior.nodes.JRLibPin;
import org.jreserve.dummy.claimtriangle.edtior.nodes.JRLibScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleScene extends JRLibScene {
    private final static String TREE_HOME = "org/jreserve/dummy/projecttree/resources/";
    private final static String HOME = "org/jreserve/dummy/claimtriangle/edtior/";
    private final static Image DATA_SOURCE = ImageUtilities.loadImage(TREE_HOME + "database.png"); // NOI18N
    private final static Image GEOMETRY = ImageUtilities.loadImage(HOME + "ruler_triangle.png"); // NOI18N

    public TriangleScene() {
        JRLibNode data = createDataNode();
        addJRLibNode(data);
        
        JRLibNode geometry = createGeometryNode();
        addJRLibNode(geometry);
        
        createEdge(new JRLibEdge(data, geometry));
    }
    
    private JRLibNode createDataNode() {
        JRLibNode node = new JRLibNode("Data", DATA_SOURCE);
        node.addPin(new JRLibPin(node, "Source:"));
        node.addPin(new JRLibPin(node, "Start:"));
        node.addPin(new JRLibPin(node, "End:"));
        return node;
    }
    
    private void addJRLibNode(JRLibNode node) {
        addNode(node);
        addPin(node, node.getDefaultPin());
        for(JRLibPin pin : node.getPins())
            addPin(node, pin);
    }
    
    private JRLibNode createGeometryNode() {
        JRLibNode node = new JRLibNode("Geometry / Length", GEOMETRY);
        node.addPin(new JRLibPin(node, "Accidents:"));
        node.addPin(new JRLibPin(node, "Developments:"));
        return node;
    }
    
    private void createEdge(JRLibEdge edge) {
        ConnectionWidget widget = (ConnectionWidget) addEdge(edge);
        setEdgeSource(edge, edge.getSource().getDefaultPin());
        setEdgeTarget(edge, edge.getTarget().getDefaultPin());
    }
}
