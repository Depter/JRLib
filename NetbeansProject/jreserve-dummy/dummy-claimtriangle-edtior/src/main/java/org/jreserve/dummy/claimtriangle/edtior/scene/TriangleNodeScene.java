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
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import org.netbeans.api.visual.vmd.VMDGraphScene;
import org.netbeans.api.visual.vmd.VMDNodeWidget;
import org.netbeans.api.visual.vmd.VMDPinWidget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleNodeScene extends VMDGraphScene {
    
    private final static String HOME = "org/jreserve/dummy/claimtriangle/edtior/scene/";
    private static final Image IMAGE_LIST = ImageUtilities.loadImage(HOME+"list_16.png"); // NOI18N
    private static final Image IMAGE_CANVAS = ImageUtilities.loadImage(HOME+"custom_displayable_16.png"); // NOI18N
    private static final Image IMAGE_COMMAND = ImageUtilities.loadImage(HOME+"command_16.png"); // NOI18N
    private static final Image IMAGE_ITEM = ImageUtilities.loadImage(HOME+"item_16.png"); // NOI18N
    private static final Image GLYPH_PRE_CODE = ImageUtilities.loadImage(HOME+"preCodeGlyph.png"); // NOI18N
    private static final Image GLYPH_POST_CODE = ImageUtilities.loadImage(HOME+"postCodeGlyph.png"); // NOI18N
    private static final Image GLYPH_CANCEL = ImageUtilities.loadImage(HOME+"cancelGlyph.png"); // NOI18N
    
    private static int nodeID = 1;
    private static int edgeID = 1;

    public TriangleNodeScene() {
        String mobile = createNode (this, 100, 100, IMAGE_LIST, "menu", "List", null);
        createPin (this, mobile, "start", IMAGE_ITEM, "Start", "Element");
        String game = createNode (this, 600, 100, IMAGE_CANVAS, "gameCanvas", "MyCanvas", Arrays.asList (GLYPH_PRE_CODE, GLYPH_CANCEL, GLYPH_POST_CODE));
        createPin (this, game, "ok", IMAGE_COMMAND, "okCommand1", "Command");
        createEdge (this, "start", game);
        createEdge (this, "ok", mobile);
    }

    private static String createNode (VMDGraphScene scene, int x, int y, Image image, String name, String type, List<Image> glyphs) {
        String nodeID = "node" + TriangleNodeScene.nodeID ++;
        VMDNodeWidget widget = (VMDNodeWidget) scene.addNode (nodeID);
        widget.setPreferredLocation (new Point (x, y));
        widget.setNodeProperties (image, name, type, glyphs);
        scene.addPin (nodeID, nodeID + VMDGraphScene.PIN_ID_DEFAULT_SUFFIX);
        return nodeID;
    }


    private static void createPin (VMDGraphScene scene, String nodeID, String pinID, Image image, String name, String type) {
        ((VMDPinWidget) scene.addPin (nodeID, pinID)).setProperties (name, null);
    }


    private static void createEdge (VMDGraphScene scene, String sourcePinID, String targetNodeID) {
        String edgeID = "edge" + TriangleNodeScene.edgeID ++;
        scene.addEdge (edgeID);
        scene.setEdgeSource (edgeID, sourcePinID);
        scene.setEdgeTarget (edgeID, targetNodeID + VMDGraphScene.PIN_ID_DEFAULT_SUFFIX);
    }
}
