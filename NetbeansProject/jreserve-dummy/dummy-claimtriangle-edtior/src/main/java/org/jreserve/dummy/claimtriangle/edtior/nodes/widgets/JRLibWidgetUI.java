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

import java.awt.Color;
import java.awt.Font;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class JRLibWidgetUI {
    
    private final static Color NODE_BORDER_COLOR = new Color(50, 50, 50);
    private final static Color NODE_BG_COLOR = new Color(110, 110, 110);
    private final static Color NODE_TITLE_COLOR = Color.WHITE;
    private final static int NODE_BORDER_WIDTH = 1;
    private final static Border NODE_HEADER_BORDER = BorderFactory.createOpaqueBorder (2, 8, 2, 8);
    private final static Border NODE_BORDER = new JRLibNodeBorder(NODE_BORDER_WIDTH, NODE_BORDER_COLOR, NODE_BG_COLOR);
    
    private final static Color PIN_BG_COLOR = new Color(170, 170, 170);
    private final static Border PIN_BORDER = BorderFactory.createOpaqueBorder (2, 8, 2, 8);
    
    void initUI(JRLibNodeWidget widget) {
        widget.setBorder(NODE_BORDER);
        widget.setOpaque(false);
        initNodeHeaderUI(widget.getHeaderWidget());
        initNodePinSeparatorUI(widget.getPinSeparator());
    }
    
    private void initNodeHeaderUI(Widget header) {
        header.setBorder(NODE_HEADER_BORDER);
        header.setBackground(NODE_BORDER_COLOR);
        header.setOpaque(false);
    }
    
    private void initNodePinSeparatorUI(Widget separator) {
        separator.setForeground(NODE_BORDER_COLOR);
    }
    
    Font getHeaderFont(Scene scene) {
        return scene.getDefaultFont().deriveFont(Font.BOLD);
    }
    
    Color getHeaderTitleColor() {
        return NODE_TITLE_COLOR;
    }
    
    Font getPinFont(Scene scene) {
        Font scenFont = scene.getDefaultFont();
        return scenFont.deriveFont(scenFont.getStyle(), scenFont.getSize()-1);
    }
    
    Color getPinTitleColor() {
        return NODE_TITLE_COLOR;
    }
    
    void initUI(JRLibPinWidget widget) {
        widget.setBorder(PIN_BORDER);
        widget.setBackground(PIN_BG_COLOR);
        widget.setOpaque(true);
    }
}
