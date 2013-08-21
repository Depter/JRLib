package org.jreserve.gui.data.actions;
/** Localizable strings for {@link org.jreserve.gui.data.actions}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>New Category</i>
     * @see CreateDataCategoryAction
     */
    static String CTL_CreateDataCategoryAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_CreateDataCategoryAction");
    }
    /**
     * @return <i>New Storage</i>
     * @see CreateDataSourceAction
     */
    static String CTL_CreateDataSourceAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_CreateDataSourceAction");
    }
    /**
     * @return <i>Delete Items</i>
     * @see DeleteDataCategoryAction
     */
    static String CTL_DeleteDataCategoryAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_DeleteDataCategoryAction");
    }
    /**
     * @return <i>Import Data</i>
     * @see ImportDataAction
     */
    static String CTL_ImportDataAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL.ImportDataAction");
    }
    /**
     * @return <i>Category Name</i>
     * @see CreateDataCategoryDialog
     */
    static String LBL_CreateDataCategoryDialog_NameText_Prompt() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.CreateDataCategoryDialog.NameText.Prompt");
    }
    /**
     * @return <i>New Data Category</i>
     * @see CreateDataCategoryDialog
     */
    static String LBL_CreateDataCategoryDialog_Title() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.CreateDataCategoryDialog.Title");
    }
    /**
     * @return <i>New Data Source</i>
     * @see CreateDataSourceAction
     */
    static String LBL_CreateDataSourceAction_Wizard_Title() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.CreateDataSourceAction.Wizard.Title");
    }
    /**
     * @return <i>Delete Data Items</i>
     * @see DeleteDataCategoryDialog
     */
    static String LBL_DeleteDataCategoryDialog_Title() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.DeleteDataCategoryDialog.Title");
    }
    /**
     * @return <i>Import Data</i>
     * @see ImportDataAction
     */
    static String LBL_ImportDataAction_WizardTitle() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.ImportDataAction.WizardTitle");
    }
    /**
     * @return <i>Unable to create data category.</i>
     * @see CreateDataCategoryAction
     */
    static String MSG_CreateDataCategoryAction_Create_Error() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CreateDataCategoryAction.Create.Error");
    }
    /**
     * @return <i>Name is not set!</i>
     * @see CreateDataCategoryDialog
     */
    static String MSG_CreateDataCategoryDialog_Name_Empty() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CreateDataCategoryDialog.Name.Empty");
    }
    /**
     * @param name name
     * @return <i>Name "</i>{@code name}<i>" already exists!</i>
     * @see CreateDataCategoryDialog
     */
    static String MSG_CreateDataCategoryDialog_Name_Exists(Object name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CreateDataCategoryDialog.Name.Exists", name);
    }
    /**
     * @param name name
     * @return <i>Name "</i>{@code name}<i>" is invalid!</i>
     * @see CreateDataCategoryDialog
     */
    static String MSG_CreateDataCategoryDialog_Name_Invalid(Object name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CreateDataCategoryDialog.Name.Invalid", name);
    }
    /**
     * @return <i>Parent category not found!</i>
     * @see CreateDataCategoryDialog
     */
    static String MSG_CreateDataCategoryDialog_Parent_Empty() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CreateDataCategoryDialog.Parent.Empty");
    }
    /**
     * @return <i>Can not delete "Data" category!</i>
     * @see DeleteDataCategoryAction
     */
    static String MSG_DeleteDataCategoryAction_ContainsRoot() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DeleteDataCategoryAction.ContainsRoot");
    }
    /**
     * @param path path
     * @return <i>Unable to delete: </i>{@code path}
     * @see DeleteDataCategoryDialog
     */
    static String MSG_DeleteDataCategoryDialog_Delete_Error(Object path) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DeleteDataCategoryDialog.Delete.Error", path);
    }
    private void Bundle() {}
}
