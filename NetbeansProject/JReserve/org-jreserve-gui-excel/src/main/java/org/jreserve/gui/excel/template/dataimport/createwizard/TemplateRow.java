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
package org.jreserve.gui.excel.template.dataimport.createwizard;

import org.jreserve.gui.excel.template.dataimport.DataImportTemplateItem;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplateItem.Table;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplateItem.Triangle;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.jrlib.gui.data.MonthDate;

/**
 *
 * @author Decsi Peter
 * @version 1.0
 */
public class TemplateRow {

    private String reference;
    private SourceType sourceType;
    private DataType dataType;
    private MonthDate monthDate;
    private Integer accidentLength;
    private Integer developmentLength;
    private Boolean cummulated;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
        if (SourceType.TRIANGLE != sourceType) {
            monthDate = null;
            accidentLength = null;
            developmentLength = null;
        }
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public MonthDate getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(MonthDate monthDate) {
        this.monthDate = monthDate;
    }

    public Integer getAccidentLength() {
        return accidentLength;
    }

    public void setAccidentLength(Integer accidentLength) {
        this.accidentLength = accidentLength;
    }

    public Integer getDevelopmentLength() {
        return developmentLength;
    }

    public void setDevelopmentLength(Integer developmentLength) {
        this.developmentLength = developmentLength;
    }

    public Boolean isCummulated() {
        return cummulated;
    }

    public void setCummulated(Boolean cummulated) {
        this.cummulated = cummulated;
    }
    
    public DataImportTemplateItem createTempalteItem() {
        if(SourceType.TABLE == sourceType) {
            return new Table(reference, dataType, cummulated);
        } else {
            return new Triangle(reference, dataType, cummulated,
                    monthDate, 
                    accidentLength , developmentLength);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof TemplateRow)) {
            return false;
        }
        TemplateRow tr = (TemplateRow) o;
        return equals(reference, tr.reference)
                && sourceType == tr.sourceType
                && dataType == tr.dataType
                && equals(monthDate, tr.monthDate)
                && equals(cummulated, tr.cummulated)
                && equals(accidentLength, tr.accidentLength)
                && equals(developmentLength, tr.developmentLength);
    }

    private boolean equals(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        return o2 == null ? false : o1.equals(o2);
    }

    @Override
    public int hashCode() {
        int hash = 31;
        hash = 17 * hash + (reference == null ? 0 : reference.hashCode());
        hash = 17 * hash + (sourceType == null ? 0 : sourceType.hashCode());
        hash = 17 * hash + (dataType == null ? 0 : dataType.hashCode());
        hash = 17 * hash + (monthDate == null ? 0 : monthDate.hashCode());
        hash = 17 * hash + (cummulated == null ? 0 : cummulated.hashCode());
        hash = 17 * hash + (accidentLength == null ? 0 : accidentLength.hashCode());
        return 17 * hash + (developmentLength == null ? 0 : developmentLength.hashCode());
    }
}
