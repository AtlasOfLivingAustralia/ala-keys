//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.13 at 10:31:49 AM EST 
//


package org.lucidcentral._2007.ds;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * human readable, language and audience specific label with role semantics
 * 
 * <p>Java class for LabelText complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LabelText">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.lucidcentral.org/2007/DS/>ShortStringText">
 *       &lt;attribute name="role" type="{http://www.lucidcentral.org/2007/DS/}LabelRoleEnum" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LabelText")
public class LabelText
    extends ShortStringText
{

    @XmlAttribute(name = "role")
    protected LabelRoleEnum role;

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link LabelRoleEnum }
     *     
     */
    public LabelRoleEnum getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link LabelRoleEnum }
     *     
     */
    public void setRole(LabelRoleEnum value) {
        this.role = value;
    }

}
