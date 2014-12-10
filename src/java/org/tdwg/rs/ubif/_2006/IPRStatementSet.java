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
import javax.xml.bind.annotation.XmlType;


/**
 * Set of IPR statements. 
 * 
 * (Internal validation on element using this: each role value should occur only once.)
 * 
 * <p>Java class for IPRStatementSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPRStatementSet">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}Set">
 *       &lt;sequence>
 *         &lt;element name="IPRStatement" type="{http://rs.tdwg.org/UBIF/2006/}IPRStatement" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPRStatementSet", propOrder = {
    "iprStatement"
})
public class IPRStatementSet
    extends Set
{

    @XmlElement(name = "IPRStatement", required = true)
    protected List<IPRStatement> iprStatement;

    /**
     * Gets the value of the iprStatement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the iprStatement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIPRStatement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IPRStatement }
     * 
     * 
     */
    public List<IPRStatement> getIPRStatement() {
        if (iprStatement == null) {
            iprStatement = new ArrayList<IPRStatement>();
        }
        return this.iprStatement;
    }

}
