package org.officelaf.ribbon;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntrySecondary;

public class SecondaryMenuEntry extends RibbonApplicationMenuEntrySecondary {
    public SecondaryMenuEntry(Action action) {
        this(ActionUtil.lookupText(action), ActionUtil.lookupDescription(action), ActionUtil.lookupIcon(action, false),
                action);
    }

    public SecondaryMenuEntry(String text, String description, ResizableIcon icon, final Action action) {
        super(icon, text, action, JCommandButton.CommandButtonKind.ACTION_ONLY);
        if (action != null) {
            action.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
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
}
