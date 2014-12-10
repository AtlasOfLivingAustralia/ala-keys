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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StateData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StateData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;sequence>
 *           &lt;element name="Modifier" type="{http://www.lucidcentral.org/2007/DS/}Modifier" maxOccurs="3" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element name="Text" type="{http://www.lucidcentral.org/2007/DS/}DescriptionText" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ref" use="required" type="{http://www.lucidcentral.org/2007/DS/}ShortString" />
 *       &lt;attribute name="debug_label" type="{http://www.lucidcentral.org/2007/DS/}ShortString" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StateData", propOrder = {
    "modifier",
    "text",
    "state"
})
public class StateData {

    @XmlElement(name = "Modifier")
    protected List<Modifier> modifier;
    @XmlElement(name = "Text")
    protected DescriptionText text;
    @XmlAttribute(name = "ref", required = true)
    protected String ref;
    @XmlAttribute(name = "debug_label")
    protected String debugLabel;
    @XmlElement(name = "State")
    protected List<StateRef> state;

    /**
     * Gets the value of the modifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the modifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getModifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Modifier }
     * 
     * 
     */
    public List<Modifier> getModifier() {
        if (modifier == null) {
            modifier = new ArrayList<Modifier>();
        }
        return this.modifier;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptionText }
     *     
     */
    public DescriptionText getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptionText }
     *     
     */
    public void setText(DescriptionText value) {
        this.text = value;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRef(String value) {
        this.ref = value;
    }

    /**
     * Gets the value of the debugLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebugLabel() {
        return debugLabel;
    }

    /**
     * Sets the value of the debugLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebugLabel(String value) {
        this.debugLabel = value;
    }

    public List<StateRef> getState() {
        if (state == null) {
            state = new ArrayList<StateRef>();
        }
        return this.state;
    }

    public void setState(List<StateRef> state) { this.state = state ;}


}