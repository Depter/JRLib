package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BoundCommandButton extends JCommandButton {
    protected final Action mainAction;
    protected final Action[] extraActions;

    public BoundCommandButton(Action mainAction, Action... extraActions) {
        this(extraActions.length == 0 ? CommandButtonKind.ACTION_ONLY : CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION,
                ActionUtil.lookupText(mainAction),
                ActionUtil.lookupDescription(mainAction),
                ActionUtil.lookupIcon(mainAction, false),
                ActionUtil.lookupIcon(mainAction, true), mainAction, extraActions);
    }

    public BoundCommandButton(CommandButtonKind kind, String text, String description, ResizableIcon icon,
                              ResizableIcon disabledIcon, Action mainAction, Action... extraActions) {
        super(text, icon);
        this.mainAction = mainAction;
        this.extraActions = extraActions;
        setCommandButtonKind(kind);
        setDisabledIcon(disabledIcon);
        addActionListener(mainAction);

        RichTooltip tooltip = new RichTooltip();
        tooltip.setTitle(getText());
        tooltip.addDescriptionSection(description == null || description.length() == 0 ? " " : description);
        setActionRichTooltip(tooltip);
        setPopupRichTooltip(tooltip);

        PropertyChangeListener l = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("enabled".equals(evt.getPropertyName())) {
                    updateState();
                }
            }
        };

        mainAction.addPropertyChangeListener(l);
        if (extraActions.length > 0) {
            final JCommandPopupMenu menu = new JCommandPopupMenu();

            for (Action extraAction : extraActions) {
                menu.addMenuButton(new BoundMenuCommandButton(extraAction));
                if (extraAction != mainAction) {
                    extraAction.addPropertyChangeListener(l);
                }
            }
            setPopupCallback(new PopupPanelCallback() {
                public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                    return menu;
                }
            });
        }

        updateState();
    }

    protected void updateState() {
        switch (getCommandButtonKind()) {
            case ACTION_ONLY:
            case POPUP_ONLY:
                setEnabled(mainAction.isEnabled());
                break;
            case ACTION_AND_POPUP_MAIN_ACTION:
            case ACTION_AND_POPUP_MAIN_POPUP:
                if (extraActions.length == 0) {
                    setEnabled(mainAction.isEnabled());
                } else {
                    getActionModel().setEnabled(mainAction.isEnabled());
                    boolean b = false;
                    for (Action extraAction : extraActions) {
                        if (b = extraAction.isEnabled()) {
                            break;
                        }
                    }
                    getPopupModel().setEnabled(b);
                }
        }
    }
}

