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
            w.setSpinnerEnabled(false);
            return w;
        }
    }
    
    private class PeriodPin extends JRLibPin {

        public PeriodPin(JRLibNode node) {
            super(node);
        }

        @Override
        public PeriodPinWidget createPinWidget(JRLibScene scene, JRLibWidgetUI ui) {
            return new PeriodPinWidget(scene, ui, this);
        }
    }
    
    private class PeriodPinWidget extends EmptyPinWidget {

        public PeriodPinWidget(JRLibScene scene, JRLibWidgetUI ui, JRLibPin pin) {
            super(scene, ui, pin);
        }

        @Override
        protected Widget getContentWidget(JRLibScene scene) {
            SpinnerModel model = new SpinnerNumberModel(12, 1, Integer.MAX_VALUE, 1);
            return new SpinnerWidget(scene, model, new IntBridge());
        }
    }
    
    private static class IntBridge implements SpinnerWidget.ModelBridge {

        @Override
        public String toString(Object value) {
            return value.toString();
        }

        @Override
        public Object toValue(String str) throws IllegalArgumentException {
            try {
                return Integer.parseInt(str);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Value invalid!");
            }
        }
        
    }
    
    private static class MonthDateSpinnerModel extends AbstractSpinnerModel implements SpinnerWidget.ModelBridge {
        
        private int[] value = {1997, 1}; 
                
        
        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public void setValue(Object value) {
            this.value = (int[]) value;
            super.fireStateChanged();
        }

        @Override
        public Object getNextValue() {
            if(value[0] == 2004 && value[1] == 12)
                return null;
            if(value[1] == 12) {
                return new int[]{value[0]+1, 1}; 
            }
            return new int[]{value[0], value[1]+1};
        }

        @Override
        public Object getPreviousValue() {
            if(value[0] == 1997 && value[1] == 1)
                return null;
            if(value[1] == 1) {
                return new int[]{value[0]-1, 12}; 
            }
            return new int[]{value[0], value[1]-1};
        }

        @Override
        public String toString(Object value) {
            int[] arr = (int[]) value;
            if(arr[1] < 10)
                return ""+arr[0]+"-0"+arr[1];
            return ""+arr[0]+"-"+arr[1];
        }

        @Override
        public Object toValue(String str) throws IllegalArgumentException {
            try {
                if(str==null || str.length()==0)
                    return new int[]{1997, 1};
                
                return new int[]{
                    Integer.parseInt(str.substring(0, 4)),
                    Integer.parseInt(str.substring(5))
                };
            } catch (Exception ex) {
                throw new IllegalArgumentException("Value invalid!");
            }
        }
    }
}
