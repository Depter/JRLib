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

package org.jreserve.gui.excel.dataimport;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.apache.poi.ss.util.CellReference;
import org.jreserve.gui.data.api.DataItem;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.api.inport.SaveType;
import org.jreserve.gui.data.spi.ImportDataProvider;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplate;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplateItem;
import org.jreserve.gui.poi.read.PoiUtil;
import org.jreserve.gui.poi.read.ReferenceUtil;
import org.jreserve.gui.poi.read.TableFactory;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.ExcelTemplateImportWizardPanel2.SaveType.Empty=Save Type not selected!",
    "MSG.ExcelTemplateImportWizardPanel2.Links.Empty=No reference is selected!",
    "MSG.ExcelTemplateImportWizardPanel2.ReadError=Unable to read file!",
    "# {0} - row",
    "MSG.ExcelTemplateImportWizardPanel2.Links.NoDataSource=Storage not selected in row {0}!"
})
class ExcelTemplateImportWizardPanel2 implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private ExcelTemplateImportVisualPanel2 component;
    private final ChangeSupport cs = new ChangeSupport(this);
    private boolean valid = false;
    private WizardDescriptor wiz;
    private InputData inputData;
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new ExcelTemplateImportVisualPanel2(this);
            if(wiz != null)
                initComponent();
        }
        return component;
    }
    
    private void initComponent() {
        DataImportTemplate template = (DataImportTemplate) wiz.getProperty(ExcelTemplateImportDataProvider.PROP_TEMPLATE);
        component.setItems(template.getItems());
        
        DataItem item = (DataItem) wiz.getProperty(ImportDataProvider.PROP_INIT_DATA_ITEM);
        if(item != null)
            component.setDataManager(item.getDataManager());
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
        if(component != null)
            initComponent();
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
        settings.putProperty(ImportDataProvider.PROP_SHOULD_IMPORT_DATA, Boolean.FALSE);
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        cs.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        cs.removeChangeListener(l);
    }

    void changed() {
        valid = isInputValid();
        if(valid)
            showError(null);
        cs.fireChange();
    }
    
    private void showError(String msg) {
        if(wiz != null)
            wiz.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private boolean isInputValid() {
        return isSaveTypeSelected() && isLinksValid();
    }
    
    private boolean isSaveTypeSelected() {
        if(component.getSaveType() == null) {
            showError(Bundle.MSG_ExcelTemplateImportWizardPanel2_SaveType_Empty());
            return false;
        }
        return true;
    }
    
    private boolean isLinksValid() {
        List<DataImportTemplateItem> items = component.getSelectedItems();
        if(items.isEmpty()) {
            showError(Bundle.MSG_ExcelTemplateImportWizardPanel2_Links_Empty());
            return false;
        }
        
        int size = items.size();
        for(int r=0; r<size; r++) {
            if(component.getDataSource(items.get(r)) == null) {
                showError(Bundle.MSG_ExcelTemplateImportWizardPanel2_Links_NoDataSource(r+1));
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public void prepareValidation() {
        inputData = new InputData();
        component.setProcessRunning(true);
    }

    @Override
    public void validate() throws WizardValidationException {
        ReferenceUtil refUtil = getReferenceUtil();
        
        Exception lex = null;
        for(Entry<DataImportTemplateItem, DataSource> entry : inputData.links.entrySet()) {
            DataImportTemplateItem item = entry.getKey();
            try {
                if(item instanceof DataImportTemplateItem.Table)
                    readTable(refUtil, (DataImportTemplateItem.Table)item);
                else
                    readTriangle(refUtil, (DataImportTemplateItem.Triangle)item);
                setSuccess(item, null);
            } catch (Exception ex) {
                setSuccess(item, ex);
                lex = ex;
            }
        }
        stopProcessRunning();
        if(lex != null)
            throw new WizardValidationException(component, lex.getMessage(), lex.getLocalizedMessage());
    }
    
    private ReferenceUtil getReferenceUtil() throws WizardValidationException {
        try {
            return PoiUtil.getReferenceUtil(inputData.file);
        } catch (Exception ex) {
            String lMsg = Bundle.MSG_ExcelTemplateImportWizardPanel2_ReadError();
            throw new WizardValidationException(component, ex.getMessage(), lMsg);
        }
    }
    
    private void readTable(ReferenceUtil refUtil, DataImportTemplateItem.Table item) throws Exception {
        TableFactory<List<DataEntry>> factory = (DataType.VECTOR == item.getDataType())?
                new VectorDataEntryTableReader() : new TriangleDataEntryTableReader();
        CellReference ref = refUtil.toCellReference(item.getReference());
        List<DataEntry> entries = PoiUtil.read(inputData.file, ref, factory);
        inputData.links.get(item).addEntries(new TreeSet<DataEntry>(entries), inputData.saveType);
    }
    
    private void readTriangle(ReferenceUtil refUtil, DataImportTemplateItem.Triangle item) throws Exception {
        double[][] values = readValues(refUtil, item);
        Set<DataEntry> entries = createEntries(values, item);
        inputData.links.get(item).addEntries(entries, inputData.saveType);
    }
    
    private double[][] readValues(ReferenceUtil refUtil, DataImportTemplateItem.Triangle item) throws IOException {
        TableFactory<double[][]> factory = new TriangleTableReader();
        CellReference ref = refUtil.toCellReference(item.getReference());
        return PoiUtil.read(inputData.file, ref, factory);
    }
    
    private Set<DataEntry> createEntries(double[][] values, DataImportTemplateItem.Triangle item) {
        MonthDate startDate = item.getStartDate();
        int aLength = item.getAccidentLength();
        int dLength = item.getDevelopmentLength();
        return createEntries(values, startDate, aLength, dLength);
    }

    private Set<DataEntry> createEntries(double[][] values, MonthDate startDate, int aLength, int dLength) {
        Set<DataEntry> result = new LinkedHashSet<DataEntry>();
        int rCount = values.length;
        for(int a=0; a<rCount; a++) {
            double[] row = values[a];
            int cCount = row.length;
            for(int d=0; d<cCount; d++) {
                MonthDate accident = startDate.addMonth(a * aLength);
                MonthDate development = accident.addMonth(d * dLength);
                result.add(new DataEntry(accident, development, values[a][d]));
            }
        }
        return result;
    }
    
    private void setSuccess(final DataImportTemplateItem item, final Exception ex) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                component.setSuccess(item, ex);
            }
        });
    }
    
    private void stopProcessRunning() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                component.setProcessRunning(false);
            }
        });
    }
    
    private class InputData {
        private final File file;
        private final SaveType saveType;
        private final Map<DataImportTemplateItem, DataSource> links;
        
        private InputData() {
            file = (File) wiz.getProperty(ExcelTemplateImportDataProvider.PROP_IMPORT_FILE);
            saveType = component.getSaveType();
            
            links = new LinkedHashMap<DataImportTemplateItem, DataSource>();
            for(DataImportTemplateItem item : component.getSelectedItems())
                links.put(item, component.getDataSource(item));
        }
    }
    
    
}
