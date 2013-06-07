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

package org.jreserve.gui.triangletable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import org.jreserve.gui.triangletable.trianglemodel.DevelopmentTriangleModel;
import org.jreserve.gui.triangletable.trianglemodel.TriangleModel;
import org.jreserve.jrlib.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleTable extends JScrollPane {
    
    private static Triangle getTriangle(List<TriangleLayer> layers) {
        if(layers==null || layers.isEmpty())
            return null;
        return layers.get(layers.size()-1).getTriangle();
    }

    private TriangleTableModel model = new TriangleTableModel();
    private TriangleTableRenderer renderer = new TriangleTableRenderer();
    private List<TriangleLayer> layers;
    
    private JTable table;
    
    public TriangleTable() {
        this(Collections.EMPTY_LIST);
    }
    
    public TriangleTable(List<TriangleLayer> layers) {
        this(layers, new DevelopmentTriangleModel(getTriangle(layers)));
    }
    
    public TriangleTable(List<TriangleLayer> layers, TriangleModel model) {
        this(layers, model, new IndexTitleModel(), new IndexTitleModel());
    }
    
    public TriangleTable(List<TriangleLayer> layers, TriangleModel model, TitleModel xAxis, TitleModel yAxis) {
        initComponents();
        
        renderer.setTriangleModel(model);
        this.model.setTriangleModel(model);
        this.model.setXAxisModel(xAxis);
        this.model.setYAxisModel(yAxis);
    }
    
    public void setModel(TriangleModel model) {
        renderer.setTriangleModel(model);
        this.model.setTriangleModel(model);
    }
    
    public void setXAxisModel(TitleModel model) {
        this.model.setXAxisModel(model);
    }
    
    public void setYAxisModel(TitleModel model) {
        this.model.setYAxisModel(model);
    }
    
    public void setLayers(List<TriangleLayer> layers) {
        this.layers = new ArrayList<TriangleLayer>(layers==null? Collections.EMPTY_LIST : layers);
        renderer.setLayers(this.layers);
        model.setTriangle(getTriangle(layers));
    }
    
    private void initComponents() {
        table = new JTable(model);
        renderer.setHeaderRenderer(table.getTableHeader().getDefaultRenderer());
        table.setDefaultRenderer(String.class, renderer);
        table.setDefaultRenderer(Double.class, renderer);

        setViewportView(table);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        //setPreferredSize(table.getPreferredSize());
    }
}
