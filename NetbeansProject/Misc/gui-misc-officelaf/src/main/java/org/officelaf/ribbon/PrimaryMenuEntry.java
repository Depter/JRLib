package org.officelaf.ribbon;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;

public class PrimaryMenuEntry extends RibbonApplicationMenuEntryPrimary {
    public PrimaryMenuEntry(Action action) {
        this(JCommandButton.CommandButtonKind.ACTION_ONLY, ActionUtil.lookupText(action),
                ActionUtil.lookupIcon(action, false), action);
        addEnabledListener(action);
    }

    public PrimaryMenuEntry(JCommandButton.CommandButtonKind kind, String text, ResizableIcon icon, final Action action) {
        super(icon, text, action, kind);
        addEnabledListener(action);
    }

    private void addEnabledListener(final Action action) {
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
    }
}
