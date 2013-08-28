/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import org.decsi.test.scenetest.scene.JRLibPin;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SpinnerPinWidget extends EmptyPinWidget {

    public SpinnerPinWidget(JRLibScene scene, JRLibWidgetUI ui, JRLibPin pin) {
        super(scene, ui, pin);
    }

    @Override
    protected Widget getContentWidget(JRLibScene scene) {
        return super.getContentWidget(scene); //To change body of generated methods, choose Tools | Templates.
    }
    
}
