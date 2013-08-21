package org.jreserve.gui.data.api.impl;
/** Localizable strings for {@link org.jreserve.gui.data.api.impl}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>Data category created.</i>
     * @see DataEventUtil
     */
    static String MSG_DataEvent_DataCategory_Created() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataEvent.DataCategory.Created");
    }
    /**
     * @return <i>Data category deleted.</i>
     * @see DataEventUtil
     */
    static String MSG_DataEvent_DataCategory_Deleted() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataEvent.DataCategory.Deleted");
    }
    /**
     * @param oldPath oldPath
     * @param newPath newPath
     * @return <i>Data category renamed '</i>{@code oldPath}<i>' -> '</i>{@code newPath}<i>'.</i>
     * @see DataEventUtil
     */
    static String MSG_DataEvent_DataCategory_Renamed(Object oldPath, Object newPath) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataEvent.DataCategory.Renamed", oldPath, newPath);
    }
    /**
     * @return <i>Data storage created.</i>
     * @see DataEventUtil
     */
    static String MSG_DataEvent_DataSource_Created() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataEvent.DataSource.Created");
    }
    /**
     * @return <i>Data storage deleted.</i>
     * @see DataEventUtil
     */
    static String MSG_DataEvent_DataSource_Deleted() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataEvent.DataSource.Deleted");
    }
    /**
     * @param oldPath oldPath
     * @param newPath newPath
     * @return <i>Data storage renamed '</i>{@code oldPath}<i>' -> '</i>{@code newPath}<i>'.</i>
     * @see DataEventUtil
     */
    static String MSG_DataEvent_DataSource_Renamed(Object oldPath, Object newPath) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataEvent.DataSource.Renamed", oldPath, newPath);
    }
    /**
     * @param id id
     * @return <i>Data provider for id ''</i>{@code id}<i>'' not found!</i>
     * @see DataProviderFactoryRegistry
     */
    static String MSG_DataProviderFactoryRegistry_NoFactory(Object id) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataProviderFactoryRegistry.NoFactory", id);
    }
    /**
     * @param path path
     * @return <i>Unable to load data provider for ''</i>{@code path}<i>''!</i>
     * @see DataSourceUtil
     */
    static String MSG_DataSourceUtil_ProviderCreate_Error(Object path) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataSourceUtil.ProviderCreate.Error", path);
    }
    private void Bundle() {}
}
