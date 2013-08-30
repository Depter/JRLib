/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.window;

import javax.swing.AbstractSpinnerModel;
import org.decsi.test.scenetest.widgets.TextBridge;

/**
 *
 * @author Peter Decsi
 */
class MonthDateSpinnerModel extends AbstractSpinnerModel implements TextBridge {

    private int[] value;

    MonthDateSpinnerModel() {
        this(1997, 1);
    }

    MonthDateSpinnerModel(int year, int month) {
        this.value = new int[]{year, month};
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (int[]) value;
        super.fireStateChanged();
    }

    @Override
    public Object getNextValue() {
        if (value[0] == 2004 && value[1] == 12) {
            return null;
        }
        if (value[1] == 12) {
            return new int[]{value[0] + 1, 1};
        }
        return new int[]{value[0], value[1] + 1};
    }

    @Override
    public Object getPreviousValue() {
        if (value[0] == 1997 && value[1] == 1) {
            return null;
        }
        if (value[1] == 1) {
            return new int[]{value[0] - 1, 12};
        }
        return new int[]{value[0], value[1] - 1};
    }

    @Override
    public String toString(Object value) {
        int[] arr = (int[]) value;
        if (arr[1] < 10) {
            return "" + arr[0] + "-0" + arr[1];
        }
        return "" + arr[0] + "-" + arr[1];
    }

    @Override
    public Object toValue(String str) throws IllegalArgumentException {
        try {
            if (str == null || str.length() == 0) {
                return new int[]{1997, 1};
            }

            return new int[]{
                Integer.parseInt(str.substring(0, 4)),
                Integer.parseInt(str.substring(5))
            };
        } catch (Exception ex) {
            throw new IllegalArgumentException("Value invalid!");
        }
    }
}
