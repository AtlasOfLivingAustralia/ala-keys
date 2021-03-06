//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.18 at 10:25:48 AM EST 
//


package org.tdwg.rs.ubif._2006;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * # Derived from AbstractCharacterDefinition to be used in instance documents (non-abstract type)
 * 
 * Quantitative data include data like the DELTA types IN/RN. They are not supported by NEXUS.
 * 
 * <p>Java class for QuantitativeCharacter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuantitativeCharacter">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}AbstractCharacterDefinition">
 *       &lt;sequence>
 *         &lt;element name="Assumptions" type="{http://rs.tdwg.org/UBIF/2006/}QuantitativeCharAssumptions" minOccurs="0"/>
 *         &lt;element name="Mappings" type="{http://rs.tdwg.org/UBIF/2006/}QuantitativeCharMappingSet" minOccurs="0"/>
 *         &lt;element name="MeasurementUnit" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Label" type="{http://rs.tdwg.org/UBIF/2006/}LabelText" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="precedesValue" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Default" type="{http://rs.tdwg.org/UBIF/2006/}QuantitativeCharDefaults" minOccurs="0"/>
 *         &lt;group ref="{http://rs.tdwg.org/UBIF/2006/}SpecificExtension" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuantitativeCharacter", propOrder = {
    "assumptions",
    "mappings",
    "measurementUnit",
    "_default",
    "nextVersion"
})
public class QuantitativeCharacter
    extends AbstractCharacterDefinition
{

    @XmlElement(name = "Assumptions")
    protected QuantitativeCharAssumptions assumptions;
    @XmlElement(name = "Mappings")
    protected QuantitativeCharMappingSet mappings;
    @XmlElement(name = "MeasurementUnit")
    protected MeasurementUnit measurementUnit;
    @XmlElement(name = "Default")
    protected QuantitativeCharDefaults _default;
    @XmlElement(name = "NextVersion")
    protected VersionExtension nextVersion;

    /**
     * Gets the value of the assumptions property.
     * 
     * @return
     *     possible object is
     *     {@link QuantitativeCharAssumptions }
     *     
     */
    public QuantitativeCharAssumptions getAssumptions() {
        return assumptions;
    }

    /**
     * Sets the value of the assumptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuantitativeCharAssumptions }
     *     
     */
    public void setAssumptions(QuantitativeCharAssumptions value) {
        this.assumptions = value;
    }

    /**
     * Gets the value of the mappings property.
     * 
     * @return
     *     possible object is
     *     {@link QuantitativeCharMappingSet }
     *     
     */
    public QuantitativeCharMappingSet getMappings() {
        return mappings;
    }

    /**
     * Sets the value of the mappings property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuantitativeCharMappingSet }
     *     
     */
    public void setMappings(QuantitativeCharMappingSet value) {
        this.mappings = value;
    }

    /**
     * Gets the value of the measurementUnit property.
     * 
     * @return
     *     possible object is
     *     {@link org.tdwg.rs.ubif._2006.QuantitativeCharacter.MeasurementUnit }
     *     
     */
    public MeasurementUnit getMeasurementUnit() {
        return measurementUnit;
    }

    /**
     * Sets the value of the measurementUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.tdwg.rs.ubif._2006.QuantitativeCharacter.MeasurementUnit }
     *     
     */
    public void setMeasurementUnit(MeasurementUnit value) {
        this.measurementUnit = value;
    }

    /**
     * Gets the value of the default property.
     * 
     * @return
     *     possible object is
     *     {@link QuantitativeCharDefaults }
     *     
     */
    public QuantitativeCharDefaults getDefault() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuantitativeCharDefaults }
     *     
     */
    public void setDefault(QuantitativeCharDefaults value) {
        this._default = value;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Label" type="{http://rs.tdwg.org/UBIF/2006/}LabelText" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *       &lt;attribute name="precedesValue" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "label"
    })
    public static class MeasurementUnit {

        @XmlElement(name = "Label", required = true)
        protected List<LabelText> label;
        @XmlAttribute(name = "precedesValue")
        protected Boolean precedesValue;

        /**
         * Gets the value of the label property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the label property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLabel().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link LabelText }
         * 
         * 
         */
        public List<LabelText> getLabel() {
            if (label == null) {
                label = new ArrayList<LabelText>();
            }
            return this.label;
        }

        /**
         * Gets the value of the precedesValue property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isPrecedesValue() {
            if (precedesValue == null) {
                return false;
            } else {
                return precedesValue;
            }
        }

        /**
         * Sets the value of the precedesValue property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setPrecedesValue(Boolean value) {
            this.precedesValue = value;
        }

    }

}
