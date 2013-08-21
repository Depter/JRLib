/*
 * ComboBoxComboBoxArrowButtonEditableState.java %E%
 *
 * Copyright (c) 2007, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.jreserve.opaquenimbus;

import java.awt.*;
import javax.swing.*;

/**
 */
class ComboBoxComboBoxArrowButtonEditableState extends State {
    ComboBoxComboBoxArrowButtonEditableState() {
        super("Editable");
    }

    @Override protected boolean isInState(JComponent c) {

                                Component parent = c.getParent();
                                return parent instanceof JComboBox && ((JComboBox)parent).isEditable();
    }
}

