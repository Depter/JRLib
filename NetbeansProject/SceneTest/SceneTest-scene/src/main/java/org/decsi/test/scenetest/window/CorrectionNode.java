/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.window;

import org.decsi.test.scenetest.scene.JRLibNode;
import org.decsi.test.scenetest.scene.JRLibPin;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.decsi.test.scenetest.widgets.EmptyPinWidget;
import org.decsi.test.scenetest.widgets.JRLibWidgetUI;
import org.decsi.test.scenetest.widgets.SpinnerWidget;
import org.decsi.test.scenetest.widgets.TextBridge;
import org.decsi.test.scenetest.widgets.TextWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CorrectionNode extends JRLibNode {
    private final static String IMG_PATH = "org/decsi/test/scenetest/correction.png";

    public CorrectionNode() {
        super("Correction", ImageUtilities.loadImage(IMG_PATH));
        super.addPin(new JRLibPin(this));
        super.addPin(new JRLibPin.Label(this, "Accident:"));
        super.addPin(new AccidentPin(this));
        super.addPin(new JRLibPin.Label(this, "Development:"));
        super.addPin(new DevelopmentPin(this));
        super.addPin(new JRLibPin.Label(this, "Value:"));
        super.addPin(new ValuePin(this));
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
    
    private class ValuePin extends JRLibPin {

        public ValuePin(JRLibNode node) {
            super(node);
        }

        @Override
        public ValueWidget createPinWidget(JRLibScene scene, JRLibWidgetUI ui) {
            return new ValueWidget(scene, ui, this);
        }
    }
    
    private class ValueWidget extends EmptyPinWidget {

        private ValueWidget(JRLibScene scene, JRLibWidgetUI ui, JRLibPin pin) {
            super(scene, ui, pin);
        }
        
        @Override
        protected Widget getContentWidget(JRLibScene scene) {
            return new TextWidget<Double>(scene, new DoubleBridge(), 500.45);
        }
    }
    
    private class DoubleBridge implements TextBridge<Double> {

        @Override
        public String toString(Double value) {
            return value==null? null : value.toString();
        }

        @Override
        public Double toValue(String str) throws IllegalArgumentException {
            if(str == null || str.length()==0)
                return null;
            try {
                return Double.parseDouble(str);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Illegal double!" + str);
            }
        }
    }
}
