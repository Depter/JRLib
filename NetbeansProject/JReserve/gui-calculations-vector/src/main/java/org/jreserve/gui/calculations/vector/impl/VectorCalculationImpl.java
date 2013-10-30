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
package org.jreserve.gui.calculations.vector.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.modification.AbstractModifiableCalculationProvider;
import org.jreserve.gui.calculations.vector.VectorCalculation;
import org.jreserve.gui.calculations.vector.VectorModifier;
import org.jreserve.gui.data.api.DataEvent;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.audit.event.AuditedObject;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.trianglewidget.DefaultTriangleLayer;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.vector.InputVector;
import org.jreserve.jrlib.vector.Vector;
import org.netbeans.api.project.Project;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - storagePath",
    "LBL.VectorCalculationImpl.DataSource.Change=Data storage set to ''{0}''.",
    "# {0} - geometry",
    "LBL.VectorCalculationImpl.Geometry.Change=Geometry set to ''{0}''.",
    "MSG.VectorCalculationImpl.Calculation.Error=Unable to calculate vector!",
    "LBL.VectorCalculationImpl.Layer.Base=Input."
})
public class VectorCalculationImpl
    extends AbstractModifiableCalculationProvider<Vector> 
    implements VectorCalculation, AuditedObject {
    
    public final static String CATEGORY = "Vector";
    public final static String CT_ELEMENT = "vector";
    public final static String DS_ELEMENT = "dataSource";
    public final static String AUDIT_ID_ELEMENT = "auditId";
    
    private final static Logger logger = Logger.getLogger(VectorCalculationImpl.class.getName());
    
    private final GeometryListener geometryListener = new GeometryListener();
    private final VectorDataObject dObj;
    private final long auditId;
    
    private DataSource dataSource;
    private TriangleGeometry geometry;
    
    VectorCalculationImpl(VectorDataObject dObj, Element root) throws Exception {
        super(dObj, root, CATEGORY);
        this.dObj = dObj;
        
        auditId = JDomUtil.getExistingLong(root, AUDIT_ID_ELEMENT);
        initDataSource(root);
        geometry = VectorGeometryUtil.fromXml(root);
        geometry.addChangeListener(geometryListener);
    }
    
    private void initDataSource(Element root) throws IOException {
        dataSource = super.lookupSource(DataSource.class, root, DS_ELEMENT);
        if(dataSource == null || DataType.TRIANGLE == dataSource.getDataType())
            dataSource = DataSource.EMPTY_VECTOR;
    }

    public void fireCreated() {
        synchronized(lock) {
            events.fireCreated();
        }
    }

    @Override
    public Class<Vector> getCalculationClass() {
        return Vector.class;
    }
    
    @Override
    protected void setPath(String path) {
        super.setPath(path);
    }
    
    @Override
    public DataSource getDataSource() {
        synchronized(lock) {
            return dataSource;
        }
    }
    
    public void setDataSource(DataSource ds) {
        synchronized(lock) {
            if(ds == null)
                throw new NullPointerException("DataSource is null!");
            this.dataSource = ds;
            dObj.setModified(true);
            recalculate();
            events.fireChange(Bundle.LBL_VectorCalculationImpl_DataSource_Change(ds.getPath()));
        }
    }
    
    
    @Override
    public TriangleGeometry getGeometry() {
        synchronized(lock) {
            return geometry;
        }
    }
    
    public void setGeometry(TriangleGeometry geometry) {
        synchronized(lock) {
            if(geometry == null)
                throw new NullPointerException("Geometry is null!");
        
            this.geometry.removeChangeListener(geometryListener);
            this.geometry = geometry;
            this.geometry.addChangeListener(geometryListener);
            geometryChanged();
        }
    }
    
    private void geometryChanged() {
        dObj.setModified(true);
        recalculate();
        String str = geometry.toString();
        events.fireChange(Bundle.LBL_VectorCalculationImpl_Geometry_Change(str));
    }
    
    @Override
    public synchronized Element toXml() {
        Element root = new Element(CT_ELEMENT);
        JDomUtil.addElement(root, AUDIT_ID_ELEMENT, auditId);
        JDomUtil.addElement(root, DS_ELEMENT, dataSource.getPath());
        root.addContent(VectorGeometryUtil.toXml(geometry));
        root.addContent(super.toXml());
        return root;
    }
    
    @Override
    public long getAuditId() {
        return auditId;
    }

    @Override
    public Project getAuditedProject() {
        return getProject();
    }

    @Override
    public String getAuditName() {
        return getPath();
    }
    
    public List<TriangleLayer> createLayers() {
        synchronized(lock) {
            if(!calculationExists()) {
                recalculate();
                Vector dummy = createDummyCalculation();
                return Collections.singletonList(createBaseLayer(dummy));
            }
            
            int count = super.getModificationCount() + 1;
            List<TriangleLayer> result = new ArrayList<TriangleLayer>(count);
            
            Vector base = super.getCalculation(0);
            result.add(createBaseLayer(base));
            
            for(int i=1; i<count; i++) {
                VectorModifier modifier = (VectorModifier) getModificationAt(i-1);
                Vector layer = super.getCalculation(i);
                result.add(modifier.createLayer(layer));
            }
            return result;
        }
    }
    
    private TriangleLayer createBaseLayer(Vector input) {
        String name = Bundle.LBL_VectorCalculationImpl_Layer_Base();
        Triangle triangle = VectorGeometryUtil.toTriangle(input);
        return new DefaultTriangleLayer(triangle, name);
    }
    
    @EventBusListener
    public synchronized void dataEvent(DataEvent evt) {
        if(isDataChanged(evt)) {
            recalculate();
            events.fireChange();
        }
    }
    
    private boolean isDataChanged(DataEvent evt) {
        return dataSource == evt.getDataSource() &&
               (evt instanceof DataEvent.DataChange);
    }

    @Override
    protected Vector createDummyCalculation() {
        return new InputVector(new double[0]);
    }

    @Override
    protected ModificationCalculator createCalculator() {
        return new RecalculateTask();
    }
    
    private class GeometryListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            geometryChanged();
        }
    }
    
    private class RecalculateTask extends ModificationCalculator {
        private final DataSource ds;
        private final TriangleGeometry geometry;

        private RecalculateTask() {
            synchronized(lock) {
                this.ds = VectorCalculationImpl.this.dataSource;
                this.geometry = new TriangleGeometry(VectorCalculationImpl.this.geometry);
            }
        }

        @Override
        protected Vector getRootCalculation() {
            try {
                return VectorGeometryUtil.createTriangle(ds, geometry);
            } catch (Exception ex) {
                String msg = "Unable to calculate vector!";
                logger.log(Level.SEVERE, msg, ex);
                String title = Bundle.MSG_VectorCalculationImpl_Calculation_Error();
                BubbleUtil.showException(title, getPath(), ex);
                return new InputVector(new double[0]);
            }
        }
    }
}
