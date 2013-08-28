/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.decsi.test.scenetest.scene.JRLibNode;
import org.decsi.test.scenetest.scene.JRLibPin;
import org.decsi.test.scenetest.scene.JRLibScene;
import org.decsi.test.scenetest.widgets.ButtonWidget;
import org.decsi.test.scenetest.widgets.EmptyPinWidget;
import org.decsi.test.scenetest.widgets.JRLibLabelWidget;
import org.decsi.test.scenetest.widgets.JRLibWidgetUI;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
class DataSourceNode extends JRLibNode {
    
    private final static String IMG_PATH = "org/decsi/test/scenetest/database.png";
    
    public DataSourceNode() {
        super("DataSource", ImageUtilities.loadImage(IMG_PATH));
        super.addPin(new JRLibPin(this));
        super.addPin(new JRLibPin.Label(this, "Source:"));
        super.addPin(new DataSourcePin(this));
    }

    @Override
    public boolean hasInput() {
        return false;
    }
    
    private class DataSourcePin extends JRLibPin {

        public DataSourcePin(JRLibNode node) {
            super(node);
        }

        @Override
        public EmptyPinWidget createPinWidget(JRLibScene scene, JRLibWidgetUI ui) {
            return new DataSourceWidget(scene, ui, this);
        }
    }
    
    private class DataSourceWidget extends EmptyPinWidget {
        
        private LabelWidget textWidget;
        private ButtonWidget button;
        
        private DataSourceWidget(JRLibScene scene, JRLibWidgetUI ui, JRLibPin pin) {
            super(scene, ui, pin);
        }

        @Override
        protected Widget getContentWidget(JRLibScene scene) {
            Widget panel = new Widget(scene);
            panel.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 5));
            
            textWidget = new JRLibLabelWidget(scene, "Paid");
            textWidget.setToolTipText("Data/MD/Paid");
            panel.addChild(textWidget, 1);
            
            button = new ButtonWidget(scene, "...");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectDataSource();
                }
            });
            panel.addChild(button, 0);
            
            return panel;
        }
        
        private void selectDataSource() {
            System.out.println("Select Data Source");
        }
        
    }
}
