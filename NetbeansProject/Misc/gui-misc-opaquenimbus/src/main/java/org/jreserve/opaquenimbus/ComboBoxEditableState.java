/*
 * ComboBoxEditableState.java %E%
 *
 * Copyright (c) 2007, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.jreserve.opaquenimbus;

import javax.swing.*;

/**
 */
class ComboBoxEditableState extends State {
    ComboBoxEditableState() {
        super("Editable");
    }

    @Override protected boolean isInState(JComponent c) {

                          return ((JComboBox)c).isEditable();
    }
}

