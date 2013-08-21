package org.jreserve.opaquenimbus;

import javax.swing.*;

/**
 */
class SliderArrowShapeState extends State {
    SliderArrowShapeState() {
        super("ArrowShape");
    }

    @Override protected boolean isInState(JComponent c) {
 return c.getClientProperty("Slider.paintThumbArrowShape") == Boolean.TRUE; 
    }
}

