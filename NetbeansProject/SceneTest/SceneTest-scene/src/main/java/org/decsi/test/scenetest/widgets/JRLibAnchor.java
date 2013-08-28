/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 */
public class JRLibAnchor extends Anchor {

    private boolean isInput;
    
    public JRLibAnchor(Widget relatedWidget, boolean isInput) {
        super(relatedWidget);
        this.isInput = isInput;
    }
    
    @Override
    public Result compute(Entry entry) {
        Rectangle bounds = getSceneBounds();
        int y = bounds.y + bounds.height/2;
        int x = isInput? bounds.x : bounds.x + bounds.width;
        Direction direction = isInput? Direction.LEFT : Direction.RIGHT;
        return new Result(new Point(x, y), direction);
    }
    
    private Rectangle getSceneBounds() {
        Widget widget = getRelatedWidget();
        Rectangle localBounds = widget.getBounds();
        return widget.convertLocalToScene(localBounds);
    }
}
