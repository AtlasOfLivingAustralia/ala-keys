//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.18 at 10:25:48 AM EST 
//


package org.tdwg.rs.ubif._2006;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Actual modification of a statement. Refers to a modifier of any type (frequency, certainty, spatial, temporal, etc.).
 * 
 * <p>Java class for ModifierRefWithData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModifierRefWithData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}AbstractLocalRef">
 *       &lt;attribute name="lower" type="{http://rs.tdwg.org/UBIF/2006/}ZeroToOne" default="0" />
 *       &lt;attribute name="upper" type="{http://rs.tdwg.org/UBIF/2006/}ZeroToOne" default="1" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModifierRefWithData")
@XmlSeeAlso({
    ModifierMarkupRef.class
})
public class ModifierRefWithData
    extends AbstractLocalRef
{

    @XmlAttribute(name = "lower")
    protected Double lower;
    @XmlAttribute(name = "upper")
    protected Double upper;

    /**
     * Gets the value of the lower property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public double getLower() {
        if (lower == null) {
            return  0.0D;
        } else {
            return lower;
        }
    }

    /**
     * Sets the value of the lower property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLower(Double value) {
        this.lower = value;
    }

    /**
     * Gets the value of the upper property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public double getUpper() {
        if (upper == null) {
            return  1.0D;
        } else {
            return upper;
        }
    }

    /**
     * Sets the value of the upper property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setUpper(Double value) {
        this.upper = value;
    }

}