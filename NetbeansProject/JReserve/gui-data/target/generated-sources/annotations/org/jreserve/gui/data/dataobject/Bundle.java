package org.jreserve.gui.data.dataobject;
/** Localizable strings for {@link org.jreserve.gui.data.dataobject}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>DataSource File</i>
     * @see DataSourceDataObject
     */
    static String LBL_DataSourceDataObject_Loader() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.DataSourceDataObject.Loader");
    }
    /**
     * @return <i>Data</i>
     * @see DataSourceObjectProvider
     */
    static String LBL_DataSourceObjectProvider_Name() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.DataSourceObjectProvider.Name");
    }
    /**
     * @return <i>Audit</i>
     * @see DataSourceDataObject
     */
    static String LBL_EditorUtil_DataSourceAudit() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.EditorUtil.DataSourceAudit");
    }
    /**
     * @return <i>Storage created</i>
     * @see DataEventUtil
     */
    static String MSG_DataEventUtil_Created() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataEventUtil.Created");
    }
    /**
     * @param source source
     * @return <i>Storage created from ''</i>{@code source}<i>''.</i>
     * @see DataEventUtil
     */
    static String MSG_DataEventUtil_CreatedFrom(Object source) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataEventUtil.CreatedFrom", source);
    }
    /**
     * @return <i>Data deleted.</i>
     * @see DataEventUtil
     */
    static String MSG_DataEventUtil_DataDelete() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataEventUtil.DataDelete");
    }
    /**
     * @return <i>Data imported.</i>
     * @see DataEventUtil
     */
    static String MSG_DataEventUtil_DataImport() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataEventUtil.DataImport");
    }
    /**
     * @return <i>Storage deleted</i>
     * @see DataEventUtil
     */
    static String MSG_DataEventUtil_Deleted() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataEventUtil.Deleted");
    }
    /**
     * @param oldName oldName
     * @param newName newName
     * @return <i>Storage renamed ''</i>{@code oldName}<i>'' => ''</i>{@code newName}<i>''.</i>
     * @see DataEventUtil
     */
    static String MSG_DataEventUtil_Renamed(Object oldName, Object newName) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataEventUtil.Renamed", oldName, newName);
    }
    private void Bundle() {}
}
