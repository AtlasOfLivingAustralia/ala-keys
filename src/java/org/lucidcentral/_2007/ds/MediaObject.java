//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.13 at 10:31:49 AM EST 
//


package org.lucidcentral._2007.ds;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MediaObject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MediaObject">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.lucidcentral.org/2007/DS/}AbstractVersionedObject">
 *       &lt;sequence>
 *         &lt;element name="Type" type="{http://www.lucidcentral.org/2007/DS/}MediaTypeEnum"/>
 *         &lt;element name="Source" type="{http://www.lucidcentral.org/2007/DS/}MediaObjectSource"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MediaObject", propOrder = {
    "type",
    "source"
})
public class MediaObject
    extends AbstractVersionedObject
{

    @XmlElement(name = "Type", required = true)
    protected MediaTypeEnum type;
    @XmlElement(name = "Source", required = true)
    protected MediaObjectSource source;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link MediaTypeEnum }
     *     
     */
    public MediaTypeEnum getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link MediaTypeEnum }
     *     
     */
    public void setType(MediaTypeEnum value) {
        this.type = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link MediaObjectSource }
     *     
     */
    public MediaObjectSource getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link MediaObjectSource }
     *     
     */
    public void setSource(MediaObjectSource value) {
        this.source = value;
    }

}
