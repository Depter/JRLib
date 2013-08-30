/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.window;

import org.decsi.test.scenetest.scene.JRLibNode;
import org.decsi.test.scenetest.scene.JRLibPin;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.decsi.test.scenetest.widgets.CheckboxWidget;
import org.decsi.test.scenetest.widgets.EmptyPinWidget;
import org.decsi.test.scenetest.widgets.JRLibWidgetUI;
import org.decsi.test.scenetest.widgets.SpinnerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
public class CellNode extends JRLibNode {
    private final static String IMG_PATH = "org/decsi/test/scenetest/correction.png";

    public CellNode() {
        super("Smoothing Cell", ImageUtilities.loadImage(IMG_PATH));
        super.addPin(new JRLibPin(this));
        super.addPin(new ApplyPin(this));
        super.addPin(new JRLibPin.Label(this, "Accident:"));
        super.addPin(new AccidentPin(this));
        super.addPin(new JRLibPin.Label(this, "Development:"));
        super.addPin(new DevelopmentPin(this));
    }
    
    public boolean hasInput() {
        return false;
    }
    
    private class ApplyPin extends JRLibPin {
        public ApplyPin(JRLibNode node) {
            super(node);
        }

        @Override
        public ApplyWidget createPinWidget(JRLibScene scene, JRLibWidgetUI ui) {
            return new ApplyWidget(scene, ui, this);
        }
    }
    
    private class ApplyWidget extends EmptyPinWidget {

        private ApplyWidget(JRLibScene scene, JRLibWidgetUI ui, JRLibPin pin) {
            super(scene, ui, pin);
        }
        
        @Override
        protected Widget getContentWidget(JRLibScene scene) {
            return new CheckboxWidget(scene, "Apply Smoothing");
        }
    }
    
    private class DevelopmentPin extends PeriodPin {
        public DevelopmentPin(JRLibNode node) {
            super(node, 1, 8, 2);
        }

        @Override
        public boolean hasInput() {
            return true;
        }
    }
    
    private class AccidentPin extends JRLibPin {

        public AccidentPin(JRLibNode node) {
            super(node);
        }

        @Override
        public AccidentWidget createPinWidget(JRLibScene scene, JRLibWidgetUI ui) {
            return new AccidentWidget(scene, ui, this);
        }

        @Override
        public boolean hasInput() {
            return true;
        }
    }
    
    private class AccidentWidget extends EmptyPinWidget {

        private AccidentWidget(JRLibScene scene, JRLibWidgetUI ui, JRLibPin pin) {
            super(scene, ui, pin);
        }
        
        @Override
        protected Widget getContentWidget(JRLibScene scene) {
            MonthDateSpinnerModel model = new MonthDateSpinnerModel(1999, 1);
            return new SpinnerWidget(scene, model, model);
        }
    }
}
