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
package org.jreserve.dummy.claimtriangle.edtior;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.triangletable.TriangleLayer;
import org.jreserve.gui.triangletable.trianglemodel.AccidentTriangleModel;
import org.jreserve.gui.triangletable.trianglemodel.CalendarTriangleModel;
import org.jreserve.gui.triangletable.trianglemodel.DevelopmentTriangleModel;
import org.jreserve.gui.triangletable.trianglemodel.IndexTitleModel;
import org.jreserve.gui.triangletable.trianglemodel.TitleModel;
import org.jreserve.gui.triangletable.widget.TriangleWidget;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleEditorPanel extends JPanel {
    
    private List<TriangleLayer> layers;
    private TitleModel accidentTitle;
    private TitleModel developmentTitle;
    private TriangleWidget widget;
    
    public TriangleEditorPanel(Lookup lkp) {
        LayerBundle bundle = lkp.lookup(LayerBundle.class);
        if(bundle == null) {
            initLayer();
        } else {
            initLayer(bundle);
        }
        initComponents();
    }
    
    private void initLayer(LayerBundle bundle) {
        this.layers = bundle.getLayers();
        this.accidentTitle = bundle.getAccidentTitles();
        this.developmentTitle = bundle.getDevelopmentTitle();
    }
    
    private void initLayer() {
        this.layers = Collections.EMPTY_LIST;
        this.accidentTitle = new IndexTitleModel();
        this.developmentTitle = new IndexTitleModel();
    }
    
    public TriangleEditorPanel() {
        this.layers = Collections.EMPTY_LIST;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        add(createToolBar(), BorderLayout.NORTH);
        
        widget = new TriangleWidget(layers, new DevelopmentTriangleModel(developmentTitle, accidentTitle));
        add(widget, BorderLayout.CENTER);
        
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private JPanel createToolBar() {
        JPanel panel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0;
        gc.weightx = 1d; gc.weighty=0d;
        panel.add(Box.createHorizontalGlue(), gc);
        
        gc.gridx=1;
        gc.weightx=0d;
        gc.anchor = GridBagConstraints.BASELINE_LEADING;
        gc.insets = new Insets(0, 0, 0, 5);
        panel.add(new JLabel("Cummulated:"), gc);
        
        gc.gridx=2;
        gc.anchor = GridBagConstraints.BASELINE_TRAILING;
        panel.add(createCummulateCheckBox(), gc);
        
        gc.gridx=3;
        gc.weightx=0d;
        gc.insets = new Insets(0, 0, 0, 5);
        panel.add(new JLabel("Scale:"), gc);
        
        gc.gridx=4;
        panel.add(createDecimalSpinner(), gc);
        
        gc.gridx=5;
        panel.add(new JLabel("Layout:"), gc);
        
        gc.gridx=6;
        panel.add(createModelCombo(), gc);
        
        gc.gridx=7;
        panel.add(new JLabel("Layer:"), gc);
        
        gc.gridx=8;
        panel.add(createLayerCombo(), gc);
        
        return panel;
    }
    
    private JComboBox createLayerCombo() {
        final JComboBox combo = new JComboBox(getLayerNames());
        combo.setSelectedIndex(layers.size()-1);
        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = combo.getSelectedIndex();
                List<TriangleLayer> tl = new ArrayList<TriangleLayer>(index+1);
                for(int i=0; i<=index; i++)
                    tl.add(layers.get(i));
                widget.setLayers(tl);
            }
        });
        return combo;
    }
    
    private String[] getLayerNames() {
        int size = layers.size();
        String[] names = new String[size];
        for(int i=0; i<size; i++)
            names[i] = layers.get(i).getDisplayName();
        return names;
    }
    
    private JCheckBox createCummulateCheckBox() {
        final JCheckBox cb = new JCheckBox();
        cb.setSelected(true);
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                widget.setCummulated(cb.isSelected());
            }
        });
        return cb;
    }
    
    private JSpinner createDecimalSpinner() {
        final JSpinner spinner  = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        spinner.setPreferredSize(new Dimension(50, 22));
        JFormattedTextField field = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        field.setEditable(false);
        field.setBackground(Color.WHITE);
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                widget.setScale((Integer) spinner.getValue());
            }
        });
        return spinner;
    }
    
    private JComboBox createModelCombo() {
        final JComboBox combo = new JComboBox(new String[]{"Development", "Accident", "Calendar"});
        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = combo.getSelectedIndex();
                switch(index) {
                    case 0:
                        widget.setModel(new DevelopmentTriangleModel(developmentTitle, accidentTitle));
                        break;
                    case 1:
                        widget.setModel(new AccidentTriangleModel(accidentTitle, developmentTitle));
                        break;
                    default:
                        widget.setModel(new CalendarTriangleModel(accidentTitle, developmentTitle));
                        break;
                }
            }
        });
        return combo;
    }
}
