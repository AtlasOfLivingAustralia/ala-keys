//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.18 at 10:25:48 AM EST 
//


package org.tdwg.rs.ubif._2006;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;


/**
 * A node in a class hierarchy tree (biology: taxonomical hierarchy)
 * 
 * NOTE: part of the constraints on the tree are not yet validated:
 * Inner nodes may be anonymous, but terminal nodes must point to a taxon name.
 * 
 * <p>Java class for TaxonHierarchyNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TaxonHierarchyNode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Parent" type="{http://rs.tdwg.org/UBIF/2006/}TaxonHierarchyNodeRef" minOccurs="0"/>
 *         &lt;element name="TaxonName" type="{http://rs.tdwg.org/UBIF/2006/}TaxonNameRef" minOccurs="0"/>
 *         &lt;element name="Synonyms" type="{http://rs.tdwg.org/UBIF/2006/}TaxonNameRefSeq" minOccurs="0"/>
 *         &lt;group ref="{http://rs.tdwg.org/UBIF/2006/}Extensions" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://rs.tdwg.org/UBIF/2006/}LocalInstanceID"/>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxonHierarchyNode", propOrder = {
    "parent",
    "taxonName",
    "synonyms",
    "nextVersionBase",
    "any"
})
public class TaxonHierarchyNode {

    @XmlElement(name = "Parent")
    protected TaxonHierarchyNodeRef parent;
    @XmlElement(name = "TaxonName")
    protected TaxonNameRef taxonName;
    @XmlElement(name = "Synonyms")
    protected TaxonNameRefSeq synonyms;
    @XmlElement(name = "NextVersionBase")
    protected VersionExtension nextVersionBase;
    @XmlAnyElement(lax = true)
    protected List<Object> any;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String id;
    @XmlAttribute(name = "debuglabel")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String debuglabel;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the parent property.
     * 
     * @return
     *     possible object is
     *     {@link TaxonHierarchyNodeRef }
     *     
     */
    public TaxonHierarchyNodeRef getParent() {
        return parent;
    }

    /**
     * Sets the value of the parent property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaxonHierarchyNodeRef }
     *     
     */
    public void setParent(TaxonHierarchyNodeRef value) {
        this.parent = value;
    }

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
     * Gets the value of the synonyms property.
     * 
     * @return
     *     possible object is
     *     {@link TaxonNameRefSeq }
     *     
     */
    public TaxonNameRefSeq getSynonyms() {
        return synonyms;
    }

    /**
     * Sets the value of the synonyms property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaxonNameRefSeq }
     *     
     */
    public void setSynonyms(TaxonNameRefSeq value) {
        this.synonyms = value;
    }

    /**
     * Gets the value of the nextVersionBase property.
     * 
     * @return
     *     possible object is
     *     {@link VersionExtension }
     *     
     */
    public VersionExtension getNextVersionBase() {
        return nextVersionBase;
    }

    /**
     * Sets the value of the nextVersionBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link VersionExtension }
     *     
     */
    public void setNextVersionBase(VersionExtension value) {
        this.nextVersionBase = value;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link org.w3c.dom.Element }
     * 
     * 
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the debuglabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebuglabel() {
        return debuglabel;
    }

    /**
     * Sets the value of the debuglabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebuglabel(String value) {
        this.debuglabel = value;
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
