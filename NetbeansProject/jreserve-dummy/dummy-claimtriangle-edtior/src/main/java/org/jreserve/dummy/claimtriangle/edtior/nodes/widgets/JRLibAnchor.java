/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.dummy.claimtriangle.edtior.nodes.widgets;

import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
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
