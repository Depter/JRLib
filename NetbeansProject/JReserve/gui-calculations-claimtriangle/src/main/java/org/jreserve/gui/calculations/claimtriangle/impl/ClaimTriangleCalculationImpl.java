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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractModifiableCalculationProvider;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleCalculation;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleModifier;
import org.jreserve.gui.data.api.DataEvent;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.audit.event.AuditedObject;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.utils.dataobject.ProjectObjectLookup;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.trianglewidget.DefaultTriangleLayer;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.claim.CummulatedClaimTriangle;
import org.jreserve.jrlib.triangle.claim.InputClaimTriangle;
import org.jreserve.jrlib.triangle.claim.ModifiedClaimTriangle;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
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
    extends AbstractModifiableCalculationProvider<ClaimTriangle> 
    implements ClaimTriangleCalculation, AuditedObject {
    
    public final static String CT_ELEMENT = "claimTriangle";
    public final static String DS_ELEMENT = "dataSource";
    public final static String AUDIT_ID_ELEMENT = "auditId";
    
    private final static Logger logger = Logger.getLogger(ClaimTriangleCalculationImpl.class.getName());
    
    private final GeometryListener geometryListener = new GeometryListener();
    private final ClaimTriangleDataObject dObj;
    private final long auditId;
    private final Project project;
    
    private DataSource dataSource;
    private TriangleGeometry geometry;
    private ClaimTriangle claimTriangle;
    
    ClaimTriangleCalculationImpl(ClaimTriangleDataObject dObj, Element root) throws Exception {
        super(dObj, ClaimTriangle.class);
        this.dObj = dObj;
        
        FileObject pf = dObj.getPrimaryFile();
        project = FileOwnerQuery.getOwner(pf);
        if(project == null)
            throw new IOException(String.format("File '%s' does not belong to a project!", pf.getPath()));
        
        auditId = JDomUtil.getExistingLong(root, AUDIT_ID_ELEMENT);
        dataSource = initDataSource(pf, root);
        geometry = TriangleGeometryUtil.fromXml(root);
        geometry.addChangeListener(geometryListener);
        recalculate();
    }
    
    private DataSource initDataSource(FileObject file, Element root) throws IOException {
        Element dse = JDomUtil.getExistingChild(root, DS_ELEMENT);
        String dsPath = dse.getTextTrim();
        if(dsPath == null || dsPath.length() == 0)
            return DataSource.EMPTY_TRIANGLE;
        
        Project p = FileOwnerQuery.getOwner(file);
        if(p == null) 
            return DataSource.EMPTY_TRIANGLE;
        
        ProjectObjectLookup pol = p.getLookup().lookup(ProjectObjectLookup.class);
        if(pol == null) 
            return DataSource.EMPTY_TRIANGLE;
        
        DataSource result = pol.lookupOne(dsPath, DataSource.class);
        return result == null? DataSource.EMPTY_TRIANGLE : result;
    }
    
    public void fireCreated() {
        synchronized(lock) {
            events.fireCreated();
        }
    }
    
    @Override
    public Project getProject() {
        return project;
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
            events.fireChange(Bundle.LBL_ClaimTriangleCalculationImpl_DataSource_Change(ds.getPath()));
        }
    }
    
    private void recalculate() {
        try {
            claimTriangle = TriangleGeometryUtil.createTriangle(dataSource, geometry);
            claimTriangle = new CummulatedClaimTriangle(claimTriangle);
            claimTriangle = super.modifyCalculation(claimTriangle);
        } catch (Exception ex) {
            String msg = "Unable to calculate claim triangle!";
            logger.log(Level.SEVERE, msg, ex);
            String title = Bundle.MSG_ClaimTriangleCalculationImpl_Calculation_Error();
            BubbleUtil.showException(title, getPath(), ex);
            claimTriangle = new InputClaimTriangle(new double[0][0]);
        }    
    }
    
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
        events.fireChange(Bundle.LBL_ClaimTriangleCalculationImpl_Geometry_Change(str));
    }
    
    @Override
    protected void modificationsChanged() {
        recalculate();
    }
    
    @Override
    public ClaimTriangle getCalculation() {
        return claimTriangle;
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
        return project;
    }

    @Override
    public String getAuditName() {
        return getPath();
    }

    @EventBusListener(forceEDT = true)
    public void dataSourcePathChanged(DataEvent.Deleted evt) {
        synchronized(lock) {
            DataSource ds = evt.getDataSource();
            if(ds == dataSource) {
                recalculate();
                events.fireChange();
            }
        }
    }
    
    public List<TriangleLayer> createLayers() {
        synchronized(lock) {
            List<ClaimTriangle> triangles = getCalculationLayers();
            int size = triangles.size();
            List<TriangleLayer> result = new ArrayList<TriangleLayer>(size);
            result.add(createBaseLayer(triangles.get(0)));
            
            for(int i=1; i<size; i++) {
                ClaimTriangleModifier modifier = (ClaimTriangleModifier) getModificationAt(i-1);
                ClaimTriangle layer = triangles.get(i);
                result.add(modifier.createLayer(layer));
            }
            return result;
        }
    }
    
    private List<ClaimTriangle> getCalculationLayers() {
        ClaimTriangle layer = claimTriangle;
        int count = getModificationCount();
        List<ClaimTriangle> result = new ArrayList<ClaimTriangle>(count+1);
        
        for(int i=0; i<count; i++) {
            result.add(0, layer);
            layer = ((ModifiedClaimTriangle)layer).getSourceClaimTriangle();
        }
        result.add(0, layer);
        
        return result;
    }
    
    private TriangleLayer createBaseLayer(ClaimTriangle input) {
        String name = Bundle.LBL_ClaimTriangleCalculationImpl_Layer_Base();
        return new DefaultTriangleLayer(input, name);
    }
    
    private class GeometryListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            geometryChanged();
        }
        
    }
}
