package org.officelaf.ribbon;

import org.openide.awt.Actions;
import org.openide.util.ImageUtilities;

import javax.swing.*;
import java.awt.*;

public class ActionUtil {
    public static String lookupText(Action action) {
        String s;
        if (action.getValue("menuText") != null) {
            s = action.getValue("menuText").toString();
        } else {
            s = String.valueOf(action.getValue(Action.NAME));
        }

        return s != null ? Actions.cutAmpersand(s) : null;
    }

    public static String lookupDescription(Action action) {
        return action.getValue(Action.SHORT_DESCRIPTION) != null ?
                action.getValue(Action.SHORT_DESCRIPTION).toString() : null;
    }

    public static TwoSizedIcon lookupIcon(Action action) {
        return lookupIcon(action, false);
    }

    public static TwoSizedIcon lookupIcon(Action action, boolean disabled) {
        Icon smallIcon = null;
        Icon largeIcon = null;

        if (action.getValue("iconBase") != null) {
            String b = action.getValue("iconBase").toString();
            String bb = insertBeforeSuffix(b, "24");

            Image small;
            Image large;

            if (!disabled) {
                small = ImageUtilities.loadImage(b);
                large = ImageUtilities.loadImage(bb);
            } else {
                small = ImageUtilities.loadImage(insertBeforeSuffix(b, "_disabled"));
                if (small == null) {
                    small = ImageUtilities.loadImage(b);
                } else {
                    disabled = false; // do not create a disabled version - we already have one
                }
                large = ImageUtilities.loadImage(insertBeforeSuffix(bb, "_disabled"));
                if (large == null) {
                    large = ImageUtilities.loadImage(bb);
                }
            }

            if (small != null) {
                smallIcon = ImageUtilities.image2Icon(small);
            }
            if (large != null) {
                largeIcon = ImageUtilities.image2Icon(large);
            }
        }

        if (smallIcon == null) {
            smallIcon = action.getValue(Action.SMALL_ICON) != null ? (Icon) action.getValue(Action.SMALL_ICON) : null;
        }
        if (largeIcon == null) {
            largeIcon = action.getValue(Action.LARGE_ICON_KEY) != null ? (Icon) action.getValue(Action.LARGE_ICON_KEY) : null;
        }

        return new TwoSizedIcon(smallIcon, largeIcon, disabled);
    }

    private static String insertBeforeSuffix(String path, String toInsert) {
        String withoutSuffix = path;
        String suffix = ""; // NOI18N

        if (path.lastIndexOf('.') >= 0) {
            withoutSuffix = path.substring(0, path.lastIndexOf('.'));
            suffix = path.substring(path.lastIndexOf('.'), path.length());
        }

        return withoutSuffix + toInsert + suffix;
    }
}
