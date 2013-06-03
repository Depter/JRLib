package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.openide.util.NbBundle;

import javax.swing.*;
import org.jreserve.gui.misc.officelaf.icons.IconService;

/**
 *
 * @author gunnar
 */
public enum PredefinedButton {
    CLIPBOARD_PASTE("paste.png", 32, true),
    CLIPBOARD_PASTE_SPECIAL("paste.png"),
    CLIPBOARD_CUT("cut.png"),
    CLIPBOARD_COPY("copy.png"),
    FONT_GROW("font_larger.png"),
    FONT_SHRINK("font_smaller.png"),
    FONT_BOLD("font_bold.png", true),
    FONT_ITALIC("font_italic.png", true),
    FONT_UNDERLINE("font_underline.png"),
    FONT_STRIKETHROUGH,
    FONT_BACKGROUND_COLOR,
    FONT_COLOR,
    ALIGNMENT_TOP,
    ALIGNMENT_MIDDLE,
    ALIGNMENT_BOTTOM,
    ALIGNMENT_LEFT,
    ALIGNMENT_CENTER,
    ALIGNMENT_RIGHT,
    ALIGNMENT_ORIENTATION,
    ALIGNMENT_ORIENTATION_ROTATE_CLOCKWISE,
    ALIGNMENT_ORIENTATION_ROTATE_COUNTERCLOCKWISE,
    ALIGNMENT_ORIENTATION_VERTICAL;

    private String iconName;
    private int size;
    private boolean toggle;

    private PredefinedButton() {
        this("empty.png", 16);
    }

    private PredefinedButton(String iconName) {
        this(iconName, 16);
    }

    private PredefinedButton(String iconName, boolean toggle) {
        this(iconName, 16, toggle);
    }

    private PredefinedButton(String iconName, int size) {
        this(iconName, size, false);
    }

    private PredefinedButton(String iconName, int size, boolean toggle) {
        this.iconName = iconName;
        this.size = size;
        this.toggle = toggle;
    }

    public String getText() {
        return NbBundle.getMessage(PredefinedButton.class, toString());
    }

    public ResizableIcon getResizableIcon() {
        return new ResizableImageIcon2(iconName, size);
    }

    public ResizableIcon getDisabledResizableIcon() {
        return new ResizableImageIcon2(iconName, size, true);
    }

    public Icon getIcon() {
        return IconService.getInstance().getIcon(iconName, size);
    }

    public boolean isToggle() {
        return toggle;
    }
}
