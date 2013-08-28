/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.scene;

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
class JRLibSceneRouter implements Router {
    
    @Override
    public List<Point> routeConnection(ConnectionWidget widget) {
        List<Point> points = new ArrayList<Point>();

        Anchor source = widget.getSourceAnchor();
        Anchor target = widget.getTargetAnchor();
        if(source != null && target != null) {
            Point sl = source.compute(widget.getSourceAnchorEntry()).getAnchorSceneLocation();
            Point tl = target.compute(widget.getTargetAnchorEntry()).getAnchorSceneLocation();
            fillPoints(points, sl, tl);
        }
        
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
