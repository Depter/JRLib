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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractModifiableCalculationProvider;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleCalculation;
import org.jreserve.gui.data.api.DataEvent;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.audit.event.AuditedObject;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.utils.dataobject.ProjectObjectLookup;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimTriangleCalculationImpl 
    extends AbstractModifiableCalculationProvider<ClaimTriangle> 
    implements ClaimTriangleCalculation, AuditedObject {

    public final static String CT_ELEMENT = "claimTriangle";
    public final static String DS_ELEMENT = "dataSource";
    public final static String AUDIT_ID_ELEMENT = "auditId";
    
    private final GeometryListener geometryListener = new GeometryListener();
    private final ClaimTriangleDataObject dObj;
    
    private long auditId;
    private Project project;
    private DataSource dataSource;
    private TriangleGeometry geometry;
    private String path;
    
    ClaimTriangleCalculationImpl(ClaimTriangleDataObject dObj, Element root) throws Exception {
        super(root, ClaimTriangle.class);
        this.dObj = dObj;
        
        FileObject pf = dObj.getPrimaryFile();
        project = FileOwnerQuery.getOwner(pf);
        if(project == null)
            throw new IOException(String.format("File '%s' does not belong to a project!", pf.getPath()));
        
        auditId = JDomUtil.getExistingLong(root, AUDIT_ID_ELEMENT);
        dataSource = initDataSource(pf, root);
        geometry = TriangleGeometryUtil.fromXml(root);
        geometry.addChangeListener(geometryListener);
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
    
    @Override
    public Project getProject() {
        return project;
    }
    
    @Override
    public synchronized DataSource getDataSource() {
        return dataSource;
    }
    
    public synchronized void setDataSource(DataSource ds) {
        if(ds == null)
            throw new NullPointerException("DataSource is null!");
        this.dataSource = ds;
        dObj.setModified(true);
        recalculate();
        EventUtil.fireDataSourceChange(this, this);
    }
    
    private void recalculate() {
        //TODO recalculate the values
    }
    
    public synchronized TriangleGeometry getGeometry() {
        return geometry;
    }
    
    public synchronized void setGeometry(TriangleGeometry geometry) {
        if(geometry == null)
            throw new NullPointerException("Geometry is null!");
        
        this.geometry.removeChangeListener(geometryListener);
        this.geometry = geometry;
        this.geometry.addChangeListener(geometryListener);
        geometryChanged();
    }
    
    private void geometryChanged() {
        recalculate();
        //TODO fire event
    }
    
    @Override
    protected void modificationsChanged() {
    }

    @Override
    protected synchronized ClaimTriangle createBaseCalculation() throws Exception {
        return TriangleGeometryUtil.createTriangle(dataSource, geometry);
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
    public synchronized String getPath() {
        return path;
    }
    
    synchronized void setPath(String path) {
        this.path = path;
    }

    @Override
    public Project getAuditedProject() {
        return project;
    }

    @Override
    public synchronized String getAuditName() {
        return path;
    }

    @EventBusListener(forceEDT = true)
    public synchronized void dataSourcePathChanged(DataEvent.Deleted evt) {
        DataSource ds = evt.getDataSource();
        if(ds == dataSource)
            setDataSource(DataSource.EMPTY_TRIANGLE);
    }
    
    private class GeometryListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            geometryChanged();
        }
        
    }
}
