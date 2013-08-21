package org.jreserve.gui.project.api;
/** Localizable strings for {@link org.jreserve.gui.project.api}. */
@javax.annotation.Generated(value="org.netbeans.modules.openide.util.NbBundleProcessor")
class Bundle {
    /**
     * @return <i>Project created</i>
     * @see ProjectEvent
     */
    static String MSG_ProjectEvent_ProjectCreated() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ProjectEvent.ProjectCreated");
    }
    /**
     * @param propertyName propertyName
     * @param oldValue oldValue
     * @param newValue newValue
     * @return <i>Project property "</i>{@code propertyName}<i>" changed from "</i>{@code oldValue}<i>" to "</i>{@code newValue}<i>".</i>
     * @see ProjectEvent
     */
    static String MSG_ProjectEvent_ProjectPropertyChanged(Object propertyName, Object oldValue, Object newValue) {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG.ProjectEvent.ProjectPropertyChanged", propertyName, oldValue, newValue);
    }
    private void Bundle() {}
}
