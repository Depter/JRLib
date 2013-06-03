package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntrySecondary;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
* User: gunnar
* Date: Nov 28, 2008
* Time: 3:37:37 PM
*/
public class SecondaryMenuEntry extends RibbonApplicationMenuEntrySecondary {
    private String componentName;

    public SecondaryMenuEntry(final Action action, ResizableIcon icon, String description, String componentName) {
        super(icon,
                String.valueOf(action.getValue(Action.NAME)),
                action,
                JCommandButton.CommandButtonKind.ACTION_ONLY);
        this.componentName = componentName;
        action.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("enabled".equals(evt.getPropertyName())) {
                    setEnabled(action.isEnabled());
                }
            }
        });
        setEnabled(action.isEnabled());
        setDescriptionText(description);
    }

    public SecondaryMenuEntry(String text, String description, ResizableIcon icon, final Action action) {
        super(icon, text, action, JCommandButton.CommandButtonKind.ACTION_ONLY);
        if (action != null) {
            action.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("enabled".equals(evt.getPropertyName())) {
                        setEnabled(action.isEnabled());
                    }
                }
            });
            setEnabled(action.isEnabled());
        }
        setDescriptionText(description);
    }


    public String getComponentName() {
        return componentName;
    }
}
