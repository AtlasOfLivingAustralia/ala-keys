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
 * <p>Java class for NaturalLanguagePhraseSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NaturalLanguagePhraseSet">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}Set">
 *       &lt;sequence>
 *         &lt;element name="Phrase" type="{http://rs.tdwg.org/UBIF/2006/}NatLangPhraseString" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NaturalLanguagePhraseSet", propOrder = {
    "phrase"
})
public class NaturalLanguagePhraseSet
    extends Set
{

    @XmlElement(name = "Phrase", required = true)
    protected List<NatLangPhraseString> phrase;

    /**
     * Gets the value of the phrase property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the phrase property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPhrase().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NatLangPhraseString }
     * 
     * 
     */
    public List<NatLangPhraseString> getPhrase() {
        if (phrase == null) {
            phrase = new ArrayList<NatLangPhraseString>();
        }
        return this.phrase;
    }

}
