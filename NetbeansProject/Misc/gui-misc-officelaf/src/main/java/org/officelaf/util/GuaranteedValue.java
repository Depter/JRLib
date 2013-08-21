/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.officelaf.util;

import java.awt.Color;
import java.awt.Font;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GuaranteedValue implements UIDefaults.LazyValue {
    private Object value;
    /** Creates a new instance of GuaranteedValue */
    public GuaranteedValue(String key, Object fallback) {
        //Be fail fast, so no random exceptions from random components later
        if (key == null || fallback == null) {
            throw new NullPointerException ("Null parameters: " + key + ',' + fallback);
        }
        
        value = UIManager.get(key);
        if (value == null) {
            value = fallback;
        }
    }
    
    public GuaranteedValue(String[] keys, Object fallback) {
        //Be fail fast, so no random exceptions from random components later
        if (keys == null || fallback == null) {
            throw new NullPointerException ("Null parameters: " + keys + ',' + fallback);
        }
        for (int i=0; i < keys.length; i++) {
            value = UIManager.get(keys[i]);
            if (value != null) {
                break;
            }
        }
        if (value == null) {
            value = fallback;
        }
    }
    
    @Override
    public Object createValue(UIDefaults table) {
        return value;
    }
    
    /** Convenience getter of the value as a color - returns null if this
     * instance was used for some other type */
    public Color getColor() {
        Object o = createValue(null);
        if (o instanceof Color) {
            return (Color) o;
        } else {
            return null;
        }
    }

    public Font getFont() {
        Object o = createValue(null);
        if (o instanceof Font) {
            return (Font) o;
        } else {
            return null;
        }
    }
}
