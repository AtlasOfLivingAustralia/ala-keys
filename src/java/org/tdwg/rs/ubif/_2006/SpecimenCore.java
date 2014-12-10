//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.18 at 10:25:48 AM EST 
//


package org.tdwg.rs.ubif._2006;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * Specimen plus core level extensions.
 * 
 * <p>Java class for SpecimenCore complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SpecimenCore">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}Specimen">
 *       &lt;group ref="{http://rs.tdwg.org/UBIF/2006/}SpecimenCoreExtensions"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpecimenCore", propOrder = {
    "taxonName",
    "identificationCertainty",
    "isPreservedInCollection"
})
public class SpecimenCore
    extends Specimen
{

    @XmlElement(name = "TaxonName")
    protected TaxonNameRef taxonName;
    @XmlElement(name = "IdentificationCertainty", defaultValue = "IdentificationCertaintyUnknown")
    protected QName identificationCertainty;
    @XmlElement(name = "IsPreservedInCollection", defaultValue = "true")
    protected Boolean isPreservedInCollection;

    /**
     * Gets the value of the taxonName property.
     * 
     * @return
     *     possible object is
     *     {@link TaxonNameRef }
     *     
     */
    public TaxonNameRef getTaxonName() {
        return taxonName;
    }

    /**
     * Sets the value of the taxonName property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaxonNameRef }
     *     
     */
    public void setTaxonName(TaxonNameRef value) {
        this.taxonName = value;
    }

    /**
     * Gets the value of the identificationCertainty property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.namespace.QName }
     *     
     */
    public QName getIdentificationCertainty() {
        return identificationCertainty;
    }

    /**
     * Sets the value of the identificationCertainty property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.namespace.QName }
     *     
     */
    public void setIdentificationCertainty(QName value) {
        this.identificationCertainty = value;
    }

    /**
     * Gets the value of the isPreservedInCollection property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPreservedInCollection() {
        return isPreservedInCollection;
    }

    /**
     * Sets the value of the isPreservedInCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPreservedInCollection(Boolean value) {
        this.isPreservedInCollection = value;
    }

}
