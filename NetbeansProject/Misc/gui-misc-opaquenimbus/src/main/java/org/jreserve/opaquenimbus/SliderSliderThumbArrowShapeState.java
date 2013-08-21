package org.jreserve.opaquenimbus;

import javax.swing.*;

/**
 */
class SliderSliderThumbArrowShapeState extends State {
    SliderSliderThumbArrowShapeState() {
        super("ArrowShape");
    }

    @Override protected boolean isInState(JComponent c) {
 return c.getClientProperty("Slider.paintThumbArrowShape") == Boolean.TRUE; 
    }
}

