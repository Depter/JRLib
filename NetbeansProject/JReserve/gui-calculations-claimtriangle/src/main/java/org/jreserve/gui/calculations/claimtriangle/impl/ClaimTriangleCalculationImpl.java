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
package org.jreserve.gui.calculations.claimtriangle.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.modification.triangle.AbstractModifiableTriangleProvider;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleCalculation;
import org.jreserve.gui.data.api.DataEvent;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.audit.event.AuditedObject;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.claim.CummulatedClaimTriangle;
import org.jreserve.jrlib.triangle.claim.InputClaimTriangle;
import org.netbeans.api.project.Project;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - storagePath",
    "LBL.ClaimTriangleCalculationImpl.DataSource.Change=Data storage set to ''{0}''.",
    "# {0} - geometry",
    "LBL.ClaimTriangleCalculationImpl.Geometry.Change=Geometry set to ''{0}''.",
    "MSG.ClaimTriangleCalculationImpl.Calculation.Error=Unable to calculate claim triangle!",
    "LBL.ClaimTriangleCalculationImpl.Layer.Base=Input."
})
public class ClaimTriangleCalculationImpl 
    extends AbstractModifiableTriangleProvider<ClaimTriangle> 
    implements ClaimTriangleCalculation, AuditedObject {
    
    public final static String CATEGORY = "ClaimTriangle";
    public final static String CT_ELEMENT = "claimTriangle";
    public final static String DS_ELEMENT = "dataSource";
    public final static String AUDIT_ID_ELEMENT = "auditId";
    
    private final static Logger logger = Logger.getLogger(ClaimTriangleCalculationImpl.class.getName());
    
    private final GeometryListener geometryListener = new GeometryListener();
    private final ClaimTriangleDataObject dObj;
    private final long auditId;
    
    private DataSource dataSource;
    private TriangleGeometry geometry;
    
    ClaimTriangleCalculationImpl(ClaimTriangleDataObject dObj, Element root) throws Exception {
        super(dObj, root, CATEGORY);
        this.dObj = dObj;
        
        auditId = JDomUtil.getExistingLong(root, AUDIT_ID_ELEMENT);
        initDataSource(root);
        geometry = TriangleGeometryUtil.fromXml(root);
        geometry.addChangeListener(geometryListener);
    }
    
    private void initDataSource(Element root) throws IOException {
        dataSource = super.lookupSource(DataSource.class, root, DS_ELEMENT);
        if(dataSource == null || DataType.TRIANGLE != dataSource.getDataType())
            dataSource = DataSource.EMPTY_TRIANGLE;
    }
    
    public void fireCreated() {
        synchronized(lock) {
            events.fireCreated();
        }
    }

    @Override
    public Class<ClaimTriangle> getCalculationClass() {
        return ClaimTriangle.class;
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
            events.fireChange(Bundle.LBL_ClaimTriangleCalculationImpl_DataSource_Change(ds.getPath()));
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
        recalculateIfExists();
        String str = geometry.toString();
        events.fireChange(Bundle.LBL_ClaimTriangleCalculationImpl_Geometry_Change(str));
    }
    
    @Override
    public synchronized Element toXml() {
        Element root = new Element(CT_ELEMENT);
        JDomUtil.addElement(root, AUDIT_ID_ELEMENT, auditId);
        JDomUtil.addElement(root, DS_ELEMENT, dataSource.getPath());
        root.addContent(TriangleGeometryUtil.toXml(geometry));
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
    
    @Override
    public void setPath(String path) {
        super.setPath(path);
    }
    
    @EventBusListener
    public synchronized void dataEvent(DataEvent evt) {
        if(isDataChanged(evt)) {
            recalculateIfExists();
            events.fireChange();
        }
    }
    
    private boolean isDataChanged(DataEvent evt) {
        return dataSource == evt.getDataSource() &&
               (evt instanceof DataEvent.DataChange);
    }

    @Override
    protected ClaimTriangle createDummyCalculation() {
        return new InputClaimTriangle(new double[0][0]);
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
                this.ds = ClaimTriangleCalculationImpl.this.dataSource;
                this.geometry = new TriangleGeometry(ClaimTriangleCalculationImpl.this.geometry);
            }
        }

        @Override
        protected ClaimTriangle getRootCalculation() {
            try {
                ClaimTriangle result = TriangleGeometryUtil.createTriangle(ds, this.geometry);
                return new CummulatedClaimTriangle(result);
            } catch (Exception ex) {
                String msg = "Unable to calculate claim triangle!";
                logger.log(Level.SEVERE, msg, ex);
                String title = Bundle.MSG_ClaimTriangleCalculationImpl_Calculation_Error();
                BubbleUtil.showException(title, getPath(), ex);
                return new InputClaimTriangle(new double[0][0]);
            }
        }
    }
}
