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
package org.jreserve.dummy.claimtriangle.edtior.scene;

import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DateFormatter;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.vmd.VMDGraphScene;
import org.netbeans.api.visual.vmd.VMDNodeWidget;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.explorer.propertysheet.PropertySheetView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleVMDScene extends VMDGraphScene {

    private final static String TREE_HOME = "org/jreserve/dummy/projecttree/resources/";
    private final static String HOME = "org/jreserve/dummy/claimtriangle/edtior/";
    private final static Image DATA_SOURCE = ImageUtilities.loadImage(TREE_HOME + "database.png"); // NOI18N
    private final static Image GEOMETRY = ImageUtilities.loadImage(HOME + "ruler_triangle.png"); // NOI18N
    private final static Image SETTINGS = ImageUtilities.loadImage(HOME + "settings.png"); // NOI18N
    private final static Image CORRECT = ImageUtilities.loadImage(TREE_HOME + "correction.png"); // NOI18N
    private final static Image SMOOTH = ImageUtilities.loadImage(TREE_HOME + "cell_smoothing.png"); // NOI18N
    private final static Image EXCLUDE = ImageUtilities.loadImage(TREE_HOME + "exclude.png"); // NOI18N
    private final static Image TRIANGLE = ImageUtilities.loadImage(TREE_HOME + "triangle.png"); // NOI18N
    private final static List<Image> SETTINGS_GLYPH = Collections.singletonList(SETTINGS);
    private static int NODE_ID;
    private static int EDGE_ID;
    private Router router = RouterFactory.createDirectRouter();
    private Map<String, PropertyNode> nodes = new HashMap<String, PropertyNode>(9);
    private PropertySheetView propertyView;

    private WidgetAction reconnectAction = ActionFactory.createReconnectAction(new MyReconnectProvider());
    
    public TriangleVMDScene(PropertySheetView propertyView) {
        this.propertyView = propertyView;

        String data = createNode(100, 20, DATA_SOURCE, "Data Source", SETTINGS_GLYPH);
        createPropertyNode(data,
                new DummyProperty("Dataset", "APC_Paid"),
                new DummyProperty("Start date", getDate("1997-01")),
                new DummyProperty("End date", getDate("2004-01")));

        String geometry = createNode(100, 120, GEOMETRY, "Geometry", SETTINGS_GLYPH);
        createEdge(data, geometry);
        createPropertyNode(geometry,
                new DummyProperty("Accident length", 12),
                new DummyProperty("Development length", 12));

        String correction = createNode(100, 220, CORRECT, "Correction", SETTINGS_GLYPH);
        createPropertyNode(correction,
                new DummyProperty("Accident", getDate("1997-01")),
                new DummyProperty("Development", 4),
                new DummyProperty("Value", 5600000d));
        createEdge(geometry, correction);

        String smoothing = createSmoothing();
        createEdge(correction, smoothing);

        String exclude = createNode(100, 420, EXCLUDE, "Exclusion", SETTINGS_GLYPH);
        createEdge(smoothing, exclude);
        createPropertyNode(exclude,
                new DummyProperty("Accident", getDate("1999-01")),
                new DummyProperty("Development", 3));

        String triangle = createNode(100, 520, TRIANGLE, "Output", SETTINGS_GLYPH);
        createEdge(exclude, triangle);

        addObjectSceneListener(new SelectionListener(), ObjectSceneEventType.OBJECT_SELECTION_CHANGED);
    }

    private String createNode(int x, int y, Image image, String name, List<Image> glyphs) {
        String nodeID = "node" + (NODE_ID++);

        VMDNodeWidget widget = (VMDNodeWidget) this.addNode(nodeID);
        widget.setPreferredLocation(new Point(x, y));
        widget.setNodeProperties(image, name, null, glyphs);

        this.addPin(nodeID, nodeID + VMDGraphScene.PIN_ID_DEFAULT_SUFFIX);
        return nodeID;
    }

    private void createPropertyNode(String nodeID, DummyProperty... properties) {
        List<DummyProperty> props = new ArrayList<DummyProperty>();
        props.addAll(Arrays.asList(properties));
        nodes.put(nodeID, new PropertyNode(props));
    }

    private void createEdge(String sourceID, String targetID) {
        String edgeID = "edge" + EDGE_ID++;
        ConnectionWidget edge = (ConnectionWidget) this.addEdge(edgeID);
        edge.getActions().addAction(reconnectAction);
        edge.setRouter(router);

        //edge.setRoutingPolicy(ConnectionWidget.RoutingPolicy.UPDATE_END_POINTS_ONLY);
        this.setEdgeSource(edgeID, sourceID + VMDGraphScene.PIN_ID_DEFAULT_SUFFIX);
        this.setEdgeTarget(edgeID, targetID + VMDGraphScene.PIN_ID_DEFAULT_SUFFIX);
    }

    private String createSmoothing() {
        String smoothing = createNode(100, 320, SMOOTH, "Smoothing", SETTINGS_GLYPH);
        createPropertyNode(smoothing,
                new DummyProperty("Type", "Exponential"),
                new DummyProperty("Alpha", 0.5));

        String cell1 = createNode(300, 220, null, "2000-01/1", SETTINGS_GLYPH);
        createEdge(cell1, smoothing);
        createPropertyNode(cell1,
                new DummyProperty("Accident", getDate("2000-01")),
                new DummyProperty("Development", 1),
                new DummyProperty("Applied", false));

        String cell2 = createNode(300, 320, null, "2001-01/1", SETTINGS_GLYPH);
        createEdge(cell2, smoothing);
        createPropertyNode(cell2,
                new DummyProperty("Accident", getDate("2001-01")),
                new DummyProperty("Development", 1),
                new DummyProperty("Applied", false));

        String cell3 = createNode(300, 420, null, "2002-01/1", SETTINGS_GLYPH);
        createEdge(cell3, smoothing);
        createPropertyNode(cell3,
                new DummyProperty("Accident", getDate("2002-01")),
                new DummyProperty("Development", 1),
                new DummyProperty("Applied", true));

        return smoothing;
    }
    private final static SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM");

    private static Date getDate(String date) {
        try {
            return DF.parse(date);
        } catch (Exception ex) {
            return new Date();
        }
    }

    private class SelectionListener implements ObjectSceneListener {

        @Override
        public void selectionChanged(ObjectSceneEvent ose, Set<Object> oldSel, Set<Object> newSel) {
            String id = (String) getNewSelection(oldSel, newSel);
            if (id != null) {
                Node node = nodes.get(id);
                if (node != null) {
                    propertyView.setNodes(new Node[]{node});
                }
            }
        }

        private Object getNewSelection(Set<Object> oldSel, Set<Object> newSel) {
            for (Object o : newSel) {
                if (!oldSel.contains(o)) {
                    return o;
                }
            }
            return null;
        }

        @Override
        public void objectAdded(ObjectSceneEvent ose, Object o) {
        }

        @Override
        public void objectRemoved(ObjectSceneEvent ose, Object o) {
        }

        @Override
        public void objectStateChanged(ObjectSceneEvent ose, Object o, ObjectState os, ObjectState os1) {
        }

        @Override
        public void highlightingChanged(ObjectSceneEvent ose, Set<Object> set, Set<Object> set1) {
        }

        @Override
        public void hoverChanged(ObjectSceneEvent ose, Object o, Object o1) {
        }

        @Override
        public void focusChanged(ObjectSceneEvent ose, Object o, Object o1) {
        }
    }

    private class PropertyNode extends AbstractNode {

        private List<DummyProperty> props;

        private PropertyNode(List<DummyProperty> props) {
            super(Children.LEAF);
            this.props = props;
        }

        @Override
        protected Sheet createSheet() {
            Sheet sheet = Sheet.createDefault();
            Sheet.Set set = Sheet.createPropertiesSet();
            set.setDisplayName("Properties");
            for (Node.Property p : props) {
                set.put(p);
            }
            sheet.put(set);
            return sheet;
        }
    }

    private class DummyProperty extends Node.Property {

        private Object value;
        private PropertyEditor editor;

        DummyProperty(String name, Object value) {
            this(name, value.getClass(), value);
        }

        DummyProperty(String name, Class clazz) {
            this(name, clazz, null);
        }

        DummyProperty(String name, Class clazz, Object value) {
            super(clazz);
            super.setDisplayName(name);
            super.setName(name);
            this.value = value;

            if (Date.class.isAssignableFrom(clazz)) {
                editor = new DatePropertyEditor(value == null ? new Date() : (Date) value);
            } else if (Integer.class.isAssignableFrom(clazz)) {
                editor = new IntPropertyEditor(value == null ? 1 : (Integer) value);
            } else {
                editor = null;
            }
        }

        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return value;
        }

        @Override
        public boolean canWrite() {
            return true;
        }

        @Override
        public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            this.value = t;
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            return editor == null
                    ? super.getPropertyEditor()
                    : editor;
        }
    }

    private abstract class SpinnerPropertyEditor extends PropertyEditorSupport
            implements ExPropertyEditor, InplaceEditor.Factory {

        protected SpinnerModel model;
        protected SpinnerEditor spinner;

        protected SpinnerPropertyEditor(Object value) {
            model = initModel(value);
        }

        protected abstract SpinnerModel initModel(Object value);

        @Override
        public void attachEnv(PropertyEnv env) {
            env.registerInplaceEditorFactory(this);
        }
    }

    private class IntPropertyEditor extends SpinnerPropertyEditor {

        private IntPropertyEditor(Integer value) {
            super(value);
        }

        @Override
        protected SpinnerModel initModel(Object value) {
            int i = (Integer) value;
            return new SpinnerNumberModel(i, 1, Integer.MAX_VALUE, 1);
        }

        @Override
        public String getAsText() {
            Integer i = (Integer) getValue();
            if (i == null) {
                return "Not set";
            }
            return "" + i.intValue();
        }

        @Override
        public void setAsText(String s) {
            try {
                setValue(Integer.parseInt(s));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Unparsable integer: " + s);
            }
        }

        @Override
        public InplaceEditor getInplaceEditor() {
            if (spinner == null) {
                spinner = new IntSpinnerEditor(model);
            }
            return spinner;
        }
    }

    private class DatePropertyEditor extends SpinnerPropertyEditor {

        private DatePropertyEditor(Date value) {
            super(value);
        }

        @Override
        protected SpinnerModel initModel(Object value) {
            Date now = (Date) value;
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.YEAR, -100);
            Date start = calendar.getTime();
            calendar.add(Calendar.YEAR, 200);
            Date end = calendar.getTime();
            return new SpinnerDateModel(now, start, end, Calendar.MONTH);
        }

        @Override
        public String getAsText() {
            Date d = (Date) getValue();
            if (d == null) {
                return "Not set";
            }
            return DF.format(d);
        }

        @Override
        public void setAsText(String s) {
            try {
                setValue(DF.parse(s));
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Unparsable date: " + s);
            }
        }

        @Override
        public InplaceEditor getInplaceEditor() {
            if (spinner == null) {
                spinner = new DateSpinnerEditor(model);
            }
            return spinner;
        }
    }

    private abstract class SpinnerEditor implements InplaceEditor {

        protected JSpinner spinner;
        protected PropertyEditor editor = null;
        private PropertyModel model;
        private boolean textEdit;

        private SpinnerEditor(SpinnerModel model, boolean textEdit) {
            spinner = new JSpinner(model);
            spinner.setBorder(BorderFactory.createEmptyBorder());
            spinner.getEditor().setBorder(BorderFactory.createEmptyBorder());
            this.textEdit = textEdit;
            if (!textEdit) {
                disableTextEdit();
            }
        }

        private void disableTextEdit() {
            JFormattedTextField field = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
            java.awt.Color bg = field.getBackground();
            field.setEditable(false);
            field.setBackground(bg);
            field.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        }

        @Override
        public void connect(PropertyEditor pe, PropertyEnv pe1) {
            editor = pe;
            reset();
        }

        @Override
        public JComponent getComponent() {
            return spinner;
        }

        @Override
        public void clear() {
            editor = null;
            model = null;
        }

        @Override
        public boolean supportsTextEntry() {
            return textEdit;
        }

        @Override
        public KeyStroke[] getKeyStrokes() {
            return new KeyStroke[0];
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            return editor;
        }

        @Override
        public PropertyModel getPropertyModel() {
            return model;
        }

        @Override
        public void setPropertyModel(PropertyModel pm) {
            this.model = pm;
        }

        @Override
        public boolean isKnownComponent(Component c) {
            return c == spinner || spinner.isAncestorOf(c);
        }

        @Override
        public void addActionListener(ActionListener al) {
        }

        @Override
        public void removeActionListener(ActionListener al) {
        }
    }

    private class DateSpinnerEditor extends SpinnerEditor {

        private DateSpinnerEditor(SpinnerModel model) {
            super(model, false);
            JFormattedTextField field = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
            field.setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {
                @Override
                public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
                    return new DateFormatter(DF);
                }
            });
        }

        @Override
        public void reset() {
            Date d = (Date) editor.getValue();
            if (d != null) {
                setValue(d);
            }
        }

        @Override
        public Object getValue() {
            return spinner.getValue();
        }

        @Override
        public void setValue(Object o) {
            try {
                spinner.setValue(o);
            } catch (IllegalArgumentException ex) {
                spinner.setValue(null);
            }
        }
    }

    private class IntSpinnerEditor extends SpinnerEditor {

        private IntSpinnerEditor(SpinnerModel model) {
            super(model, false);
        }

        @Override
        public void reset() {
            Integer d = (Integer) editor.getValue();
            if (d != null) {
                setValue(d);
            }
        }

        @Override
        public Object getValue() {
            return spinner.getValue();
        }

        @Override
        public void setValue(Object o) {
            try {
                spinner.setValue(o);
            } catch (IllegalArgumentException ex) {
                spinner.setValue(null);
            }
        }
    }

    private class MyReconnectProvider implements ReconnectProvider {

        private String edge;
        private String originalNode;
        private String replacementNode;

        @Override
        public void reconnectingStarted(ConnectionWidget cw, boolean bln) {
        }

        @Override
        public void reconnectingFinished(ConnectionWidget cw, boolean bln) {
        }

        @Override
        public boolean hasCustomReplacementWidgetResolver(Scene scene) {
            return false;
        }

        @Override
        public Widget resolveReplacementWidget(Scene scene, Point point) {
            return null;
        }

        @Override
        public boolean isSourceReconnectable(ConnectionWidget cw) {
            Object object = findObject(cw);
            edge = isEdge(object) ? (String) object : null;
            originalNode = edge != null ? getEdgeSource(edge) : null;
            return originalNode != null;
        }

        @Override
        public boolean isTargetReconnectable(ConnectionWidget cw) {
            Object object = findObject(cw);
            edge = isEdge(object) ? (String) object : null;
            originalNode = edge != null ? getEdgeTarget(edge) : null;
            return originalNode != null;
        }

        @Override
        public ConnectorState isReplacementWidget(ConnectionWidget cw, Widget widget, boolean bln) {
            Object o = findObject(widget);
            replacementNode = isNode(o) ? (String) o : null;
            if (replacementNode != null) {
                return ConnectorState.ACCEPT;
            }
            return o != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
        }

        @Override
        public void reconnect(ConnectionWidget cw, Widget widget, boolean reconnectingSource) {
            if(widget == null) {
                removeEdge(edge);
            } else if(reconnectingSource) {
                setEdgeSource(edge, replacementNode);
            } else {
                setEdgeTarget(edge, replacementNode);
            }
        }
    }
}
