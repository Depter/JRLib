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
package org.jreserve.gui.excel.template.dataimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.jrlib.gui.data.TriangleGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
abstract class DataImportTemplateItem {
    
    @XmlElement(name="reference", required=true)
    private String reference;
    @XmlElement(name="dataType", required=true)
    private DataType dataType;
    
    public DataImportTemplateItem() {
    }

    public DataImportTemplateItem(String reference, DataType dataType) {
        this.reference = reference;
        this.dataType = dataType;
    }
    
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
    
    @XmlRootElement(name="tableSource")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class TableDataImportTempalteItem extends DataImportTemplateItem {
        
        TableDataImportTempalteItem() {
        }

        TableDataImportTempalteItem(String reference, DataType dataType) {
            super(reference, dataType);
        }
    }
    
    @XmlRootElement(name="triangleSource")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class TriangleDataImportTempalteItem extends DataImportTemplateItem {
        @XmlElement(name="geometry",required=true)
        private TriangleGeometry geometry;

        public TriangleDataImportTempalteItem() {
        }

        public TriangleDataImportTempalteItem(String reference, DataType dataType, TriangleGeometry geometry) {
            super(reference, dataType);
            if(geometry == null)
                throw new NullPointerException("Geometry is null!");
            this.geometry = geometry;
        }

        public TriangleGeometry getGeometry() {
            return geometry;
        }

        public void setGeometry(TriangleGeometry geometry) {
            this.geometry = geometry;
        }
    }
    
}
