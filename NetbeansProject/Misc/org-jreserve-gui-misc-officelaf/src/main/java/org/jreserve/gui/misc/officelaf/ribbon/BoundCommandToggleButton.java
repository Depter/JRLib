package org.jreserve.gui.misc.officelaf.ribbon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import org.pushingpixels.flamingo.api.common.JCommandToggleButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;

/**
 * Meme principe que BoundCommandButton
 * @author hironico
 */
public class BoundCommandToggleButton extends JCommandToggleButton implements ActionListener {
    private Action mainAction = null;
    private boolean selected = false;

    public BoundCommandToggleButton(Action action) {
        super(ActionUtil.lookupText(action));
        super.setIcon(ActionUtil.lookupIcon(action, false));
        super.setDisabledIcon(ActionUtil.lookupIcon(action, true));
        super.addActionListener(action);
        super.addActionListener(BoundCommandToggleButton.this);
        
        String description = ActionUtil.lookupDescription(action);
        RichTooltip tooltip = new RichTooltip();
        tooltip.setTitle(getText());
        tooltip.addDescriptionSection(description == null || description.length() == 0 ? " " : description);
        setActionRichTooltip(tooltip);

        this.mainAction = action;

        PropertyChangeListener l = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("enabled".equals(evt.getPropertyName())) {
                    updateState();
                }
            }
        };
    }

    protected void updateState() {
        super.setEnabled(mainAction.isEnabled());
    }

    /**
     * Permet de mettre jour la property selected.
     * @param e ActionEvent qui a d�clench cette mthode.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        selected = !selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
