package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * User: gunnar
 * Date: Nov 28, 2008
 * Time: 3:36:51 PM
 */
public class PrimaryMenuEntry extends RibbonApplicationMenuEntryPrimary {

    private String componentName;

    public PrimaryMenuEntry(Action action) {
        this(JCommandButton.CommandButtonKind.ACTION_ONLY, ActionUtil.lookupText(action),
                ActionUtil.lookupIcon(action, false), action);
        addEnabledListener(action);
    }

    public PrimaryMenuEntry(ResizableIcon icon, String text, ActionListener mainActionListener, JCommandButton.CommandButtonKind entryKind, String componentName) {
        super(icon, text, mainActionListener, entryKind);
        this.componentName = componentName;
    }

    public PrimaryMenuEntry(JCommandButton.CommandButtonKind kind, String text, ResizableIcon icon, final Action action) {
        super(icon, text, action, kind);
        addEnabledListener(action);
    }

    public PrimaryMenuEntry(final Action action, ResizableIcon icon, String componentName) {
        this(icon,
                String.valueOf(action.getValue(Action.NAME)),
                action,
                JCommandButton.CommandButtonKind.ACTION_ONLY, componentName);
        action.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ("enabled".equals(evt.getPropertyName())) {
                    setEnabled(action.isEnabled());
                }
            }
        });
        setEnabled(action.isEnabled());
    }

    private void addEnabledListener(final Action action) {
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
    }

    public String getComponentName() {
        return componentName;
    }
}
