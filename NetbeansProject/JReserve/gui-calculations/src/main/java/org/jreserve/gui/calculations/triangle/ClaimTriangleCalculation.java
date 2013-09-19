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
package org.jreserve.gui.calculations.triangle;

import java.io.IOException;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractModifiableCalculationProvider;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.audit.db.AuditDbManager;
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
public class ClaimTriangleCalculation extends AbstractModifiableCalculationProvider<ClaimTriangle> {

    private final static String CT_ELEMENT = "claimTriangle";
    private final static String DS_ELEMENT = "dataSource";
    private final static String AUDIT_ID_ELEMENT = "auditId";
    
    private long auditId;
    private DataSource dataSource;
    private TriangleGeometry geometry;
    private String path;
    
    public ClaimTriangleCalculation(Project project, DataSource ds, TriangleGeometry geometry) {
        super(ClaimTriangle.class);
        auditId = AuditDbManager.getInstance().getNextObjectId(project);
        dataSource = ds;
        this.geometry = geometry;
    }
    
    public ClaimTriangleCalculation(FileObject file, Element root) throws Exception {
        super(root, ClaimTriangle.class);
        auditId = JDomUtil.getExistingLong(root, AUDIT_ID_ELEMENT);
        dataSource = initDataSource(file, root);
    }
    
    private DataSource initDataSource(FileObject file, Element root) throws IOException {
        Element dse = JDomUtil.getExistingChild(root, DS_ELEMENT);
        String dsPath = dse.getTextTrim();
        if(dsPath == null || dsPath.length() == 0)
            return null;
        
        Project p = FileOwnerQuery.getOwner(file);
        if(p == null) 
            return null;
        
        ProjectObjectLookup pol = p.getLookup().lookup(ProjectObjectLookup.class);
        if(pol == null) 
            return null;
        
        return pol.lookupOne(dsPath, DataSource.class);
    }
    
    @Override
    protected void modificationsChanged() {
    }

    @Override
    protected ClaimTriangle createBaseCalculation() throws Exception {
        return TriangleGeometryUtil.createTriangle(dataSource, geometry);
    }
    
    @Override
    public Element toXml() {
        Element root = new Element(CT_ELEMENT);
        JDomUtil.addElement(root, AUDIT_ID_ELEMENT, auditId);
        JDomUtil.addElement(root, DS_ELEMENT, dataSource.getPath());
        root.addContent(TriangleGeometryUtil.toXml(geometry));
        root.addContent(super.toXml());
        return root;
    }
    
    long getAuditId() {
        return auditId;
    }
    
    @Override
    public String getPath() {
        return path;
    }
    
    void setPath(String path) {
        this.path = path;
    }
}
