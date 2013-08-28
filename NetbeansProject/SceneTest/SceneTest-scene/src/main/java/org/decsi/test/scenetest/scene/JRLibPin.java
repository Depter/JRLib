/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.scene;

import org.decsi.test.scenetest.widgets.EmptyPinWidget;
import org.decsi.test.scenetest.widgets.JRLibWidgetUI;
import org.decsi.test.scenetest.widgets.LabelPinWidget;

/**
 *
 * @author Peter Decsi
 */
public class JRLibPin {
 
    private JRLibNode node;

    public JRLibPin(JRLibNode node) {
        this.node = node;
    }

    public JRLibNode getNode() {
        return node;
    }
    
    public boolean hasInput() {
        return false;
    }
    
    public boolean hasOutput() {
        return false;
    }
    
    public EmptyPinWidget createPinWidget(JRLibScene scene, JRLibWidgetUI ui) {
        return new EmptyPinWidget(scene, ui, this);
    }
    
    @Override
    public String toString() {
        return String.format("JRLibPin [%s]", node);
    }  
    
    public static class Label extends JRLibPin {
        
        private String label;

        public Label(JRLibNode node, String label) {
            super(node);
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
        
        public LabelPinWidget createPinWidget(JRLibScene scene, JRLibWidgetUI ui) {
            return new LabelPinWidget(scene, ui, this, label);
        }
        
    }
}
