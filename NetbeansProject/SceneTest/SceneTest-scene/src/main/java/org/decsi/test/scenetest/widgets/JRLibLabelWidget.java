/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import java.awt.Color;
import java.awt.Dimension;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class JRLibLabelWidget extends LabelWidget {

    public JRLibLabelWidget(Scene scene) {
        super(scene);
        initLabel();
    }

    public JRLibLabelWidget(Scene scene, String label) {
        super(scene, label);
        initLabel();
    }
    
    private void initLabel() {
        setOpaque(true);
//        setPreferredSize(new Dimension(16, 16));
        setBorder(BorderFactory.createCompositeBorder(
            BorderFactory.createLineBorder(1, Color.BLACK),
            BorderFactory.createEmptyBorder(1)
        ));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
    }
}
