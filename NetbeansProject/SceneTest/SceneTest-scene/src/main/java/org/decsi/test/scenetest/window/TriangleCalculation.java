/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.window;

import java.util.ArrayList;
import java.util.List;
import org.decsi.test.scenetest.scene.JRLibNode;
import org.decsi.test.scenetest.scene.JRLibScene;

/**
 *
 * @author AA461472
 */
public class TriangleCalculation {
    
    private List<JRLibNode> nodes = new ArrayList<JRLibNode>();
    
    public TriangleCalculation() {
        
    }
    
    public void fillScene(JRLibScene scene) {
        DataSourceNode dsNode = new DataSourceNode();
        GeometryNode geometry = new GeometryNode();
        
        scene.addNode(dsNode);
        scene.addNode(geometry);
    }
}
