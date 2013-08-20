package org.officelaf.ribbon;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

public class BoundMenuCommandButton extends JCommandMenuButton {
    protected final Action action;

    public BoundMenuCommandButton(Action action) {
        this(CommandButtonKind.ACTION_ONLY, ActionUtil.lookupText(action),
                ActionUtil.lookupDescription(action), ActionUtil.lookupIcon(action, false),
                ActionUtil.lookupIcon(action, true), action);
    }

    public BoundMenuCommandButton(CommandButtonKind kind, String text, String description, ResizableIcon icon,
                                  ResizableIcon disabledIcon, Action action) {
        super(text, icon);
        this.action = action;
        setCommandButtonKind(kind);
        setDisabledIcon(disabledIcon);
        addActionListener(action);

        RichTooltip tooltip = new RichTooltip();
        tooltip.setTitle(getText());
        tooltip.addDescriptionSection(description == null || description.length() == 0 ? " " : description);
        setActionRichTooltip(tooltip);
        setPopupRichTooltip(tooltip);

        PropertyChangeListener l = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("enabled".equals(evt.getPropertyName())) {
                    updateState();
                }
                if (Action.SHORT_DESCRIPTION.equals(evt.getPropertyName())) {
                    updateTooltip();
                }
            }
        };

        action.addPropertyChangeListener(l);

        updateState();
        updateTooltip();
    }

    protected void updateState() {
        setEnabled(action.isEnabled());
    }

    protected void updateTooltip() {
    }
}

