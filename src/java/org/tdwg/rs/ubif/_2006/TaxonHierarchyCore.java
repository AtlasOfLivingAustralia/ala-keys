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
 * TaxonHierarchy plus core level extensions.
 * 
 * <p>Java class for TaxonHierarchyCore complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TaxonHierarchyCore">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}TaxonHierarchy">
 *       &lt;group ref="{http://rs.tdwg.org/UBIF/2006/}TaxonHierarchyCoreExtensions"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxonHierarchyCore", propOrder = {
    "scope",
    "taxonHierarchyType",
    "nodes",
    "nextVersion"
})
public class TaxonHierarchyCore
    extends TaxonHierarchy
{

    @XmlElement(name = "Scope")
    protected TaxonomicScopeSet scope;
    @XmlElement(name = "TaxonHierarchyType", required = true)
    protected QName taxonHierarchyType;
    @XmlElement(name = "Nodes")
    protected TaxonHierarchyNodeSeq nodes;
    @XmlElement(name = "NextVersion")
    protected VersionExtension nextVersion;

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link TaxonomicScopeSet }
     *     
     */
    public TaxonomicScopeSet getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaxonomicScopeSet }
     *     
     */
    public void setScope(TaxonomicScopeSet value) {
        this.scope = value;
    }

    /**
     * Gets the value of the taxonHierarchyType property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.namespace.QName }
     *     
     */
    public QName getTaxonHierarchyType() {
        return taxonHierarchyType;
    }

    /**
     * Sets the value of the taxonHierarchyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.namespace.QName }
     *     
     */
    public void setTaxonHierarchyType(QName value) {
        this.taxonHierarchyType = value;
    }

    /**
     * Gets the value of the nodes property.
     * 
     * @return
     *     possible object is
     *     {@link TaxonHierarchyNodeSeq }
     *     
     */
    public TaxonHierarchyNodeSeq getNodes() {
        return nodes;
    }

    /**
     * Sets the value of the nodes property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaxonHierarchyNodeSeq }
     *     
     */
    public void setNodes(TaxonHierarchyNodeSeq value) {
        this.nodes = value;
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
