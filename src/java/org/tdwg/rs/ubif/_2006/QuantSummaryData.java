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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * # Derived from AbstractCharSummaryData to be used for numerical (statistical measures) data in instance documents (non-abstract type) 
 * 
 * <p>Java class for QuantSummaryData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuantSummaryData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}AbstractCharSummaryData">
 *       &lt;sequence>
 *         &lt;group ref="{http://rs.tdwg.org/UBIF/2006/}ModifierRefWithDataGroup"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="Measure" type="{http://rs.tdwg.org/UBIF/2006/}UnivarSimpleStatMeasureData"/>
 *           &lt;element name="PMeasure" type="{http://rs.tdwg.org/UBIF/2006/}UnivarParamStatMeasureData"/>
 *         &lt;/choice>
 *         &lt;element name="MeasurementUnitPrefix" type="{http://rs.tdwg.org/UBIF/2006/}MeasurementUnitPrefixEnum" minOccurs="0"/>
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
@XmlType(name = "QuantSummaryData", propOrder = {
    "modifier",
    "measureOrPMeasure",
    "measurementUnitPrefix",
    "nextVersion"
})
public class QuantSummaryData
    extends AbstractCharSummaryData
{

    @XmlElement(name = "Modifier")
    protected List<ModifierRefWithData> modifier;
    @XmlElements({
        @XmlElement(name = "Measure", type = UnivarSimpleStatMeasureData.class),
        @XmlElement(name = "PMeasure", type = UnivarParamStatMeasureData.class)
    })
    protected List<UnivarAnyStatMeasure> measureOrPMeasure;
    @XmlElement(name = "MeasurementUnitPrefix")
    protected QName measurementUnitPrefix;
    @XmlElement(name = "NextVersion")
    protected VersionExtension nextVersion;

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
     * {@link ModifierRefWithData }
     * 
     * 
     */
    public List<ModifierRefWithData> getModifier() {
        if (modifier == null) {
            modifier = new ArrayList<ModifierRefWithData>();
        }
        return this.modifier;
    }

    /**
     * Gets the value of the measureOrPMeasure property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the measureOrPMeasure property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMeasureOrPMeasure().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UnivarSimpleStatMeasureData }
     * {@link UnivarParamStatMeasureData }
     * 
     * 
     */
    public List<UnivarAnyStatMeasure> getMeasureOrPMeasure() {
        if (measureOrPMeasure == null) {
            measureOrPMeasure = new ArrayList<UnivarAnyStatMeasure>();
        }
        return this.measureOrPMeasure;
    }

    /**
     * Gets the value of the measurementUnitPrefix property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.namespace.QName }
     *     
     */
    public QName getMeasurementUnitPrefix() {
        return measurementUnitPrefix;
    }

    /**
     * Sets the value of the measurementUnitPrefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.namespace.QName }
     *     
     */
    public void setMeasurementUnitPrefix(QName value) {
        this.measurementUnitPrefix = value;
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

}
