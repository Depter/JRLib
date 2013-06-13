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

package org.jreserve.dummy.claimtriangle.edtior.ribbon;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.JButton;
import org.jreserve.gui.flamingo.api.ResizableIcons;
import org.openide.util.ImageUtilities;
import org.pushingpixels.flamingo.api.common.CommandButtonDisplayState;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonPanel;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.JFlowRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonComponent;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimTrianglePane extends JRibbonBand {
    
    private final static String HOME = "org/jreserve/dummy/projecttree/resources/";
    
    public ClaimTrianglePane() {
        super("Geometry", null);
        initComponents();
    }
    
    private void initComponents() {
        startGroup();
        addRibbonComponent(new JRibbonComponent(new MonthPanel()), 2);
        startGroup();
//        addRibbonComponent(createButton("Exclude", "exclude.png"));
//        addRibbonComponent(createButton("Corrigate", "correction.png"));
//        addRibbonComponent(createButton("Smooth", "cell_smoothing.png"));
//        
//        JCommandButtonStrip strip = new JCommandButtonStrip(JCommandButtonStrip.StripOrientation.VERTICAL);
//        strip.add(new JCommandButton("Exclude", getResizableIcon("exclude.png")));
//        strip.add(new JCommandButton("Corrigate", getResizableIcon("correction.png")));
//        strip.add(new JCommandButton("Smooth", getResizableIcon("cell_smoothing.png")));
//        addRibbonComponent(new JRibbonComponent(strip), 3);
        
        RibbonElementPriority bp = RibbonElementPriority.TOP;
        addCommandButton(new JCommandButton("Exclude", getResizableIcon("exclude.png")), bp);
        addCommandButton(new JCommandButton("Corrigate", getResizableIcon("correction.png")), bp);
        addCommandButton(new JCommandButton("Smooth", getResizableIcon("cell_smoothing.png")), bp);
        
//        addFlowComponent(new JRibbonComponent(new MonthPanel()));
//        addFlowComponent(new JCommandButton("Exclude", getIcon("exclude.png")));
//        addFlowComponent(new JCommandButton("Corrigate", getIcon("correction.png")));
//        addFlowComponent(new JCommandButton("Smooth", getIcon("cell_smoothing.png")));
//        
//        JCommandButtonStrip strip = new JCommandButtonStrip(JCommandButtonStrip.StripOrientation.VERTICAL);
//        strip.add(new JCommandButton("Exclude", getIcon("exclude.png")));
//        strip.add(new JCommandButton("Corrigate", getIcon("correction.png")));
//        strip.add(new JCommandButton("Smooth", getIcon("cell_smoothing.png")));
//        addFlowComponent(strip);
//        
//        JCommandButtonPanel panel = new JCommandButtonPanel(CommandButtonDisplayState.TILE);
//        panel.setLayoutKind(JCommandButtonPanel.LayoutKind.ROW_FILL);
//        panel.add(new JCommandButton("Exclude", getIcon("exclude.png")));
//        panel.add(new JCommandButton("Corrigate", getIcon("correction.png")));
//        panel.add(new JCommandButton("Smooth", getIcon("cell_smoothing.png")));
//        addFlowComponent(panel);
    }
    
    private JRibbonComponent createButton(String title, String icon) {
        return new JRibbonComponent(new JButton(title, getIcon(icon)));
    }
    
    private static Icon getIcon(String name) {
        return ImageUtilities.loadImageIcon(HOME+name, false);
    }
    
    private static ResizableIcon getResizableIcon(String name) {
        return ResizableIcons.fromResource(HOME+name);
    }
    
}
