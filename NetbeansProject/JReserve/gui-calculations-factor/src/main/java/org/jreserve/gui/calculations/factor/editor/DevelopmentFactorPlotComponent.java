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
package org.jreserve.gui.calculations.factor.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jreserve.gui.calculations.factor.FactorTriangleCalculation;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;
import org.jreserve.gui.plot.PlotLabel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DevelopmentFactorPlotComponent extends JPanel {
    
    private FactorTriangleCalculation calculation;
    private DefaultListModel listModel = new DefaultListModel();
    private JList seriesList;
    private DevelopmentFactorPlot plot;
    
    DevelopmentFactorPlotComponent(FactorTriangleCalculation calculation) {
        this.calculation = calculation;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JSplitPane split = new JSplitPane();
        split.setResizeWeight(1d);
        split.setOneTouchExpandable(true);
        
        plot = new DevelopmentFactorPlot(calculation);
        Component c = plot.getPlot();
        split.setLeftComponent(c);
        
        for(PlotLabel label : plot.getDevelopments())
            listModel.addElement(label);
        seriesList = new JList(listModel);
        seriesList.setCellRenderer(new ListRenderer());
        seriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        seriesList.addListSelectionListener(new SeriesListener());
        if(!listModel.isEmpty())
            seriesList.setSelectedIndex(0);
        split.setRightComponent(new JScrollPane(seriesList));
        
        add(split, BorderLayout.CENTER);
    }
    
    void recalculate() {
        int index = seriesList.getSelectedIndex();
        plot.recalculate();
        
        listModel.removeAllElements();
        for(PlotLabel label : plot.getDevelopments())
            listModel.addElement(label);
        
        if(!listModel.isEmpty()) {
            if(index >= 0 && listModel.size() > index)
                seriesList.setSelectedIndex(index);
            else
                seriesList.setSelectedIndex(0);
        }
    }
    
    ClipboardUtil.Copiable createCopiable() {
        return plot.createCopiable();
    }
    
    private class SeriesListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int index = seriesList.getSelectedIndex();
            if(index >= 0)
                plot.showSeries(index);
        }
    }
    
    private class ListRenderer extends JRadioButton implements ListCellRenderer {
        
        private ListRenderer() {
            Border border = super.getBorder();
            Border margin = BorderFactory.createEmptyBorder(0, 2, 0, 4);
            if(border == null) {
                border = margin;
            } else {
                border = BorderFactory.createCompoundBorder(margin, border);
            }
            setBorder(border);
        }
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if(value instanceof PlotLabel)
                setText(((PlotLabel)value).getName());
            setSelected(isSelected);
            return this;
        }
    }
    
}
