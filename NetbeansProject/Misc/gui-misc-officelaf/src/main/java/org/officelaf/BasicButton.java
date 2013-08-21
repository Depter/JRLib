package org.officelaf;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * A basic JButton with the BasicButtonUI installed as default.
 *
 *  @author Gunnar A. Reinseth
 */
public class BasicButton extends JButton {
    private Border outer;
    private Border inner;

    /**
     * Creates a button with no text or icon.
     */
    public BasicButton() {
        init();
    }

    /**
     * Creates a button with an icon.
     * @param icon the icon for the button
     */
    public BasicButton(Icon icon) {
        super(icon);
        init();
    }

    /**
     * Creates a button with the given text.
     * @param text the text of the button
     */
    public BasicButton(String text) {
        super(text);
        init();
    }

    /**
     * Creates a button where properties are taken from the
     * <code>Action</code> supplied.
     *
     * @param a the <code>Action</code> used to specify the new button
     */
    public BasicButton(Action a) {
        super(a);
        init();
    }

    /**
     * Creates a button with the given text and icon.
     * @param text the text of the button
     * @param icon the icon for the button
     */
    public BasicButton(String text, Icon icon) {
        super(text, icon);
        init(); 
    }

    private void init() {
        setBorder(null); 
    }

    public void updateUI() {
        LookAndFeel.installProperty(this, "rolloverEnabled", Boolean.TRUE);
        setUI(new BasicButtonUI());
    }

    public Border getInnerBorder() {
        return inner;
    }

    public void setInnerBorder(Border border) {
        inner = border;
        setBorder(outer);
    }

    public Border getOuterBorder() {
        return outer;
    }

    public void setBorder(Border border) {
        outer = border;
        super.setBorder(new CompoundBorder(outer, inner));
    }
}