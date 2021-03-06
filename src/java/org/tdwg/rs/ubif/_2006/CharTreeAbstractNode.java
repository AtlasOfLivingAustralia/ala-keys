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
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * Inner nodes (or terminal nodes if no characters follow)
 * 
 * <p>Java class for CharTree_AbstractNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CharTree_AbstractNode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Parent" type="{http://rs.tdwg.org/UBIF/2006/}CharTree_NodeRef" minOccurs="0"/>
 *         &lt;group ref="{http://rs.tdwg.org/UBIF/2006/}NaturalLanguageGroup" minOccurs="0"/>
 *         &lt;element name="DependencyRules" type="{http://rs.tdwg.org/UBIF/2006/}DependencyRules" minOccurs="0"/>
 *         &lt;element name="RecommendedModifiers" type="{http://rs.tdwg.org/UBIF/2006/}DescriptiveConceptRefSeq" minOccurs="0"/>
 *         &lt;element name="RecommendedMeasures" type="{http://rs.tdwg.org/UBIF/2006/}RecommendedMeasureSeq" minOccurs="0"/>
 *         &lt;group ref="{http://rs.tdwg.org/UBIF/2006/}Extensions"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CharTree_AbstractNode", propOrder = {
    "parent",
    "naturalLanguage",
    "dependencyRules",
    "recommendedModifiers",
    "recommendedMeasures",
    "nextVersionBase",
    "any"
})
@XmlSeeAlso({
    CharTreeCharacter.class,
    CharTreeNode.class
})
public class CharTreeAbstractNode {

    @XmlElement(name = "Parent")
    protected CharTreeNodeRef parent;
    @XmlElement(name = "NaturalLanguage")
    protected NaturalLanguagePhraseSet naturalLanguage;
    @XmlElement(name = "DependencyRules")
    protected DependencyRules dependencyRules;
    @XmlElement(name = "RecommendedModifiers")
    protected DescriptiveConceptRefSeq recommendedModifiers;
    @XmlElement(name = "RecommendedMeasures")
    protected RecommendedMeasureSeq recommendedMeasures;
    @XmlElement(name = "NextVersionBase")
    protected VersionExtension nextVersionBase;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the parent property.
     * 
     * @return
     *     possible object is
     *     {@link CharTreeNodeRef }
     *     
     */
    public CharTreeNodeRef getParent() {
        return parent;
    }

    /**
     * Sets the value of the parent property.
     * 
     * @param value
     *     allowed object is
     *     {@link CharTreeNodeRef }
     *     
     */
    public void setParent(CharTreeNodeRef value) {
        this.parent = value;
    }

    /**
     * Gets the value of the naturalLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link NaturalLanguagePhraseSet }
     *     
     */
    public NaturalLanguagePhraseSet getNaturalLanguage() {
        return naturalLanguage;
    }

    /**
     * Sets the value of the naturalLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link NaturalLanguagePhraseSet }
     *     
     */
    public void setNaturalLanguage(NaturalLanguagePhraseSet value) {
        this.naturalLanguage = value;
    }

    /**
     * Gets the value of the dependencyRules property.
     * 
     * @return
     *     possible object is
     *     {@link DependencyRules }
     *     
     */
    public DependencyRules getDependencyRules() {
        return dependencyRules;
    }

    /**
     * Sets the value of the dependencyRules property.
     * 
     * @param value
     *     allowed object is
     *     {@link DependencyRules }
     *     
     */
    public void setDependencyRules(DependencyRules value) {
        this.dependencyRules = value;
    }

    /**
     * Gets the value of the recommendedModifiers property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptiveConceptRefSeq }
     *     
     */
    public DescriptiveConceptRefSeq getRecommendedModifiers() {
        return recommendedModifiers;
    }

    /**
     * Sets the value of the recommendedModifiers property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptiveConceptRefSeq }
     *     
     */
    public void setRecommendedModifiers(DescriptiveConceptRefSeq value) {
        this.recommendedModifiers = value;
    }

    /**
     * Gets the value of the recommendedMeasures property.
     * 
     * @return
     *     possible object is
     *     {@link RecommendedMeasureSeq }
     *     
     */
    public RecommendedMeasureSeq getRecommendedMeasures() {
        return recommendedMeasures;
    }

    /**
     * Sets the value of the recommendedMeasures property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecommendedMeasureSeq }
     *     
     */
    public void setRecommendedMeasures(RecommendedMeasureSeq value) {
        this.recommendedMeasures = value;
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

}
