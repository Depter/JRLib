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

package org.jreserve.gui.trianglewidget;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.Box;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.trianglewidget.model.DevelopmentTriangleModel;
import org.jreserve.gui.trianglewidget.model.TitleModel;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.trianglewidget.model.TriangleModel;
import org.jreserve.jrlib.triangle.Triangle;
import javax.swing.JPanel;
import org.jreserve.gui.localesettings.LocaleSettings;
import org.jreserve.gui.trianglewidget.model.DefaultTriangleSelectionModel;
import org.jreserve.gui.trianglewidget.model.TriangleSelection;
import org.jreserve.gui.trianglewidget.model.TriangleSelectionListener;
import org.jreserve.gui.trianglewidget.model.TriangleSelectionModel;
import org.jreserve.gui.trianglewidget.model.TriangleSelectionEvent;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Cell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleWidget extends JPanel {
    
    final static int DEFAULT_ROW_HEIGHT = 20;
    final static int DEFAULT_COLUMN_WIDTH = 100;
    
    private List<TriangleLayer> layers;
    private TriangleModel model;
    private TriangleSelectionModel selectionModel = new DefaultTriangleSelectionModel();
    
    private ModelListener modelListener = new ModelListener();
    private TriangleGeometry geometry;
    private LocaleSettings.DecimalFormatter df = LocaleSettings.createDecimalFormatter();
    private SelectionListener selectionListener = new SelectionListener();
    private TriangleEditController editController;
    
    //Components
    private LayerTriangleRenderer renderer;
    private WidgetContentPanel content;
    private WidgetHeader xHeader;
    private WidgetHeader yHeader;
    private WidgetCorner corner;
    
    /**
     * Creates an empty widget, using
     * {@link DevelopmentTriangleModel DevelopmentTriangleModel} as model.
     */
    public TriangleWidget() {
        this(Collections.EMPTY_LIST);
    }
    
    /**
     * Creates an empty widget, using the sepcified layers and
     * {@link DevelopmentTriangleModel DevelopmentTriangleModel} as model.
     * 
     * @param layers the data layers to use. Can be null.
     */
    public TriangleWidget(List<TriangleLayer> layers) {
        this(layers, new DevelopmentTriangleModel());
    }
    
    /**
     * Creates an empty widget, using the sepcified layers and model.
     * 
     * @param layers the data layers to use. Can be null.
     * @param model if null, then {@link DevelopmentTriangleModel DevelopmentTriangleModel}
     *              will be used as default.
     */
    public TriangleWidget(List<TriangleLayer> layers, TriangleModel model) {
        if(layers == null) layers = Collections.EMPTY_LIST;
        if(model == null) model = new DevelopmentTriangleModel();
        
        this.layers = layers;
        this.selectionModel.addTriangleSelectionListener(selectionListener);
        initNewModel(model);
        initComponents();
    }

    private void initComponents() {
        renderer = new LayerTriangleRenderer();
        content = new WidgetContentPanel(this, renderer);
        content.addMouseListener(new ContentMouseAdapter());
        xHeader = new WidgetHeader(this, WidgetHeader.X_AXIS);
        yHeader = new WidgetHeader(this, WidgetHeader.Y_AXIS);
        corner = new WidgetCorner(this);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0;gc.gridy=0;
        gc.weightx=1d;gc.weighty=0d;
        gc.fill=GridBagConstraints.HORIZONTAL;
        add(creteContentPanel(), gc);
        
        gc.gridy=1;
        gc.weightx=0d;gc.weighty=1d;
        gc.fill=GridBagConstraints.VERTICAL;
        add(Box.createVerticalGlue(), gc);
    }
    
    private JPanel creteContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy=0;
        gc.weightx=0d; gc.weighty=0d;
        gc.fill = GridBagConstraints.NONE;
        gc.insets=new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(corner, gc);
        
        gc.gridx = 1;
        gc.weightx=1d;
        gc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(xHeader, gc);
        
        gc.gridx = 0; gc.gridy=1;
        gc.weightx=0d; gc.weighty=1d;
        gc.fill = GridBagConstraints.VERTICAL;
        panel.add(yHeader, gc);
        
        gc.gridx = 1;
        gc.weightx=1d;
        gc.fill = GridBagConstraints.BOTH;
        panel.add(content, gc);
        
        return panel;
    
    }
    
    public TriangleSelectionModel getTriangleSelectionModel() {
        return selectionModel;
    }
    
    public void setTriangleSelectionModel(TriangleSelectionModel selectionModel) {
        if(selectionModel == null)
            throw new NullPointerException("TriangleSelectionModel can not be null!");
        this.selectionModel.removeTriangleSelectionListener(selectionListener);
        this.selectionModel = selectionModel;
        this.selectionModel.addTriangleSelectionListener(selectionListener);
        resizeAndRepaint();
    }
    
    public boolean isEditable() {
        return editController != null;
    }
    
    public TriangleEditController getEditController() {
        return editController;
    }
    
    public void setEditController(TriangleEditController editController) {
        this.editController = editController;
    }
    
    public LocaleSettings.DecimalFormatter getDecimalFormatter() {
        return df;
    }
    
    public void setDecimalFormatter(LocaleSettings.DecimalFormatter df) {
        if(df == null)
            df = LocaleSettings.createDecimalFormatter();
        this.df = df;
        resizeAndRepaint();
    }
    
    public TriangleGeometry getTriangleGeometry() {
        return geometry;
    }
    
    public void setTriangleGeometry(TriangleGeometry geometry) {
        TriangleSelection selection = createSelection();
        if(this.geometry != null)
            this.geometry.removeChangeListener(modelListener);
        this.geometry = geometry;
        if(this.geometry != null)
            this.geometry.addChangeListener(modelListener);
        resizeAndRepaint();
        updateSelection(selection);
    }
    
    public TriangleSelection createSelection() {
        Triangle triangle = getModel().getTriangle();
        return selectionModel.createSelection(triangle);
    }
    
    private void updateSelection(TriangleSelection selection) {
        selectionModel.setValueAdjusting(true);
        
        selectionModel.clearSelection();
        int count = selection.getCellCount();
        for(Cell cell : selection.getCells()) {
            int accident = cell.getAccident();
            int development = cell.getDevelopment();
            if(isCellSelected(accident, development))
                selectionModel.setSelected(accident, development);
        }
        
        selectionModel.setValueAdjusting(false);
    }
    
    private boolean isCellSelected(int accident, int development) {
        int row = model.getRowIndex(accident, development);
        int column = model.getColumnIndex(accident, development);
        return model.hasValueAt(row, column);
    }
    
    /**
     * Returns the model, used by the widget.
     */
    public TriangleModel getModel() {
        return model;
    }
    
    /**
     * Sets the model used by this widget.
     * 
     * @param model if null, then {@link DevelopmentTriangleModel DevelopmentTriangleModel}
     *              will be used as default.
     */
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
        if(model == null)
            model = new DevelopmentTriangleModel();
        this.model = model;
        model.setTriangle(getTriangle());
        model.addChangeListener(modelListener);
    }
    
    private Triangle getTriangle() {
        if(layers==null || layers.isEmpty())
            return null;
        return layers.get(layers.size()-1).getTriangle();
    }
    
    /**
     * Returns the layers used by this widget.
     */
    public List<TriangleLayer> getLayers() {
        return new ArrayList<TriangleLayer>(layers);
    }
    
    /**
     * Sets the layers displayed in the widget.
     * 
     * @param layers the data layers to use. Can be null.
     */
    public void setLayers(List<TriangleLayer> layers) {
        if(layers == null) 
            layers = Collections.EMPTY_LIST;
        this.layers = new ArrayList<TriangleLayer>(layers);
        this.model.setTriangle(getTriangle());
        resizeAndRepaint();
    }
    
    /**
     * Sets the layers displayed in the widget.
     * 
     * @param layers the data layers to use.
     */
    public void setLayers(TriangleLayer... layers) {
        setLayers(Arrays.asList(layers));
    }
    
    /**
     * Returns the title model used for the column header.
     */
    public TitleModel getHorizontalTitleModel() {
        return model.getHorizontalTitleModel();
    }
    
    /**
     * Returns the title model used for the row header.
     */
    public TitleModel getVerticalTitleModel() {
        return model.getVerticalTitleModel();
    }
    
    /**
     * Sets wether the values should be cummulated.
     */
    public void setCummulated(boolean cummulated) {
        content.setCummualted(cummulated);
        resizeAndRepaint();
    }
    
    /**
     * Returns wether the values are cummulated.
     */
    public boolean isCummulated() {
        return content.isCummualted();
    }
    
    /**
     * Retunrs the number of rows in the widget.
     */
    public int getRowCount() {
        return model==null? 0 : model.getRowCount();
    }
    
    /**
     * Retunrs the number of columns in the widget.
     */
    public int getColumnCount() {
        return model==null? 0 : model.getColumnCount();
    }
    
    public int getRowIndex(Point point) {
        point = getContentPoint(point);
        return content.getRowIndex(point);
    }
    
    private Point getContentPoint(Point p) {
        Point l = content.getLocation();
        return new Point(p.x - l.x, p.y - l.y);
    }
    
    public int getColumnIndex(Point point) {
        point = getContentPoint(point);
        return content.getColumnIndex(point);
    }
    
    public Cell getCellAt(Point point) {
        point = getContentPoint(point);
        return content.getCellAt(point);
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
        xHeader.resize();
        yHeader.resize();
        corner.resize();
        
        setPreferredSize(calculatePrefferedSize());
        setMinimumSize(calculateMinimumSize());
        
        revalidate();
        repaint();
    }
    
    private Dimension calculatePrefferedSize() {
        Dimension cSize = corner.getPreferredSize();
        Dimension xSize = xHeader.getPreferredSize();
        Dimension ySize = yHeader.getPreferredSize();
        int w = cSize.width + xSize.width;
        int h = cSize.height + ySize.height;
        return new Dimension(w, h);
    }
    
    private Dimension calculateMinimumSize() {
        Dimension xSize = xHeader.getMinimumSize();
        Dimension ySize = yHeader.getMinimumSize();
        int w = ySize.width + xSize.width;
        int h = xSize.height + xSize.height;
        return new Dimension(w, h);
    }
    
//    @Override
//    public void addMouseListener(MouseListener listener) {
//        this.addMouseListener(listener);
//    }
//    
//    @Override
//    public void removeMouseListener(MouseListener listener) {
//        content.removeMouseListener(listener);
//    }
        
    private class ModelListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            resizeAndRepaint();
        }
    }
    
    private class SelectionListener implements TriangleSelectionListener {
        @Override
        public void selectionChanged(TriangleSelectionEvent event) {
            if(!event.isAdjusting())
                resizeAndRepaint();
        }
    }
    
    
    private class ContentMouseAdapter implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            MouseEvent evt = null;
            for(MouseListener l : getMouseListeners()) {
                if(evt == null)
                    evt = mirrorEvent(e);
                l.mouseClicked(evt);
            }
        }
        
        private MouseEvent mirrorEvent(MouseEvent original) {
            Point l = content.getLocation();
            int x = l.x + original.getX();
            int y = l.y + original.getY();
            return new MouseEvent(
                    TriangleWidget.this, original.getID(), original.getWhen(), 
                    original.getModifiers(), x, y, 
                    original.getXOnScreen(), original.getYOnScreen(), 
                    original.getClickCount(), original.isPopupTrigger(), 
                    original.getButton());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            MouseEvent evt = null;
            for(MouseListener l : getMouseListeners()) {
                if(evt == null)
                    evt = mirrorEvent(e);
                l.mousePressed(evt);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            MouseEvent evt = null;
            for(MouseListener l : getMouseListeners()) {
                if(evt == null)
                    evt = mirrorEvent(e);
                l.mouseReleased(evt);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
