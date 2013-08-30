/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.window;

import javax.swing.AbstractSpinnerModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import org.decsi.test.scenetest.scene.JRLibNode;
import org.decsi.test.scenetest.scene.JRLibPin;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.decsi.test.scenetest.widgets.EmptyPinWidget;
import org.decsi.test.scenetest.widgets.JRLibWidgetUI;
import org.decsi.test.scenetest.widgets.SpinnerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
public class GeometryNode extends JRLibNode {
    private final static String IMG_PATH = "org/decsi/test/scenetest/filter.png";

    public GeometryNode() {
        super("Geometry", ImageUtilities.loadImage(IMG_PATH));
        super.addPin(new JRLibPin(this));
        super.addPin(new JRLibPin.Label(this, "Start Date:"));
        super.addPin(new StartDatePin(this));
        super.addPin(new JRLibPin.Label(this, "Accident Length:"));
        super.addPin(new PeriodPin(this));
        super.addPin(new JRLibPin.Label(this, "Development Length:"));
        super.addPin(new PeriodPin(this));
    }
    
    private class StartDatePin extends JRLibPin {

        public StartDatePin(JRLibNode node) {
            super(node);
        }

        @Override
        public StartDateWidget createPinWidget(JRLibScene scene, JRLibWidgetUI ui) {
            return new StartDateWidget(scene, ui, this);
        }
    }
    
    private class StartDateWidget extends EmptyPinWidget {

        private StartDateWidget(JRLibScene scene, JRLibWidgetUI ui, JRLibPin pin) {
            super(scene, ui, pin);
        }
        
        @Override
        protected Widget getContentWidget(JRLibScene scene) {
            MonthDateSpinnerModel model = new MonthDateSpinnerModel();
            SpinnerWidget w = new SpinnerWidget(scene, model, model);
            return w;
        }
    }
}
