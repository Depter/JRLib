package org.jreserve.gui.misc.utils.dataobject.impl;
/** Localizable strings for {@link org.jreserve.gui.misc.utils.dataobject.impl}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>Folder</i>
     * @see CreateDataFolderIterator
     */
    static String LBL_CreateDataFolderIterator_DisplayName() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.CreateDataFolderIterator.DisplayName");
    }
    /**
     * @param current current
     * @param total total
     * @return {@code current}<i> of </i>{@code total}
     * @see CreateDataFolderIterator
     */
    static String LBL_CreateDataFolderIterator_Name(Object current, Object total) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.CreateDataFolderIterator.Name", current, total);
    }
    /**
     * @return <i>Name of the folder</i>
     * @see CreateFolderVisualPanel
     */
    static String LBL_CreateFolderVisualPanel_Prompt_Name() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.CreateFolderVisualPanel.Prompt.Name");
    }
    /**
     * @return <i>Path to parent folder</i>
     * @see CreateFolderVisualPanel
     */
    static String LBL_CreateFolderVisualPanel_Prompt_ParentFolder() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.CreateFolderVisualPanel.Prompt.ParentFolder");
    }
    /**
     * @return <i>Create Folder</i>
     * @see CreateFolderVisualPanel
     */
    static String LBL_CreateFolderVisualPanel_Title() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.CreateFolderVisualPanel.Title");
    }
    /**
     * @return <i>Folder already exists!</i>
     * @see CreateFolderWizardPanel
     */
    static String MSG_CreateFolderWizardPanel_FolderExists() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CreateFolderWizardPanel.FolderExists");
    }
    /**
     * @return <i>Name is not selected!</i>
     * @see CreateFolderWizardPanel
     */
    static String MSG_CreateFolderWizardPanel_NameEmpty() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CreateFolderWizardPanel.NameEmpty");
    }
    /**
     * @return <i>Project not selected!</i>
     * @see CreateFolderWizardPanel
     */
    static String MSG_CreateFolderWizardPanel_ProjectEmpty() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CreateFolderWizardPanel.ProjectEmpty");
    }
    /**
     * @return <i>Location not selected!</i>
     * @see CreateFolderWizardPanel
     */
    static String MSG_CreateFolderWizardPanel_ProviderEmpty() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CreateFolderWizardPanel.ProviderEmpty");
    }
    /**
     * @param root root
     * @return <i>New Folder must be within ''</i>{@code root}<i>''!</i>
     * @see CreateFolderWizardPanel
     */
    static String MSG_CreateFolderWizardPanel_WrongParent(Object root) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.CreateFolderWizardPanel.WrongParent", root);
    }
    private void Bundle() {}
}
