package org.jreserve.gui.excel.template.dataimport;
/** Localizable strings for {@link org.jreserve.gui.excel.template.dataimport}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>Create Data Import Tempalte</i>
     * @see DataImportTemplateBuilder
     */
    static String LBL_DataImportTemplateBuilder_WizardTitle() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.DataImportTemplateBuilder.WizardTitle");
    }
    /**
     * @return <i>Can not rename template!</i>
     * @see DataImportTemplate
     */
    static String MSG_DataImportTemplate_Rename_Error() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataImportTemplate.Rename.Error");
    }
    /**
     * @param name name
     * @return <i>Name ''</i>{@code name}<i>'' already exists!!</i>
     * @see DataImportTemplate
     */
    static String MSG_DataImportTemplate_Rename_NameExists(Object name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DataImportTemplate.Rename.NameExists", name);
    }
    /**
     * @return <i>Unable to load template!</i>
     * @see TemplateLoader
     */
    static String MSG_TemplateLoader_LoadError() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TemplateLoader.LoadError");
    }
    /**
     * @param name name
     * @return <i>Unable to save template ''</i>{@code name}<i>''!</i>
     * @see TemplateLoader
     */
    static String MSG_TemplateLoader_SaveError(Object name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.TemplateLoader.SaveError", name);
    }
    private void Bundle() {}
}
