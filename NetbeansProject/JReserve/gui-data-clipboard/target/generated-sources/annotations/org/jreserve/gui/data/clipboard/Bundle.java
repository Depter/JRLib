package org.jreserve.gui.data.clipboard;
/** Localizable strings for {@link org.jreserve.gui.data.clipboard}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>Paste</i>
     * @see ClipboardTable
     */
    static String CTL_ClipboardTable_Paste() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL.ClipboardTable.Paste");
    }
    /**
     * @return <i>Paste</i>
     * @see ImportClipboardTriangleVisualPanel
     */
    static String CTL_ImportClipboardTriangleVisualPanel_Paste() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL.ImportClipboardTriangleVisualPanel.Paste");
    }
    /**
     * @return <i>Clipboard Table</i>
     * @see ClipboardTableImportDataProvider
     */
    static String LBL_ClipboardTableImportDataProvider_Name() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.ClipboardTableImportDataProvider.Name");
    }
    /**
     * @return <i>Clipboard Triangle</i>
     * @see ClipboardTriangleImportDataProvider
     */
    static String LBL_ClipboardTriangleImportDataProvider_Name() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.ClipboardTriangleImportDataProvider.Name");
    }
    /**
     * @return <i>Paste Table</i>
     * @see ImportClipboardTableVisualPanel
     */
    static String LBL_ImportClipboardTableVisualPanel_Name() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.ImportClipboardTableVisualPanel.Name");
    }
    /**
     * @return <i>Paste Triangle</i>
     * @see ImportClipboardTriangleVisualPanel
     */
    static String LBL_ImportClipboardTriangleVisualPanel_Name() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.ImportClipboardTriangleVisualPanel.Name");
    }
    /**
     * @return <i>Date format is not set!</i>
     * @see ImportClipboardTableWizardPanel
     */
    static String MSG_ImportClipboardTableWizardPanel_DateFormat_Empty() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ImportClipboardTableWizardPanel.DateFormat.Empty");
    }
    /**
     * @param pattern pattern
     * @return <i>Date format ''</i>{@code pattern}<i>'' is invalid!</i>
     * @see ImportClipboardTableWizardPanel
     */
    static String MSG_ImportClipboardTableWizardPanel_DateFormat_Invalid(Object pattern) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ImportClipboardTableWizardPanel.DateFormat.Invalid", pattern);
    }
    /**
     * @param str str
     * @param row row
     * @param column column
     * @return <i>''</i>{@code str}<i>'' at [</i>{@code row}<i>, </i>{@code column}<i>] can not be parsed as a date!</i>
     * @see ImportClipboardTableWizardPanel
     */
    static String MSG_ImportClipboardTableWizardPanel_Table_Date_Invalid(Object str, Object row, Object column) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ImportClipboardTableWizardPanel.Table.Date.Invalid", str, row, column);
    }
    /**
     * @param row2 row2
     * @param row1 row1
     * @return <i>Row ''</i>{@code row2}<i>'' is the dupplicate of row ''</i>{@code row1}<i>''!</i>
     * @see ImportClipboardTableWizardPanel
     */
    static String MSG_ImportClipboardTableWizardPanel_Table_DuplicateRow(Object row2, Object row1) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ImportClipboardTableWizardPanel.Table.DuplicateRow", row2, row1);
    }
    /**
     * @return <i>No input is pasted!</i>
     * @see ImportClipboardTableWizardPanel
     */
    static String MSG_ImportClipboardTableWizardPanel_Table_Empty() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ImportClipboardTableWizardPanel.Table.Empty");
    }
    /**
     * @param rowIndex rowIndex
     * @param cCount cCount
     * @return <i>Row ''</i>{@code rowIndex}<i>'' must have at least ''</i>{@code cCount}<i>'' columns!</i>
     * @see ImportClipboardTableWizardPanel
     */
    static String MSG_ImportClipboardTableWizardPanel_Table_EmptyRow(Object rowIndex, Object cCount) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ImportClipboardTableWizardPanel.Table.EmptyRow", rowIndex, cCount);
    }
    /**
     * @param str str
     * @param row row
     * @param column column
     * @return <i>''</i>{@code str}<i>'' at [</i>{@code row}<i>, </i>{@code column}<i>]  can not be parsed as a number!</i>
     * @see ImportClipboardTableWizardPanel
     */
    static String MSG_ImportClipboardTableWizardPanel_Table_Value_Invalid(Object str, Object row, Object column) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ImportClipboardTableWizardPanel.Table.Value.Invalid", str, row, column);
    }
    /**
     * @param row row
     * @return <i>Accident and development date is different in row ''</i>{@code row}<i>''!</i>
     * @see ImportClipboardTableWizardPanel
     */
    static String MSG_ImportClipboardTableWizardPanel_Table_VectorDates(Object row) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ImportClipboardTableWizardPanel.Table.VectorDates", row);
    }
    /**
     * @return <i>No data is selected!</i>
     * @see ImportClipboardTriangleWizardPanel
     */
    static String MSG_ImportClipboardTriangleWizardPanel_NoData() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ImportClipboardTriangleWizardPanel.NoData");
    }
    /**
     * @param value value
     * @param row row
     * @param column column
     * @return <i>Unable to parse value ''</i>{@code value}<i>'' in cell [</i>{@code row}<i>, </i>{@code column}<i>]!</i>
     * @see ImportClipboardTriangleWizardPanel
     */
    static String MSG_ImportClipboardTriangleWizardPanel_ParseError(Object value, Object row, Object column) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ImportClipboardTriangleWizardPanel.ParseError", value, row, column);
    }
    private void Bundle() {}
}
