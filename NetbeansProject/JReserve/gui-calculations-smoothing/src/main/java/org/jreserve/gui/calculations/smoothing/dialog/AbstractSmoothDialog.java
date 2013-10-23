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
package org.jreserve.gui.calculations.smoothing.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.localesettings.LocaleSettings;
import org.jreserve.gui.localesettings.ScaleSpinner;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.plot.ChartWrapper;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.triangle.Triangle;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.AbstractSmoothDialog.Tab.Table=Table",
    "LBL.AbstractSmoothDialog.Tab.Plot=Plot",
    "LBL.AbstractSmoothDialog.Paramtersr=Parameters",
    "LBL.AbstractSmoothDialog.Scale=Scale:",
    "LBL.AbstractSmoothDialog.Cancel=Cancel",
    "LBL.AbstractSmoothDialog.Ok=ok",
    "LBL.AbstractSmoothDialog.Help=Help"
})
public class AbstractSmoothDialog<C extends CalculationData> extends JPanel {
    
    private final static boolean MODAL = true;
    private final static Dimension TABS_SIZE = new Dimension(400, 400);
    private final static Dimension TAB_PANEL_SIZE = new Dimension(100, 100);
    private final static Dimension PLOT_SIZE = new Dimension(300, 300);
    
    public static <C extends CalculationData> CalculationModifier<C> createModifier(SmoothDialogController<C> controller) {
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
    private JButton helpButton;
    private SmoothTableModel tableModel;
    private JTable table;
    private JScrollPane plotScroll;
    private Component plotComponent;
    private ScaleSpinner scaleSpinner;
    private List<SmoothRecord> records;
    private boolean myChnage = false;
    private Dialog dialog;
    private CalculationModifier<C> modifier = null;
    private LocaleSettings.DecimalFormatter df;
    
    public AbstractSmoothDialog(SmoothDialogController<C> controller) {
        this.controller = controller;
        initComponents();
        this.controller.addChangeListener(new ControllerListener());
        this.okButton.setEnabled(controller.isValid());
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(createParameterContainer(), BorderLayout.NORTH);
        add(createInfoPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createParameterContainer() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(Bundle.LBL_AbstractSmoothDialog_Paramtersr()));
        panel.add(controller.getParameterComponent());
        return panel;
    }
    
    private JTabbedPane createInfoPanel() {
        JTabbedPane tabs = new JTabbedPane();
        
        records = controller.getRecords();
        controller.updateRecords(records);
        tabs.addTab(Bundle.LBL_AbstractSmoothDialog_Tab_Table(), createTablePanel());
        
        ChartWrapper wrapper = SmoothChartUtil.createChart(records);
        plotComponent = wrapper.getChartComponent();
        plotComponent.setPreferredSize(PLOT_SIZE);
        plotScroll = new JScrollPane(plotComponent);
        plotScroll.setPreferredSize(TAB_PANEL_SIZE);
        tabs.addTab(Bundle.LBL_AbstractSmoothDialog_Tab_Plot(), plotScroll);
        
        tabs.setPreferredSize(TABS_SIZE);
        return tabs;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0;
        gc.anchor = GridBagConstraints.BASELINE_LEADING;
        gc.insets = new Insets(0, 0, 5, 5);
        panel.add(new JLabel(Bundle.LBL_AbstractSmoothDialog_Scale()), gc);
        
        scaleSpinner = new ScaleSpinner();
        scaleSpinner.addChangeListener(new ScaleListener());
        df = LocaleSettings.createDecimalFormatter();
        gc.gridx=1;
        gc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gc.insets = new Insets(0, 0, 5, 0);
        panel.add(scaleSpinner, gc);
        
        gc.gridx=2; gc.weightx=1d;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(0, 0, 0, 0);
        panel.add(Box.createHorizontalGlue(), gc);
        
        tableModel = new SmoothTableModel(records);
        tableModel.addTableModelListener(new TableListener());
        table = new JTable(tableModel);
        table.setDefaultRenderer(Double.class, new DoubleRenderer());
        table.setDefaultRenderer(Boolean.class, new BooleanRenderer());
        table.addMouseListener(new AppliedListener());
        gc.gridx=0; gc.gridy=1; 
        gc.weighty=1d; gc.gridwidth=3;
        gc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(table), gc);
        
        panel.setPreferredSize(TAB_PANEL_SIZE);
        return panel;
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
        okButton = new JButton(Bundle.LBL_AbstractSmoothDialog_Ok(), CommonIcons.ok());
        okButton.addActionListener(bl);
        gc.gridx = 1;
        gc.weightx = 0d;
        gc.insets = new Insets(0, 0, 0, 5);
        panel.add(okButton, gc);
        
        cancelButton = new JButton(Bundle.LBL_AbstractSmoothDialog_Cancel(), CommonIcons.cancel());
        cancelButton.addActionListener(bl);
        gc.gridx = 2;
        panel.add(cancelButton, gc);
        
        helpButton = new JButton(Bundle.LBL_AbstractSmoothDialog_Help(), CommonIcons.help());
        helpButton.addActionListener(bl);
        gc.gridx=3;
        gc.insets = new Insets(0, 0, 0, 0);
        panel.add(helpButton, gc);
        
        return panel;
    }
    
    private void recalculateRecords() {
        if(!myChnage && records.size() > 0) {
            myChnage = true;
            controller.updateRecords(records);
            
            tableModel.fireTableRowsUpdated(0, records.size()-1);
            
            plotComponent = SmoothChartUtil.createChart(records).getChartComponent();
            plotComponent.setPreferredSize(PLOT_SIZE);
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
    
    private class ScaleListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            df.setDecimalCount((Integer) scaleSpinner.getValue());
            tableModel.fireTableDataChanged();
        }
    }
    
    private class DoubleRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(value instanceof Double)
                value = df.format(((Double)value).doubleValue());
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    
    private class BooleanRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
            setIcon(getIcon(value));
            setHorizontalAlignment(SwingConstants.CENTER);
            return this;
        }
        
        private Icon getIcon(Object value) {
            if(value instanceof Boolean)
                return ((Boolean)value).booleanValue()?
                        CommonIcons.ok() : CommonIcons.cancel();
            return CommonIcons.cancel();
        }
    }
    
    private class AppliedListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(!controller.canModifyCells())
                return;
            
            Point p = e.getPoint();
            int row = table.rowAtPoint(p);
            int column = table.columnAtPoint(p);
            if(row < 0 || SmoothTableModel.COLUMN_APPLY != column)
                return;
            
            Boolean v = (Boolean) tableModel.getValueAt(row, column);
            if(v==null || !v.booleanValue())
                tableModel.setValueAt(true, row, column);
            else
                tableModel.setValueAt(false, row, column);
        }
        
    } 
    
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if(source == helpButton) {
                controller.getHelpContext().display();
            } else if(source == cancelButton) {
                closeDialog();
            } else {
                modifier = controller.createModifier();
                closeDialog();
            }
        }
    }
}
