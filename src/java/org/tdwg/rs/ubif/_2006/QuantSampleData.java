//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.18 at 10:25:48 AM EST 
//


package org.tdwg.rs.ubif._2006;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * # Derived from abstract CharSampleData to be used for numerical data in instance documents (non-abstract type) in coded descriptions (Sample/ SamplingUnit). Attribute value (xs: double) is for directly measured/observed values. Not for statistical measures; these cannot occur in sampling units!
 * 
 * <p>Java class for QuantSampleData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuantSampleData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}AbstractCharSampleData">
 *       &lt;sequence>
 *         &lt;element name="Note" type="{http://rs.tdwg.org/UBIF/2006/}LongStringL" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;group ref="{http://rs.tdwg.org/UBIF/2006/}SpecificExtension" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="significant" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="unitprefix" type="{http://rs.tdwg.org/UBIF/2006/}MeasurementUnitPrefixEnum" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuantSampleData", propOrder = {
    "note",
    "nextVersion"
})
public class QuantSampleData
    extends AbstractCharSampleData
{

    @XmlElement(name = "Note")
    protected List<LongStringL> note;
    @XmlElement(name = "NextVersion")
    protected VersionExtension nextVersion;
    @XmlAttribute(name = "value", required = true)
    protected double value;
    @XmlAttribute(name = "significant")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger significant;
    @XmlAttribute(name = "unitprefix")
    protected QName unitprefix;

    /**
     * Gets the value of the note property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the note property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LongStringL }
     * 
     * 
     */
    public List<LongStringL> getNote() {
        if (note == null) {
            note = new ArrayList<LongStringL>();
        }
        return this.note;
    }

    /**
     * Gets the value of the nextVersion property.
     * 
     * @return
     *     possible object is
     *     {@link VersionExtension }
     *     
     */
    public VersionExtension getNextVersion() {
        return nextVersion;
    }

    /**
     * Sets the value of the nextVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link VersionExtension }
     *     
     */
    public void setNextVersion(VersionExtension value) {
        this.nextVersion = value;
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

    /**
     * Gets the value of the significant property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *     
     */
    public BigInteger getSignificant() {
        return significant;
    }

    /**
     * Sets the value of the significant property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setSignificant(BigInteger value) {
        this.significant = value;
    }

    /**
     * Gets the value of the unitprefix property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.namespace.QName }
     *     
     */
    public QName getUnitprefix() {
        return unitprefix;
    }

    /**
     * Sets the value of the unitprefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.namespace.QName }
     *     
     */
    public void setUnitprefix(QName value) {
        this.unitprefix = value;
    }

}
