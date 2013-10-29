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
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
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
class AccidentFactorPlotComponent extends JPanel {
    
    private FactorTriangleCalculation calculation;
    private DefaultListModel listModel = new DefaultListModel();
    private JList accidentList;
    private AccidentFactorPlot plot;
    
    AccidentFactorPlotComponent(FactorTriangleCalculation calculation) {
        this.calculation = calculation;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JSplitPane split = new JSplitPane();
        split.setResizeWeight(1d);
        split.setOneTouchExpandable(true);
        
        plot = new AccidentFactorPlot(calculation);
        split.setRightComponent(plot.getPlot());
        
        for(PlotLabel label : plot.getAccidents())
            listModel.addElement(label);
        accidentList = new JList(listModel);
        accidentList.setCellRenderer(new ListRenderer());
        if(!listModel.isEmpty()) {
            ListSelectionModel sModel = accidentList.getSelectionModel();
            sModel.addSelectionInterval(0, listModel.getSize());
        }
        accidentList.addListSelectionListener(new SeriesListener());
        split.setRightComponent(new JScrollPane(accidentList));
        
        add(split, BorderLayout.CENTER);
    }
    
    void recalculate() {
        plot.recalculate();
        
        listModel.removeAllElements();
        for(PlotLabel label : plot.getAccidents())
            listModel.addElement(label);
        
        if(!listModel.isEmpty()) {
            ListSelectionModel sModel = accidentList.getSelectionModel();
            sModel.addSelectionInterval(0, listModel.getSize());
        }
    }
    
    ClipboardUtil.Copiable createCopiable() {
        return plot.createCopiable();
    }
    
    private class SeriesListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(!e.getValueIsAdjusting()) {
                int size = listModel.getSize();
                for(int i=0; i<size; i++)
                    plot.setSeriesVisible(i, accidentList.isSelectedIndex(i));
            }
        }
    }
    
    private class ListRenderer extends JCheckBox implements ListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if(value instanceof PlotLabel)
                setText(((PlotLabel)value).getName());
            setSelected(isSelected);
            return this;
        }
    }
}
