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

import org.jreserve.dummy.claimtriangle.edtior.nodes.JRLibPin;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class JRLibPinWidget extends Widget {
    
    private LabelWidget nameWidget;
    private JRLibWidgetUI ui;
    private JRLibPin pin;
    
    public JRLibPinWidget(Scene scene, JRLibWidgetUI ui, JRLibPin pin) {
        super(scene);
        this.ui = ui;
        this.pin = pin;
        initWidget(scene);
        ui.initUI(this);
    }
    
    private void initWidget(Scene scene) {
        setLayout (LayoutFactory.createHorizontalFlowLayout (LayoutFactory.SerialAlignment.CENTER, 8));
        
        addChild(initNameWidget(scene));
    }
    
    private Widget initNameWidget(Scene scene) {
        nameWidget = new LabelWidget(scene);
        nameWidget.setLabel(pin.getTitle());
        nameWidget.setFont(ui.getPinFont(scene));
        nameWidget.setForeground(ui.getPinTitleColor());
        return nameWidget;
    }
    
}
