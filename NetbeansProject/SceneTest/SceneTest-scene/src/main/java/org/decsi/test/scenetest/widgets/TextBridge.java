/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

/**
 *
 * @author Peter Decsi
 */
public interface TextBridge<T> {
    public String toString(T value);
        
    public T toValue(String str) throws IllegalArgumentException;
    
    public static TextBridge<String> STRING = new TextBridge<String>() {

        @Override
        public String toString(String value) {
            return value;
        }

        @Override
        public String toValue(String str) throws IllegalArgumentException {
            return str;
        }
    };
}
