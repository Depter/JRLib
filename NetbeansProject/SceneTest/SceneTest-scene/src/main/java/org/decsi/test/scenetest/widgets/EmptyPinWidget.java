/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import org.decsi.test.scenetest.scene.JRLibPin;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 */
public class EmptyPinWidget extends Widget {
    
    private JRLibWidgetUI ui;
    private JRLibPin pin;
    private Anchor inputAnchor;
    private Anchor outputAnchor;

    public EmptyPinWidget(JRLibScene scene, JRLibWidgetUI ui, JRLibPin pin) {
        super(scene);
        this.ui = ui;
        this.pin = pin;
        initWidget(scene);
        ui.initUI(this);
    }
    
    private void initWidget(JRLibScene scene) {
        setLayout(LayoutFactory.createHorizontalFlowLayout (LayoutFactory.SerialAlignment.CENTER, 8));
        if(pin.hasInput()) {
            addChild(new AnchorWidget(scene, true, ui), 0);
            inputAnchor = new JRLibAnchor(this, true);
        }
       
        addChild(getContentWidget(scene), 1);
       
        if(pin.hasOutput()) {
            addChild(new AnchorWidget(scene, false, ui), 0);
            outputAnchor = new JRLibAnchor(this, false);
        }
    }
    
    protected JRLibPin getPin() {
        return pin;
    }
    
    protected JRLibWidgetUI getUI() {
        return ui;
    }
    
    protected Widget getContentWidget(JRLibScene scene) {
        return new Widget(scene);
    }
}


