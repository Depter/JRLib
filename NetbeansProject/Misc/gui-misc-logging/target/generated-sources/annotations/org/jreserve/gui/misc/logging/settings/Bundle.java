package org.jreserve.gui.misc.logging.settings;
/** Localizable strings for {@link org.jreserve.gui.misc.logging.settings}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>Logging</i>
     * @see LoggingOptionsPanelController
     */
    static String AdvancedOption_DisplayName_Logging() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "AdvancedOption_DisplayName_Logging");
    }
    /**
     * @return <i>log;logging</i>
     * @see LoggingOptionsPanelController
     */
    static String AdvancedOption_Keywords_Logging() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "AdvancedOption_Keywords_Logging");
    }
    /**
     * @return <i>Add</i>
     * @see AddLoggerDialog
     */
    static String CTL_AddLoggerDialog_add() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL.AddLoggerDialog.add");
    }
    /**
     * @return <i>Cancel</i>
     * @see AddLoggerDialog
     */
    static String CTL_AddLoggerDialog_cancel() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL.AddLoggerDialog.cancel");
    }
    /**
     * @return <i>Add</i>
     * @see LoggingPanel
     */
    static String CTL_LoggingPanel_add() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL.LoggingPanel.add");
    }
    /**
     * @return <i>Reset default</i>
     * @see LoggingPanel
     */
    static String CTL_LoggingPanel_default() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL.LoggingPanel.default");
    }
    /**
     * @return <i>Delete</i>
     * @see LoggingPanel
     */
    static String CTL_LoggingPanel_delete() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL.LoggingPanel.delete");
    }
    /**
     * @return <i>Level:</i>
     * @see AddLoggerDialog
     */
    static String LBL_AddLoggerDialog_level() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.AddLoggerDialog.level");
    }
    /**
     * @return <i>Logger:</i>
     * @see AddLoggerDialog
     */
    static String LBL_AddLoggerDialog_logger() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.AddLoggerDialog.logger");
    }
    /**
     * @return <i>logger.name</i>
     * @see AddLoggerDialog
     */
    static String LBL_AddLoggerDialog_logger_prompt() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.AddLoggerDialog.logger.prompt");
    }
    /**
     * @return <i>Configure logger</i>
     * @see AddLoggerDialog
     */
    static String LBL_AddLoggerDialog_title() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.AddLoggerDialog.title");
    }
    /**
     * @return <i>Level</i>
     * @see LogLevelTableModel
     */
    static String LBL_LogLevelTableModel_level() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.LogLevelTableModel.level");
    }
    /**
     * @return <i>Logger</i>
     * @see LogLevelTableModel
     */
    static String LBL_LogLevelTableModel_logger() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.LogLevelTableModel.logger");
    }
    /**
     * @return <i>Level:</i>
     * @see LoggingPanel
     */
    static String LBL_LoggingPanel_level() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.LoggingPanel.level");
    }
    /**
     * @return <i>Show log:</i>
     * @see LoggingPanel
     */
    static String LBL_LoggingPanel_showLog() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL.LoggingPanel.showLog");
    }
    /**
     * @return <i>All loggers</i>
     * @see AddLoggerDialog
     */
    static String MSG_AddLoggerDialog_allloggers() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.AddLoggerDialog.allloggers");
    }
    /**
     * @return <i>Field 'Level' is empty!</i>
     * @see AddLoggerDialog
     */
    static String MSG_AddLoggerDialog_level_empty() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.AddLoggerDialog.level.empty");
    }
    /**
     * @param the_level_typed_by_the_user the level, typed by the user
     * @return <i>Level "</i>{@code the_level_typed_by_the_user}<i>" is invalid!</i>
     * @see AddLoggerDialog
     */
    static String MSG_AddLoggerDialog_level_invalid(Object the_level_typed_by_the_user) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.AddLoggerDialog.level.invalid", the_level_typed_by_the_user);
    }
    /**
     * @return <i>Field 'Logger' is empty!</i>
     * @see AddLoggerDialog
     */
    static String MSG_AddLoggerDialog_logger_empty() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.AddLoggerDialog.logger.empty");
    }
    /**
     * @return <i>Name of logger can not end with '.'!</i>
     * @see AddLoggerDialog
     */
    static String MSG_AddLoggerDialog_logger_point() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.AddLoggerDialog.logger.point");
    }
    /**
     * @param level_typed_by_the_user level, typed by the user
     * @return <i>Value "</i>{@code level_typed_by_the_user}<i>" is not a valid level.<br>Use a number or one of the following values 'OFF', 'SEVERE','WARNING', 'INFO', 'CONFIG', 'FINE', 'FINER', 'FINEST' or 'ALL'</i>
     * @see LogLevelTableModel
     */
    static String MSG_LogLevelTableModel_wronglevel(Object level_typed_by_the_user) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.LogLevelTableModel.wronglevel", level_typed_by_the_user);
    }
    private void Bundle() {}
}
