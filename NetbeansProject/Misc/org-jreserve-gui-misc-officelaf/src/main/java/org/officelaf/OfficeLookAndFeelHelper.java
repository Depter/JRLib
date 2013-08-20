package org.officelaf;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;


public class OfficeLookAndFeelHelper {
    public static void installLFCustoms(LookAndFeel laf, UIDefaults table) {
        table.put("Nb." + laf.getName() + "LFCustoms", new OfficeLFCustoms());
    }

    public UIDefaults createDefaults() {
        return new UIDefaults(610, 0.75f);
    }

    public Object[] getClassDefaults() {
        final String uiClassnamePrefix = "org.officelaf.Office";

        return new Object[] {
                        "PanelUI", uiClassnamePrefix + "PanelUI",
                       "HeaderUI", uiClassnamePrefix + "HeaderUI",
                     "RootPaneUI", uiClassnamePrefix + "RootPaneUI",
                "CommandButtonUI", uiClassnamePrefix + "CommandButtonUI",
          "CommandToggleButtonUI", uiClassnamePrefix + "CommandToggleButtonUI",
            "CommandMenuButtonUI", uiClassnamePrefix + "CommandMenuButtonUI",
           "CommandButtonPanelUI", uiClassnamePrefix + "CommandButtonPanelUI",
                       "RibbonUI", uiClassnamePrefix + "RibbonUI",
                   "RibbonBandUI", uiClassnamePrefix + "RibbonBandUI",
             "BandControlPanelUI", uiClassnamePrefix + "BandControlPanelUI",
       "RibbonTaskToggleButtonUI", uiClassnamePrefix + "RibbonTaskToggleButtonUI",
  "RibbonApplicationMenuButtonUI", uiClassnamePrefix + "RibbonApplicationMenuButtonUI"
        };
    }

    public String[] getSystemColorDefaults() {
        return new String[] {
                "desktop", "#005C5C", /* Color of the desktop background */
          "activeCaption", "#000080", /* Color for captions (title bars) when they are active. */
      "activeCaptionText", "#FFFFFF", /* Text color for text in captions (title bars). */
    "activeCaptionBorder", "#C0C0C0", /* Border color for caption (title bar) window borders. */
        "inactiveCaption", "#808080", /* Color for captions (title bars) when not active. */
    "inactiveCaptionText", "#C0C0C0", /* Text color for text in inactive captions (title bars). */
  "inactiveCaptionBorder", "#C0C0C0", /* Border color for inactive caption (title bar) window borders. */
                 "window", "#FFFFFF", /* Default color for the interior of windows */
           "windowBorder", "#000000", /* ??? */
             "windowText", "#000000", /* ??? */
                   "menu", "#EBEBEB", /* Background color for menus */
       "menuPressedItemB", "#ffe7a2", /* LightShadow of menubutton highlight */
       "menuPressedItemF", "#000000", /* Default color for foreground "text" in menu item */
               "menuText", "#000000", /* Text color for menus  */
                   "text", "#EBEBEB", /* Text background color */
               "textText", "#000000", /* Text foreground color */
          "textHighlight", "#ffe7a2", /* Text background color when selected */
      "textHighlightText", "#000000", /* Text color when selected */
       "textInactiveText", "#808080", /* Text color when disabled */
                "control", "#EBEBEB", /* Default color for controls (buttons, sliders, etc) */
            "controlText", "#000000", /* Default color for text in controls */
       "controlHighlight", "#EBEBEB",

     "controlLtHighlight", "#FFFFFF", /* Highlight color for controls */
          "controlShadow", "#808080", /* Shadow color for controls */
        "controlDkShadow", "#000000", /* Dark shadow color for controls */
              "scrollbar", "#E0E0E0", /* Scrollbar background (usually the "track") */
                   "info", "#EBEBEB", /* ??? */
               "infoText", "#000000", /* ??? */
        };
    }

    public Object[] getComponentDefaults() {
        return new Object[] {
                            "InternalFrame.closeIcon", createIcon("close.png"),
                        "InternalFrame.closeDownIcon", createIcon("close_down.png"),
                        "InternalFrame.closeOverIcon", createIcon("close_over.png"),
                         "InternalFrame.maximizeIcon", createIcon("maximize.png"),
                     "InternalFrame.maximizeDownIcon", createIcon("maximize_down.png"),
                     "InternalFrame.maximizeOverIcon", createIcon("maximize_over.png"),
                         "InternalFrame.minimizeIcon", createIcon("restore.png"),
                     "InternalFrame.minimizeDownIcon", createIcon("restore_down.png"),
                     "InternalFrame.minimizeOverIcon", createIcon("restore_over.png"),
                          "InternalFrame.iconifyIcon", createIcon("minimize.png"),
                      "InternalFrame.iconifyDownIcon", createIcon("minimize_down.png"),
                      "InternalFrame.iconifyOverIcon", createIcon("minimize_over.png"),

                                    "Table.gridColor", new ColorUIResource(new Color(208, 215, 229)),

                                      "Ribbon.border", new BorderUIResource.EmptyBorderUIResource(3, 1, 2, 1),
                                  "RibbonBand.border", new BorderUIResource.EmptyBorderUIResource(2, 4, 2, 5),
                             "ToggleTabButton.border", new BorderUIResource.EmptyBorderUIResource(0, 14, 0, 14),
                                "ControlPanel.border", new BorderUIResource.EmptyBorderUIResource(0, 0, 0, 0),
        };
    }

    /**
     * Creates an ImageIcon based on the given file name -
     * assumes the file is in the images directory
     *
     * @param image name of the image file
     * @return an ImageIcon created from file
     */
    private ImageIcon createIcon(String image) {
        return new ImageIcon(OfficeLookAndFeelHelper.class.getResource("images/" + image));
    }

    public static Font getSystemFont(int style, float size) {
        return new Font(Font.DIALOG, style, (int) size);
    }
}
