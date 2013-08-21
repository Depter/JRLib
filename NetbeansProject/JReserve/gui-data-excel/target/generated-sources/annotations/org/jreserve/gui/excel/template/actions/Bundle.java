package org.jreserve.gui.excel.template.actions;
/** Localizable strings for {@link org.jreserve.gui.excel.template.actions}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>New Template</i>
     * @see CreateTemplateAction
     */
    static String CTL_CreateTemplateAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_CreateTemplateAction");
    }
    /**
     * @return <i>Delete Template</i>
     * @see DeleteTemplateAction
     */
    static String CTL_DeleteTemplateAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_DeleteTemplateAction");
    }
    /**
     * @return <i>Edit Template</i>
     * @see EditTemplateAction
     */
    static String CTL_EditTemplateAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_EditTemplateAction");
    }
    /**
     * @return <i>Delete Excel Templates</i>
     * @see DeleteTemplateDialog
     */
    static String LBL_DeleteTemplateDialog_Title() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.DeleteTemplateDialog.Title");
    }
    /**
     * @param path path
     * @return <i>Unable to delete: </i>{@code path}
     * @see DeleteTemplateDialog
     */
    static String MSG_DeleteTemplateDialog_Delete_Error(Object path) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DeleteTemplateDialog.Delete.Error", path);
    }
    private void Bundle() {}
}
