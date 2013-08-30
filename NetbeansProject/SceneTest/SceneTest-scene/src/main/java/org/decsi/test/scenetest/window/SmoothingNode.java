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
import org.decsi.test.scenetest.widgets.TextBridge;
import org.decsi.test.scenetest.widgets.TextWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
public class SmoothingNode extends JRLibNode {
    private final static String IMG_PATH = "org/decsi/test/scenetest/smoothing.png";

    public SmoothingNode() {
        super("DE - Smoothing", ImageUtilities.loadImage(IMG_PATH));
        super.addPin(new JRLibPin(this));
        super.addPin(new JRLibPin.Label(this, "Alpha:"));
        super.addPin(new ValuePin(this, 0.6));
        super.addPin(new JRLibPin.Label(this, "Beta:"));
        super.addPin(new ValuePin(this, 0.3));
        super.addPin(new CellsPin(this));
    }
    
    private class CellsPin extends JRLibPin {
        
        public CellsPin(JRLibNode node) {
            super(node);
        }

        @Override
        public CellsWidget createPinWidget(JRLibScene scene, JRLibWidgetUI ui) {
            return new CellsWidget(scene, ui, this);
        }
        
        public boolean hasInput() {
            return true;
        }
    }
    
    private class CellsWidget extends EmptyPinWidget {

        private CellsWidget(JRLibScene scene, JRLibWidgetUI ui, JRLibPin pin) {
            super(scene, ui, pin);
        }
        
        @Override
        protected Widget getContentWidget(JRLibScene scene) {
            return new LabelWidget(scene, "Cells");
        }
    }
    
    
    private class ValuePin extends JRLibPin {
        
        private double value;
        
        public ValuePin(JRLibNode node, double value) {
            super(node);
            this.value = value;
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
            double v = ((ValuePin)super.getPin()).value;
            return new TextWidget<Double>(scene, new DoubleBridge(), v);
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
                double v = Double.parseDouble(str);
                if(v < 0d || v > 1d)
                    throw new Exception();
                return v;
            } catch (Exception ex) {
                throw new IllegalArgumentException("Illegal double!" + str);
            }
        }
    }

}
