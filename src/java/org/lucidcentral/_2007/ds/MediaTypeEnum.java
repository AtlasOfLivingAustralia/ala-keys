//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.13 at 10:31:49 AM EST 
//


package org.lucidcentral._2007.ds;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MediaTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MediaTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NCName">
 *     &lt;enumeration value="image"/>
 *     &lt;enumeration value="video"/>
 *     &lt;enumeration value="sound"/>
 *     &lt;enumeration value="text"/>
 *     &lt;enumeration value="other"/>
 *     &lt;enumeration value="unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MediaTypeEnum")
@XmlEnum
public enum MediaTypeEnum {

    @XmlEnumValue("image")
    IMAGE("image"),
    @XmlEnumValue("video")
    VIDEO("video"),
    @XmlEnumValue("sound")
    SOUND("sound"),
    @XmlEnumValue("text")
    TEXT("text"),
    @XmlEnumValue("other")
    OTHER("other"),
    @XmlEnumValue("unknown")
    UNKNOWN("unknown");
    private final String value;

    MediaTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MediaTypeEnum fromValue(String v) {
        for (MediaTypeEnum c: MediaTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}