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
 * Subsets are used to filter/reduce the list of available items shown in the trees.
 * 
 * <p>Java class for Subset complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Subset">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.lucidcentral.org/2007/DS/}AbstractVersionedObject">
 *       &lt;attribute name="type" use="required" type="{http://www.lucidcentral.org/2007/DS/}SubsetTypeEnum" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Subset")
public class Subset
    extends AbstractVersionedObject
{

    @XmlAttribute(name = "type", required = true)
    protected SubsetTypeEnum type;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link SubsetTypeEnum }
     *     
     */
    public SubsetTypeEnum getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubsetTypeEnum }
     *     
     */
    public void setType(SubsetTypeEnum value) {
        this.type = value;
    }

}
