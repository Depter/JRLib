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
package org.jreserve.gui.misc.expandable.view;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import org.jreserve.gui.misc.expandable.ExpandableElementDescription;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExpandableScrollHandler extends ExpandablePanelHandler {
    private final static int SCROLL_INCREMENT = 20;
    
    private JComponent viewComponent;
    private JScrollPane scroll;
    
    public ExpandableScrollHandler(ExpandableElementDescription[] elements) {
        super(elements);
    }

    @Override
    public void navigateTo(ExpandableElementDescription description) {
        JComponent component = getComponentFor(description);
        Rectangle bounds = component.getBounds(null);
//        bounds.height = scroll.getViewport().getViewSize().height;
        viewComponent.scrollRectToVisible(bounds);
    }
    
    @Override
    protected JComponent createComponent() {
        viewComponent = super.createComponent();
        
        scroll = new JScrollPane(viewComponent);
        scroll.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
        scroll.getVerticalScrollBar().setBlockIncrement(SCROLL_INCREMENT);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        scroll.addComponentListener(new ScrollAdapter(scroll, viewComponent));
        return scroll;
    }

    private static class ScrollAdapter extends ComponentAdapter {
        
        private JScrollPane scroll;
        private Component component;
        private boolean firsCall = true;
        
        ScrollAdapter(JScrollPane scroll, Component component) {
            this.scroll = scroll;
            this.component = component;
        }
        
        @Override
        public void componentResized(ComponentEvent e) {
            if(firsCall)
                firsCall = false;
            else
                resizeContent();
        }
        
        private void resizeContent() {
            JScrollBar scrollBar = scroll.getVerticalScrollBar();
            int width = scroll.getWidth() - (scrollBar.isShowing()? scrollBar.getWidth() : 0);
            component.setSize(width, component.getHeight());
        }
    }
}
