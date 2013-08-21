package org.jreserve.opaquenimbus;

import javax.swing.*;

/**
 */
class SliderSliderTrackArrowShapeState extends State {
    SliderSliderTrackArrowShapeState() {
        super("ArrowShape");
    }

    @Override protected boolean isInState(JComponent c) {
 return c.getClientProperty("Slider.paintThumbArrowShape") == Boolean.TRUE; 
    }
}

