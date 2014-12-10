//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.13 at 10:31:49 AM EST 
//


package org.lucidcentral._2007.ds;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DescriptionData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DescriptionData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="MultistateData" type="{http://www.lucidcentral.org/2007/DS/}MultistateData"/>
 *         &lt;element name="NumericData" type="{http://www.lucidcentral.org/2007/DS/}NumericData"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DescriptionData", propOrder = {
    "multistateDataOrNumericData"
})
public class DescriptionData {

    @XmlElements({
        @XmlElement(name = "MultistateData", type = MultistateData.class),
        @XmlElement(name = "NumericData", type = NumericData.class)
    })
    protected List<Object> multistateDataOrNumericData;

    /**
     * Gets the value of the multistateDataOrNumericData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the multistateDataOrNumericData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMultistateDataOrNumericData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MultistateData }
     * {@link NumericData }
     * 
     * 
     */
    public List<Object> getMultistateDataOrNumericData() {
        if (multistateDataOrNumericData == null) {
            multistateDataOrNumericData = new ArrayList<Object>();
        }
        return this.multistateDataOrNumericData;
    }

}
