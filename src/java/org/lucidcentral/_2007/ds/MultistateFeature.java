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
 * <p>Java class for MultistateFeature complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MultistateFeature">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.lucidcentral.org/2007/DS/}Feature">
 *       &lt;sequence>
 *         &lt;element name="States" type="{http://www.lucidcentral.org/2007/DS/}StateSet"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultistateFeature", propOrder = {
    "states"
})
public class MultistateFeature
    extends Feature
{

    @XmlElement(name = "States", required = true)
    protected StateSet states;

    /**
     * Gets the value of the states property.
     * 
     * @return
     *     possible object is
     *     {@link StateSet }
     *     
     */
    public StateSet getStates() {
        return states;
    }

    /**
     * Sets the value of the states property.
     * 
     * @param value
     *     allowed object is
     *     {@link StateSet }
     *     
     */
    public void setStates(StateSet value) {
        this.states = value;
    }

}
