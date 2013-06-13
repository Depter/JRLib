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

package org.jreserve.gui.triangletable.widget;

import java.awt.GridBagLayout;
import java.util.Collections;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.triangletable.trianglemodel.TitleModel;
import org.jreserve.gui.triangletable.TriangleLayer;
import org.jreserve.gui.triangletable.trianglemodel.DevelopmentTriangleModel;
import org.jreserve.gui.triangletable.trianglemodel.TriangleModel;
import org.jreserve.jrlib.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleWidget extends JScrollPane {
    
    final static int DEFAULT_ROW_HEIGHT = 20;
    final static int DEFAULT_COLUMN_WIDTH = 100;
    
    private List<TriangleLayer> layers;
    private TriangleModel model;
    private ModelListener modelListener = new ModelListener();
    
    //Components
    private LayerTriangleRenderer renderer;
    private WidgetContentPanel content;
    private WidgetHeader xHeader;
    private WidgetHeader yHeader;
    private WidgetCorner corner;
    
    public TriangleWidget() {
        this(Collections.EMPTY_LIST);
    }
    
    public TriangleWidget(List<TriangleLayer> layers) {
        this(layers, new DevelopmentTriangleModel());
    }
    
    public TriangleWidget(List<TriangleLayer> layers, TriangleModel model) {
        this.layers = layers;
        initNewModel(model);
        initComponents();
    }

    private void initComponents() {
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        renderer = new LayerTriangleRenderer(this);
        content = new WidgetContentPanel(this, renderer);
        xHeader = new WidgetHeader(this, WidgetHeader.X_AXIS);
        yHeader = new WidgetHeader(this, WidgetHeader.Y_AXIS);
        corner = new WidgetCorner(this);
        
        super.setViewportView(content);
        super.setColumnHeaderView(xHeader);
        super.setRowHeaderView(yHeader);
        super.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, corner);
    }
    
    public TriangleModel getModel() {
        return model;
    }
    
    public void setModel(TriangleModel model) {
        releasOldModel();
        initNewModel(model);
        resizeAndRepaint();
    }
    
    private void releasOldModel() {
        if(model != null)
            model.removeChangeListener(modelListener);
    }
    
    private void initNewModel(TriangleModel model) {
        this.model = model;
        if(model != null) {
            model.setTriangle(getTriangle());
            model.addChangeListener(modelListener);
        }
    }
    
    private Triangle getTriangle() {
        if(layers==null || layers.isEmpty())
            return null;
        return layers.get(layers.size()-1).getTriangle();
    }
    
    public List<TriangleLayer> getLayers() {
        return layers;
    }
    
    public void setLayers(List<TriangleLayer> layers) {
        this.layers = layers;
        this.model.setTriangle(getTriangle());
    }
    
    public TitleModel getHorizontalTitleModel() {
        return model.getHorizontalTitleModel();
    }
    
    public TitleModel getVerticalTitleModel() {
        return model.getVerticalTitleModel();
    }
    
    public void setScale(int scale) {
        content.setScale(scale);
        resizeAndRepaint();
    }
    
    public int getScale() {
        return content.getScale();
    }
    
    public void setCummulated(boolean cummulated) {
        content.setCummualted(cummulated);
        resizeAndRepaint();
    }
    
    public boolean isCummulated() {
        return content.isCummualted();
    }
    
    public int getRowCount() {
        return model==null? 0 : model.getRowCount();
    }
    
    public int getColumnCount() {
        return model==null? 0 : model.getColumnCount();
    }
    
    WidgetContentPanel getContentPanel() {
        return content;
    }
    
    WidgetHeader getHorizontalHeader() {
        return xHeader;
    }
    
    WidgetHeader getVerticalHeader() {
        return yHeader;
    }
    
    private void resizeAndRepaint() {
        revalidate();
        repaint();
    }
    
    private class ModelListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            resizeAndRepaint();
        }
        
    }
}
