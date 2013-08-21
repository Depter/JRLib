package org.officelaf.util;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.text.AttributedString;
import java.util.Map;
import javax.swing.JComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SwingUtilities2 {

    private final static Object charsBufferLock = new Object();
    private final static int CHAR_BUFFER_SIZE = 100;
    private static char[] charsBuffer = new char[CHAR_BUFFER_SIZE];
    private final static FontRenderContext DEFAULT_FRC = new FontRenderContext(null, false, false);
    private final static Object AA_TEXT_PROPERTY_KEY = new StringBuffer("AATextInfoPropertyKey");

    private static final int MIN_LAYOUT_CHARCODE = 0x0300;
    private static final int MAX_LAYOUT_CHARCODE = 0x206F;
    private static final int HI_SURROGATE_START = 0xD800;
    private static final int LO_SURROGATE_END = 0xDFFF; 
    private static final String DESKTOPFONTHINTS = "awt.font.desktophints";
   
    public static FontMetrics getFontMetrics(JComponent c, Graphics g) {
        Font font = g.getFont();
        if (c != null) {
            return c.getFontMetrics(g.getFont());
        }
        return g.getFontMetrics(font);
    }

    public static String clipStringIfNecessary(JComponent jc, FontMetrics fm, String str, int maxWidth) {
        if (str == null || str.length() == 0) {
            return "";
        }
        int textWidth = SwingUtilities2.stringWidth(jc, fm, str);
        if (textWidth > maxWidth) {
            return clipString(jc, fm, str, maxWidth);
        }
        return str;
    }

    public static String clipString(JComponent jc, FontMetrics fm, String str, int toWidth) {
        if (str == null) {
            str = "";
        }
        String clipString = "...";
        int stringLength = str.length();
        toWidth -= SwingUtilities2.stringWidth(jc, fm, clipString);
        if (toWidth <= 0) {
            return clipString;
        }

        boolean needsTextLayout;

        synchronized (charsBufferLock) {
            if (charsBuffer == null || charsBuffer.length < stringLength) {
                charsBuffer = str.toCharArray();
            } else {
                str.getChars(0, stringLength, charsBuffer, 0);
            }

            needsTextLayout = isComplexLayout(charsBuffer, 0, stringLength);
            if (!needsTextLayout) {
                int width = 0;
                for (int nChars = 0; nChars < stringLength; nChars++) {
                    width += fm.charWidth(charsBuffer[nChars]);
                    if (width > toWidth) {
                        str = str.substring(0, nChars);
                        break;
                    }
                }
            }
        }

        if (needsTextLayout) {
            FontRenderContext frc = getFontRenderContext(jc, fm);
            AttributedString aString = new AttributedString(str);
            LineBreakMeasurer measurer = new LineBreakMeasurer(aString.getIterator(), frc);
            int nChars = measurer.nextOffset(toWidth);
            str = str.substring(0, nChars);
        }
        return str + clipString;
    }

    private static boolean isComplexLayout(char[] text, int start, int limit) {
        for (int i = start; i < limit; i++) {
            if (text[i] < MIN_LAYOUT_CHARCODE) {
                continue;
            } else if (isNonSimpleChar(text[i])) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNonSimpleChar(char ch) {
        return isComplexCharCode(ch)
                || (ch >= HI_SURROGATE_START
                && ch <= LO_SURROGATE_END);
    }

    private static boolean isComplexCharCode(int code) {

        if (code < MIN_LAYOUT_CHARCODE || code > MAX_LAYOUT_CHARCODE) {
            return false;
        } else if (code <= 0x036f) {
            // Trigger layout for combining diacriticals 0x0300->0x036f
            return true;
        } else if (code < 0x0590) {
            // No automatic layout for Greek, Cyrillic, Armenian.
            return false;
        } else if (code <= 0x06ff) {
            // Hebrew 0590 - 05ff
            // Arabic 0600 - 06ff
            return true;
        } else if (code < 0x0900) {
            return false; // Syriac and Thaana
        } else if (code <= 0x0e7f) {
            // if Indic, assume shaping for conjuncts, reordering:
            // 0900 - 097F Devanagari
            // 0980 - 09FF Bengali
            // 0A00 - 0A7F Gurmukhi
            // 0A80 - 0AFF Gujarati
            // 0B00 - 0B7F Oriya
            // 0B80 - 0BFF Tamil
            // 0C00 - 0C7F Telugu
            // 0C80 - 0CFF Kannada
            // 0D00 - 0D7F Malayalam
            // 0D80 - 0DFF Sinhala
            // 0E00 - 0E7F if Thai, assume shaping for vowel, tone marks
            return true;
        } else if (code < 0x1780) {
            return false;
        } else if (code <= 0x17ff) { // 1780 - 17FF Khmer
            return true;
        } else if (code < 0x200c) {
            return false;
        } else if (code <= 0x200d) { //  zwj or zwnj
            return true;
        } else if (code >= 0x202a && code <= 0x202e) { // directional control
            return true;
        } else if (code >= 0x206a && code <= 0x206f) { // directional control
            return true;
        }
        return false;
    }

    private static FontRenderContext getFontRenderContext(Component c, FontMetrics fm) {
        assert fm != null || c != null;
        return (fm != null) ? fm.getFontRenderContext()
                : getFontRenderContext(c);
    }

    private static FontRenderContext getFontRenderContext(Component c) {
        if (c == null) {
            return DEFAULT_FRC;
        } else {
            return c.getFontMetrics(c.getFont()).getFontRenderContext();
        }
    }

    public static int stringWidth(JComponent c, FontMetrics fm, String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return fm.stringWidth(str);
    }

    public static void drawString(JComponent c, Graphics g, String text, int x, int y) {
        // c may be null

        // All non-editable widgets that draw strings call into this
        // methods.  By non-editable that means widgets like JLabel, JButton
        // but NOT JTextComponents.
        if (text == null || text.length() <= 0) { //no need to paint empty strings
            return;
        }
        
        // If we get here we're not printing
        AATextInfo info = drawTextAntialiased(c);
        if (info != null && (g instanceof Graphics2D)) {
            Graphics2D g2 = (Graphics2D) g;

            Object oldContrast = null;
            Object oldAAValue = g2.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
            if (info.aaHint != oldAAValue) {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, info.aaHint);
            } else {
                oldAAValue = null;
            }
            if (info.lcdContrastHint != null) {
                oldContrast = g2.getRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST);
                if (info.lcdContrastHint.equals(oldContrast)) {
                    oldContrast = null;
                } else {
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST,
                            info.lcdContrastHint);
                }
            }

            g.drawString(text, x, y);

            if (oldAAValue != null) {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, oldAAValue);
            }
            if (oldContrast != null) {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, oldContrast);
            }
        } else {
            g.drawString(text, x, y);
        }
    }

    private static AATextInfo drawTextAntialiased(JComponent c) {
        if (c != null) {
            return (AATextInfo) c.getClientProperty(AA_TEXT_PROPERTY_KEY);
        }
        return null;
    }

    private static class AATextInfo {

        private static AATextInfo getAATextInfoFromMap(Map hints) {
            Object aaHint = hints.get(RenderingHints.KEY_TEXT_ANTIALIASING);
            Object contHint = hints.get(RenderingHints.KEY_TEXT_LCD_CONTRAST);

            if (aaHint == null
                    || aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
                    || aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
                return null;
            } else {
                return new AATextInfo(aaHint, (Integer) contHint);
            }
        }

        public static AATextInfo getAATextInfo(boolean lafCondition) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            Object map = tk.getDesktopProperty(DESKTOPFONTHINTS);
            if (map instanceof Map) {
                return getAATextInfoFromMap((Map) map);
            } else {
                return null;
            }
        }
        
        Object aaHint;
        Integer lcdContrastHint;
        FontRenderContext frc;

        /* These are rarely constructed objects, and only when a complete
         * UI is being updated, so the cost of the tests here is minimal
         * and saves tests elsewhere.
         * We test that the values are ones we support/expect.
         */
        public AATextInfo(Object aaHint, Integer lcdContrastHint) {
            if (aaHint == null) {
                throw new InternalError("null not allowed here");
            }
            if (aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
                    || aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
                throw new InternalError("AA must be on");
            }
            this.aaHint = aaHint;
            this.lcdContrastHint = lcdContrastHint;
            this.frc = new FontRenderContext(null, aaHint,
                    RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
        }
    }
}
