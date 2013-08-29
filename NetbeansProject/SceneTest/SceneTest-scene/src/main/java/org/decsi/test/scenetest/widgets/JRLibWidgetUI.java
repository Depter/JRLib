/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import java.awt.Color;
import java.awt.Font;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 */
public class JRLibWidgetUI {
 
    private final static Color INPUT_ANCHOR_COLOR = new Color(90, 190, 90);
    private final static Color OUTPUT_ANCHOR_COLOR = new Color(240, 210, 40);
    private final static Color NODE_BORDER_COLOR = new Color(50, 50, 50);
    private final static Color NODE_BG_COLOR = new Color(110, 110, 110);
    private final static Color NODE_TITLE_COLOR = Color.WHITE;
    
    private final static int NODE_BORDER_WIDTH = 1;
    private final static Border NODE_HEADER_BORDER = BorderFactory.createOpaqueBorder (2, 8, 2, 8);
    
    private final static Border NODE_BORDER = new JRLibNodeBorder(NODE_BORDER_WIDTH, NODE_BORDER_COLOR, NODE_BG_COLOR);
    
    private final static Color PIN_BG_COLOR = new Color(210, 210, 210);
    private final static Border PIN_BORDER = BorderFactory.createOpaqueBorder (2, 8, 2, 8);
    private final static Color PIN_FG_COLOR = Color.BLACK;
    
    private final static Color CONTROL_BORDER_COLOR = Color.BLACK;
    private final static Color CONTROL_BORDER_HOVER_COLOR = Color.BLUE;
    private final static Color CONTROL_BORDER_DISABLED_COLOR = new Color(150, 150, 150);
    private final static Color CONTROL_TEXT_COLOR = Color.BLACK;
    private final static Color CONTROL_TEXT_DISABLED_COLOR = new Color(150, 150, 150);
    
    private final static Color TEXT_ENABLED_BG = Color.WHITE;
    private final static Color TEXT_DISABLED_BG = new Color(210, 210, 210);
    
    Color getTextEnabledBg() {
        return TEXT_ENABLED_BG;
    }
    
    Color getTextDisabledBg() {
        return TEXT_DISABLED_BG;
    }
    
    Color getControlBorderColor() {
        return CONTROL_BORDER_COLOR;
    }
    
    Color getControlBorderHoverColor() {
        return CONTROL_BORDER_HOVER_COLOR;
    }
    
    Color getControlBorderDisabledColor() {
        return CONTROL_BORDER_DISABLED_COLOR;
    }
    
    Color getControlForeground() {
        return CONTROL_TEXT_COLOR;
    }
    
    Color getControlDisabledForeground() {
        return CONTROL_TEXT_DISABLED_COLOR;
    }
    
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
        return PIN_FG_COLOR;
    }
    
    Color getAnchorColor(boolean isInput) {
        return isInput? getInputAnchorColor() : getOutputAnchorColor();
    }
    
    Color getInputAnchorColor() {
        return INPUT_ANCHOR_COLOR;
    }
    
    Color getOutputAnchorColor() {
        return OUTPUT_ANCHOR_COLOR;
    }
    
    void initUI(EmptyPinWidget widget) {
        widget.setBorder(PIN_BORDER);
        widget.setBackground(PIN_BG_COLOR);
        widget.setOpaque(true);
    }    
}
