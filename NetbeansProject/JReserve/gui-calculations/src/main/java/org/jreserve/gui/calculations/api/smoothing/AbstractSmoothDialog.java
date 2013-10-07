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
package org.jreserve.gui.calculations.api.smoothing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.plot.ChartWrapper;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractSmoothDialog<C extends CalculationModifier> extends JPanel {
    
    private final static boolean MODAL = true;
    
    public static <C extends CalculationModifier> C createModifier(SmoothDialogController<C> controller) {
        AbstractSmoothDialog<C> content = new AbstractSmoothDialog<C>(controller);
        DialogDescriptor dd = new DialogDescriptor(
            content, controller.getDialogTitle(), MODAL, 
            new Object[0], null, DialogDescriptor.DEFAULT_ALIGN, 
            HelpCtx.DEFAULT_HELP, null);
        
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        content.showDialog(dialog);
        return content.modifier;
    }
    
    private SmoothDialogController<C> controller;
    private JButton okButton;
    private JButton cancelButton;
    private SmoothTableModel tableModel;
    private JScrollPane plotScroll;
    private Component plotComponent;
    private List<SmoothRecord> records;
    private boolean myChnage = false;
    private Dialog dialog;
    private C modifier = null;
    
    public AbstractSmoothDialog(SmoothDialogController<C> controller) {
        this.controller = controller;
        initComponents();
        this.controller.addChangeListener(new ControllerListener());
        this.okButton.setEnabled(controller.isValid());
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        add(createParameterContainer(), BorderLayout.NORTH);
        add(createInfoPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createParameterContainer() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Parameters"));
        panel.add(controller.getParameterComponent());
        return panel;
    }
    
    private JTabbedPane createInfoPanel() {
        JTabbedPane tabs = new JTabbedPane();
        
        records = controller.getRecords();
        tableModel = new SmoothTableModel(records);
        tableModel.addTableModelListener(new TableListener());
        tabs.addTab("Table", new JScrollPane(new JTable(tableModel)));
        
        ChartWrapper wrapper = SmoothChartUtil.createChart(records);
        plotComponent = wrapper.getChartComponent();
        plotScroll = new JScrollPane(plotComponent);
        tabs.addTab("Plot", plotScroll);
        
        return tabs;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.BASELINE_LEADING;
        gc.weightx = 1d;
        panel.add(Box.createHorizontalGlue(), gc);
        
        ButtonListener bl = new ButtonListener();
        okButton = new JButton("Ok", CommonIcons.ok());
        okButton.addActionListener(bl);
        gc.gridx = 1;
        gc.weightx = 0d;
        gc.insets = new Insets(0, 0, 0, 5);
        panel.add(okButton, gc);
        
        cancelButton = new JButton("Cancel", CommonIcons.cancel());
        cancelButton.addActionListener(bl);
        gc.gridx = 2;
        gc.insets = new Insets(0, 0, 0, 0);
        panel.add(cancelButton, gc);
        
        return panel;
    }
    
    private void recalculateRecords() {
        if(!myChnage && records.size() > 0) {
            myChnage = true;
            controller.updateRecords(records);
            
            tableModel.fireTableRowsUpdated(0, records.size()-1);
            
            plotComponent = SmoothChartUtil.createChart(records).getChartComponent();
            plotScroll.setViewportView(plotComponent);
            myChnage = false;
        }
    }
    
    void showDialog(Dialog dialog) {
        this.dialog = dialog;
        this.dialog.setVisible(true);
    }
    
    private void closeDialog() {
        this.dialog.setVisible(false);
        this.dialog.dispose();
    }
    
    private class TableListener implements TableModelListener {
        @Override
        public void tableChanged(TableModelEvent e) {
            recalculateRecords();
        }
    }
    
    private class ControllerListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            recalculateRecords();
            okButton.setEnabled(controller.isValid());
        }
    }
    
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == cancelButton) {
                closeDialog();
            } else {
                modifier = controller.createModifier();
                closeDialog();
            }
        }
    
    }
}
