/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.window;

import javax.swing.SpinnerNumberModel;
import org.decsi.test.scenetest.scene.JRLibNode;
import org.decsi.test.scenetest.scene.JRLibPin;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.decsi.test.scenetest.widgets.EmptyPinWidget;
import org.decsi.test.scenetest.widgets.JRLibWidgetUI;
import org.decsi.test.scenetest.widgets.SpinnerWidget;
import org.decsi.test.scenetest.widgets.TextBridge;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 */
class PeriodPin extends JRLibPin {

    private SpinnerNumberModel model;
    
    public PeriodPin(JRLibNode node) {
        this(node, 1, Integer.MAX_VALUE, 1);
    }

    public PeriodPin(JRLibNode node, int start,  int end, int value) {
        super(node);
        model = new SpinnerNumberModel(value, start, end, 1);
    }

    @Override
    public PeriodPinWidget createPinWidget(JRLibScene scene, JRLibWidgetUI ui) {
        return new PeriodPinWidget(scene, ui, this);
    }
    
    
    private class PeriodPinWidget extends EmptyPinWidget {

        public PeriodPinWidget(JRLibScene scene, JRLibWidgetUI ui, JRLibPin pin) {
            super(scene, ui, pin);
        }

        @Override
        protected Widget getContentWidget(JRLibScene scene) {
            return new SpinnerWidget(scene, model, new IntBridge());
        }
    }
    
    private class IntBridge implements TextBridge {

        @Override
        public String toString(Object value) {
            return value.toString();
        }

        @Override
        public Object toValue(String str) throws IllegalArgumentException {
            try {
                Integer value = Integer.parseInt(str);
                checkValue(value);
                return value;
            } catch (Exception ex) {
                throw new IllegalArgumentException("Value invalid!");
            }
        }
        
        private void checkValue(Integer value) throws Exception {
            Comparable min = model.getMinimum();
            if(min!=null && min.compareTo(value)>0)
                throw new Exception();
            Comparable max = model.getMaximum();
            if(max!=null && max.compareTo(value)<0)
                throw new Exception();
        }
        
    }
}
