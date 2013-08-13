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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ImportTemplateModel.Reference=Reference",
    "LBL.ImportTemplateModel.SourceType=Source Type",
    "LBL.ImportTemplateModel.DataType=Data Type",
    "LBL.ImportTemplateModel.Cummulated=Cummulated",
    "LBL.ImportTemplateModel.StartDate=Start Date",
    "LBL.ImportTemplateModel.AccidentLength=Accident Length",
    "LBL.ImportTemplateModel.DevelopmentLength=Development Length",
    "LBL.ImportTemplateModel.SourceType.Table=Table",
    "LBL.ImportTemplateModel.SourceType.Triangle=Triangle"
})
public class ImportTemplateModel extends AbstractTableModel {
    
    private final static int COL_REFERENCE = 0;
    private final static int COL_SOURCE_TYPE = 1;
    private final static int COL_DATA_TYPE = 2;
    private final static int COL_CUMMULATED = 3;
    private final static int COL_START_DATE = 4;
    private final static int COL_ACCIDENT_LENGTH = 5;
    private final static int COL_DEVELOPMENT_LENGTH = 6;
    private final static int COLUMN_COUNT = 7;
    
    static enum SourceType implements Displayable {
        TABLE(
            Bundle.LBL_CreateTempalteWizardVisualPanel_SoruceType_Table(), 
            "org/jreserve/gui/excel/source_table.png"), //NOI18
        TRIANGLE(
            Bundle.LBL_CreateTempalteWizardVisualPanel_SoruceType_Triangle(), 
            "org/jreserve/gui/excel/source_triangle.png");  //NOI18
        
        private final String displayName;
        private final Icon icon;
        
        private SourceType(String displayName, String iconBase) {
            this.displayName = displayName;
            this.icon = ImageUtilities.loadImageIcon(iconBase, false);
        }
        
        @Override public String getDisplayName() {return displayName;}
        @Override public Icon getIcon() {return icon;}
    }
    
    private List<TemplateRow> rows = new ArrayList<TemplateRow>();
    
    List<TemplateRow> getRows() {
        return rows;
    }
    
    void addRow() {
        int index = rows.size();
        rows.add(new TemplateRow());
        super.fireTableRowsInserted(index, index);
    }
    
    void deleteRows(int[] indices) {
        for(int index : toList(indices))
            rows.remove(index);
        fireTableDataChanged();
    }
    
    private List<Integer> toList(int[] indices) {
        List<Integer> result = new ArrayList<Integer>(indices.length);
        for(int i : indices)
            result.add(i);
        Collections.sort(result, Collections.reverseOrder());
        return result;
    }
    
    void moveUp(int index) {
        if(index > 0) {
            Collections.swap(rows, index, index-1);
            fireTableDataChanged();
        }
    }
    
    void moveDown(int index) {
        if(index < (rows.size()-1)) {
            Collections.swap(rows, index, index+1);
            fireTableDataChanged();
        }
    }
    
    @Override
    public int getRowCount() {
        return rows==null? 0 : rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }
    
    @Override
    public String getColumnName(int column) {
        switch(column) {
            case COL_REFERENCE: return Bundle.LBL_ImportTemplateModel_Reference();
            case COL_SOURCE_TYPE: return Bundle.LBL_ImportTemplateModel_SourceType();
            case COL_DATA_TYPE: return Bundle.LBL_ImportTemplateModel_DataType();
            case COL_CUMMULATED: return Bundle.LBL_ImportTemplateModel_Cummulated();
            case COL_START_DATE: return Bundle.LBL_ImportTemplateModel_StartDate();
            case COL_ACCIDENT_LENGTH: return Bundle.LBL_ImportTemplateModel_AccidentLength();
            case COL_DEVELOPMENT_LENGTH: return Bundle.LBL_ImportTemplateModel_DevelopmentLength();
            default: throw new IllegalArgumentException("Invalid column index: "+column);
        }
    }
    
    @Override
    public Class getColumnClass(int column) {
        switch(column) {
            case COL_REFERENCE: return String.class;
            case COL_SOURCE_TYPE: return SourceType.class;
            case COL_DATA_TYPE: return DataType.class;
            case COL_CUMMULATED: return Boolean.class;
            case COL_START_DATE: return MonthDate.class;
            case COL_ACCIDENT_LENGTH: return Integer.class;
            case COL_DEVELOPMENT_LENGTH: return Integer.class;
            default: throw new IllegalArgumentException("Invalid column index: "+column);
        }
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TemplateRow row = rows.get(rowIndex);
        switch(columnIndex) {
            case COL_REFERENCE: return row.reference;
            case COL_SOURCE_TYPE: return row.sourceType;
            case COL_DATA_TYPE: return row.dataType;
            case COL_CUMMULATED: return row.cummulated;
            case COL_START_DATE: return row.monthDate;
            case COL_ACCIDENT_LENGTH: return row.accidentLength;
            case COL_DEVELOPMENT_LENGTH: return row.developmentLength;
            default: throw new IllegalArgumentException("Invalid column index: "+columnIndex);
        }
    }

    class TemplateRow {
        private String reference;
        private SourceType sourceType = SourceType.TABLE;
        private DataType dataType = DataType.TRIANGLE;
        private MonthDate monthDate;
        private Integer accidentLength;
        private Integer developmentLength;
        private Boolean cummulated = false;
        
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
            if(SourceType.TRIANGLE != sourceType) {
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
    }
}
