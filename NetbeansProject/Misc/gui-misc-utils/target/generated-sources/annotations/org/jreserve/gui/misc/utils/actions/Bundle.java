package org.jreserve.gui.misc.utils.actions;
/** Localizable strings for {@link org.jreserve.gui.misc.utils.actions}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>Copy</i>
     * @see CopyAction
     */
    static String CTL_CopyAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_CopyAction");
    }
    /**
     * @return <i>Cut</i>
     * @see CutAction
     */
    static String CTL_CutAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_CutAction");
    }
    /**
     * @return <i>Delete</i>
     * @see DeleteAction
     */
    static String CTL_DeleteAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL.DeleteAction");
    }
    /**
     * @return <i>Paste</i>
     * @see PasteAction
     */
    static String CTL_PasteAction() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_PasteAction");
    }
    /**
     * @return <i>Paste</i>
     * @see ClipboardUtil
     */
    static String LBL_ClipboardUtil_Copy_Name() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.ClipboardUtil.Copy.Name");
    }
    /**
     * @return <i>Paste</i>
     * @see ClipboardUtil
     */
    static String LBL_ClipboardUtil_Move_Name() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.ClipboardUtil.Move.Name");
    }
    /**
     * @return <i>Cancel</i>
     * @see DeleteDialog
     */
    static String LBL_DeleteDialog_Cancel() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.DeleteDialog.Cancel");
    }
    /**
     * @return <i>Ok</i>
     * @see DeleteDialog
     */
    static String LBL_DeleteDialog_Ok() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.DeleteDialog.Ok");
    }
    /**
     * @return <i>Do you want to delete the following items?</i>
     * @see DeleteDialog
     */
    static String LBL_DeleteDialog_Question() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.DeleteDialog.Question");
    }
    /**
     * @return <i>Delete Items</i>
     * @see DeleteDialog
     */
    static String LBL_DeleteDialog_Title() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.DeleteDialog.Title");
    }
    /**
     * @param path path
     * @return <i>Unable to copy '</i>{@code path}<i>'</i>
     * @see ClipboardUtil
     */
    static String MSG_ClipboardUtil_Copy_Error(Object path) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ClipboardUtil.Copy.Error", path);
    }
    /**
     * @param path path
     * @return <i>Unable to move '</i>{@code path}<i>'</i>
     * @see ClipboardUtil
     */
    static String MSG_ClipboardUtil_Move_Error(Object path) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ClipboardUtil.Move.Error", path);
    }
    /**
     * @param path path
     * @return <i>Pasting '</i>{@code path}<i>'</i>
     * @see ClipboardUtil
     */
    static String MSG_ClipboardUtil_Pasting(Object path) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ClipboardUtil.Pasting", path);
    }
    /**
     * @return <i>Copy to clipboard failed!</i>
     * @see CopyAction
     */
    static String MSG_CopyAction_Error() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CopyAction.Error");
    }
    /**
     * @return <i>Cut to clipboard failed!</i>
     * @see CutAction
     */
    static String MSG_CutAction_Error() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CutAction.Error");
    }
    /**
     * @param name name
     * @return <i>Unable to delete: </i>{@code name}
     * @see DeleteDialog
     */
    static String MSG_DeleteDialog_DeleteError(Object name) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.DeleteDialog.DeleteError", name);
    }
    private void Bundle() {}
}
