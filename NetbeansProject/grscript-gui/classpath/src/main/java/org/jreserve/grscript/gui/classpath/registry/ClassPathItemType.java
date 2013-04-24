package org.jreserve.grscript.gui.classpath.registry;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@XmlType
@XmlEnum
public enum ClassPathItemType {
    @XmlEnumValue("class") CLASS,
    @XmlEnumValue("jar") JAR;
}
