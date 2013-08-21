package org.jreserve.gui.data.api.inport;
/** Localizable strings for {@link org.jreserve.gui.data.api.inport}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>Override existing values</i>
     * @see SaveType
     */
    static String LBL_SaveType_Override() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.SaveType.Override");
    }
    /**
     * @return <i>Keep existing values</i>
     * @see SaveType
     */
    static String LBL_SaveType_SaveNew() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.SaveType.SaveNew");
    }
    /**
     * @param accident accident
     * @param development development
     * @return <i>Input contains duplicate entries for </i>{@code accident}<i>/</i>{@code development}<i>!</i>
     * @see ImportDataWizardPanelLast
     */
    static String MSG_ImportDataWizardPanelLast_DuplicateEntries(Object accident, Object development) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ImportDataWizardPanelLast.DuplicateEntries", accident, development);
    }
    /**
     * @return <i>There is no new data to save!</i>
     * @see ImportDataWizardPanelLast
     */
    static String MSG_ImportDataWizardPanelLast_NoEntries() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ImportDataWizardPanelLast.NoEntries");
    }
    private void Bundle() {}
}
