/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import org.decsi.test.scenetest.scene.JRLibPin;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 */
public class LabelPinWidget extends EmptyPinWidget {
    
    private LabelWidget nameWidget;
    
    public LabelPinWidget(JRLibScene scene, JRLibWidgetUI ui, JRLibPin pin, String label) {
        super(scene, ui, pin);
        nameWidget.setLabel(label);
    }

    @Override
    protected Widget getContentWidget(JRLibScene scene) {
        nameWidget = new LabelWidget(scene);
        nameWidget.setFont(getUI().getPinFont(scene));
        nameWidget.setForeground(getUI().getPinTitleColor());
        return nameWidget;
    }
}
