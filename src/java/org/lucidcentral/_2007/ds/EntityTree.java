//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.13 at 10:31:49 AM EST 
//


package org.lucidcentral._2007.ds;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntityTree complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EntityTree">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.lucidcentral.org/2007/DS/}AbstractVersionedObject">
 *       &lt;sequence>
 *         &lt;element name="Nodes" type="{http://www.lucidcentral.org/2007/DS/}EntityTreeNodeSet"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntityTree", propOrder = {
    "nodes"
})
public class EntityTree
    extends AbstractVersionedObject
{

    @XmlElement(name = "Nodes", required = true)
    protected EntityTreeNodeSet nodes;

    /**
     * Gets the value of the nodes property.
     * 
     * @return
     *     possible object is
     *     {@link EntityTreeNodeSet }
     *     
     */
    public EntityTreeNodeSet getNodes() {
        return nodes;
    }

    /**
     * Sets the value of the nodes property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityTreeNodeSet }
     *     
     */
    public void setNodes(EntityTreeNodeSet value) {
        this.nodes = value;
    }

}
