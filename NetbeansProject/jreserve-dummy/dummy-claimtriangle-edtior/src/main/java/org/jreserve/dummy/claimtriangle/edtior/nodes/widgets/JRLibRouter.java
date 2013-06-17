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
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.widget.ConnectionWidget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class JRLibRouter implements Router {
    
    @Override
    public List<Point> routeConnection(ConnectionWidget widget) {
        ArrayList<Point> points = new ArrayList<Point> ();

        Anchor source = widget.getSourceAnchor ();
        Anchor target = widget.getTargetAnchor ();
        if(source != null && target != null)
            fillPoints(points, 
                  source.compute(widget.getSourceAnchorEntry()).getAnchorSceneLocation(), 
                  target.compute(widget.getTargetAnchorEntry()).getAnchorSceneLocation());
        
        return points;
    }
    
    private void fillPoints(List<Point> points, Point source, Point target) {
        points.add(source);
        
        int sourceX = source.x;
        int sourceY = source.y;
        int stepCount = getStepCount(source.x, target.x)-1;
        int xPosStep = (target.x - source.x) / stepCount;
        double xStep = 10d / (double) stepCount;
        int yGap = target.y - source.y;

        for(int i=1; i<stepCount; i++) {
            double x = -5d + i * xStep;
            int xPos = sourceX + i*xPosStep;
            int yPos = sourceY + (int)(yGap * getYScale(x));
            points.add(new Point(xPos, yPos));
        }
        
        points.add(target);
    }
    
    private int getStepCount(int x0, int x1) {
        int gap = Math.abs(x0-x1);
        if(gap < 2) return 2;
        else if(gap < 10) return gap;
        else if(gap <= 50) return 10;
        else return gap / 5;
    }
    
    private double getYScale(double x) {
        return 1d / (1d + 1d/Math.exp(x));
    }
}
