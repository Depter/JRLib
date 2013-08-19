/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.officelaf.util;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UIBootstrapValue implements UIDefaults.LazyValue {
    private boolean installed = false;
    private final String uiClassName;
    protected Object[] defaults;
    /** Creates a new instance of UIBootstrapValue */
    public UIBootstrapValue(String uiClassName, Object[] defaults) {
        this.defaults = defaults;
        this.uiClassName = uiClassName;
    }
    
    /** Create the value that UIDefaults will return.  If the keys and values
     * the UI class we're representing requires have not yet been installed,
     * this will install them.
     */
    @Override
    public Object createValue(UIDefaults uidefaults) {
        if (!installed) {
            installKeysAndValues(uidefaults);
        }
        return uiClassName;
    }
    
    /** Install the defaults the UI we're representing will need to function */
    private void installKeysAndValues(UIDefaults ui) {
        ui.putDefaults (getKeysAndValues());
        installed = true;
    }

    public Object[] getKeysAndValues() {
        return defaults;
    }

    public void uninstall() {
        if (defaults == null) {
            return;
        }
        for (int i=0; i < defaults.length; i+=2) {
            UIManager.put (defaults[i], null);
        }
        //null defaults so a Meta instance won't cause us to do work twice
        defaults = null;
    }

    @Override
    public String toString() {
        return getClass() + " for " + uiClassName; //NOI18N
    }

    /** Create another entry value to put in UIDefaults, which will also
     * trigger installing the keys and values, to ensure that they are only
     * added once, by whichever entry is asked for the value first. */
    public UIDefaults.LazyValue createShared (String uiClassName) {
        return new Meta (uiClassName);
    }
    
    private class Meta implements UIDefaults.LazyValue {
        private String name;
        public Meta (String uiClassName) {
            this.name = uiClassName;
        }
        
        @Override
        public Object createValue(javax.swing.UIDefaults uidefaults) {
            if (!installed) {
                installKeysAndValues(uidefaults);
            }
            return name;
        }

        @Override
        public String toString() {
            return "Meta-" + super.toString() + " for " + uiClassName; //NOI18N
        }
    }

    public abstract static class Lazy extends UIBootstrapValue implements UIDefaults.LazyValue {
        public Lazy (String uiClassName) {
            super (uiClassName, null);
        }

        @Override
        public Object[] getKeysAndValues() {
            if (defaults == null) {
                defaults = createKeysAndValues();
            }
            return defaults;
        }

        public abstract Object[] createKeysAndValues();

    }
}
