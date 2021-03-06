//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.18 at 10:25:48 AM EST 
//


package org.tdwg.rs.ubif._2006;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * For single values (singleton observation or values in a sample).
 * 
 * <p>Java class for ValueMarkup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ValueMarkup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="Note" type="{http://rs.tdwg.org/UBIF/2006/}MarkupText"/>
 *         &lt;element name="Text" type="{http://rs.tdwg.org/UBIF/2006/}MarkupText"/>
 *       &lt;/choice>
 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValueMarkup", propOrder = {
    "noteOrText"
})
public class ValueMarkup {

    @XmlElementRefs({
        @XmlElementRef(name = "Text", namespace = "http://rs.tdwg.org/UBIF/2006/", type = JAXBElement.class),
        @XmlElementRef(name = "Note", namespace = "http://rs.tdwg.org/UBIF/2006/", type = JAXBElement.class)
    })
    protected List<JAXBElement<MarkupText>> noteOrText;
    @XmlAttribute(name = "value", required = true)
    protected double value;

    /**
     * Gets the value of the noteOrText property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the noteOrText property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNoteOrText().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link MarkupText }{@code >}
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link MarkupText }{@code >}
     * 
     * 
     */
    public List<JAXBElement<MarkupText>> getNoteOrText() {
        if (noteOrText == null) {
            noteOrText = new ArrayList<JAXBElement<MarkupText>>();
        }
        return this.noteOrText;
    }

    /**
     * Gets the value of the value property.
     * 
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     */
    public void setValue(double value) {
        this.value = value;
    }

}
