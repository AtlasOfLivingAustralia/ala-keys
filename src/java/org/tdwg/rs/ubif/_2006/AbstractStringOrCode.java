//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.18 at 10:25:48 AM EST 
//


package org.tdwg.rs.ubif._2006;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * Three attributes provide options to express a value constrained (enumerated/extensible) vocabulary, simple free-form text (perhaps interpreted), or verbatim (uninterpreted original version). At least one attribute should be present; this can not be validated by the schema (external validation is required for this and for all types derived from this).
 * 
 * <p>Java class for AbstractStringOrCode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractStringOrCode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="literal" type="{http://rs.tdwg.org/UBIF/2006/}ShortString" />
 *       &lt;attribute name="verbatim" type="{http://rs.tdwg.org/UBIF/2006/}ShortString" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractStringOrCode")
@XmlSeeAlso({
    Sex.class,
    TaxonomicRank.class,
    DataStatus.class,
    RevisionStatus.class
})
public abstract class AbstractStringOrCode {

    @XmlAttribute(name = "code")
    protected QName code;
    @XmlAttribute(name = "literal")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String literal;
    @XmlAttribute(name = "verbatim")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String verbatim;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.namespace.QName }
     *     
     */
    public QName getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.namespace.QName }
     *     
     */
    public void setCode(QName value) {
        this.code = value;
    }

    /**
     * Gets the value of the literal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLiteral() {
        return literal;
    }

    /**
     * Sets the value of the literal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLiteral(String value) {
        this.literal = value;
    }

    /**
     * Gets the value of the verbatim property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerbatim() {
        return verbatim;
    }

    /**
     * Sets the value of the verbatim property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerbatim(String value) {
        this.verbatim = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
