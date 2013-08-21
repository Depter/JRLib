package org.jreserve.gui.data.spi;
/** Localizable strings for {@link org.jreserve.gui.data.spi}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @param path path
     * @return <i>Data deleted from ''</i>{@code path}<i>''!</i>
     * @see AbstractDataProvider
     */
    static String MSG_AbstractDataProvider_Deleted(Object path) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.AbstractDataProvider.Deleted", path);
    }
    /**
     * @param path path
     * @return <i>Data imported into ''</i>{@code path}<i>'', keeping old values!</i>
     * @see AbstractDataProvider
     */
    static String MSG_AbstractDataProvider_Imported_OnlyNew(Object path) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.AbstractDataProvider.Imported.OnlyNew", path);
    }
    /**
     * @param path path
     * @return <i>Data imported into ''</i>{@code path}<i>'', overwriting old values!</i>
     * @see AbstractDataProvider
     */
    static String MSG_AbstractDataProvider_Imported_Overwrite(Object path) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.AbstractDataProvider.Imported.Overwrite", path);
    }
    private void Bundle() {}
}
