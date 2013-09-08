package org.jreserve.gui.data.api.wizard;
/** Localizable strings for {@link org.jreserve.gui.data.api.wizard}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @param current current
     * @param total total
     * @return {@code current}<i> of </i>{@code total}
     * @see AbstractDataSourceWizardIterator
     */
    static String LBL_AbstractDataSourceWizardIterator_Name(Object current, Object total) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.AbstractDataSourceWizardIterator.Name", current, total);
    }
    /**
     * @return <i>Name &amp; Location</i>
     * @see DataSourceNameVisualPanel
     */
    static String LBL_DataSourceNameVisualPanel_Name() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.DataSourceNameVisualPanel.Name");
    }
    /**
     * @return <i>DataProvider not present in the given project!</i>
     * @see DataSourceNameWizardPanel
     */
    static String MSG_DataSourceNameWizardPanel_NoDOP() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataSourceNameWizardPanel.NoDOP");
    }
    /**
     * @return <i>Type not selected!</i>
     * @see DataSourceNameWizardPanel
     */
    static String MSG_DataSourceNameWizardPanel_NoDataType() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataSourceNameWizardPanel.NoDataType");
    }
    /**
     * @return <i>Name is not selected!</i>
     * @see DataSourceNameWizardPanel
     */
    static String MSG_DataSourceNameWizardPanel_NoName() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataSourceNameWizardPanel.NoName");
    }
    /**
     * @return <i>Data source already exists!</i>
     * @see DataSourceNameWizardPanel
     */
    static String MSG_DataSourceNameWizardPanel_PathExists() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataSourceNameWizardPanel.PathExists");
    }
    /**
     * @param root root
     * @return <i>Data sources must be within ''</i>{@code root}<i>'' folder!</i>
     * @see DataSourceNameWizardPanel
     */
    static String MSG_DataSourceNameWizardPanel_PathNotInRoot(Object root) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataSourceNameWizardPanel.PathNotInRoot", root);
    }
    private void Bundle() {}
}
