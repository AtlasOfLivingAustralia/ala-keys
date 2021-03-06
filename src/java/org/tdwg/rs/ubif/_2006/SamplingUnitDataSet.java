//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.18 at 10:25:48 AM EST 
//


package org.tdwg.rs.ubif._2006;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * (This is equivalent to the SummaryDataSet - the sample data structure has the intermediate event for which no equivalent exists in summary data!)
 * 
 * <p>Java class for SamplingUnitDataSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SamplingUnitDataSet">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}Set">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="Categorical" type="{http://rs.tdwg.org/UBIF/2006/}CatSampleData"/>
 *         &lt;element name="Quantitative" type="{http://rs.tdwg.org/UBIF/2006/}QuantSampleData"/>
 *         &lt;element name="TextChar" type="{http://rs.tdwg.org/UBIF/2006/}FreeFormTextData"/>
 *         &lt;element name="Text" type="{http://rs.tdwg.org/UBIF/2006/}FreeFormTextData"/>
 *         &lt;element name="Sequence" type="{http://rs.tdwg.org/UBIF/2006/}MolecularSequenceData"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SamplingUnitDataSet", propOrder = {
    "categoricalOrQuantitativeOrTextChar"
})
public class SamplingUnitDataSet
    extends Set
{

    @XmlElementRefs({
        @XmlElementRef(name = "Quantitative", namespace = "http://rs.tdwg.org/UBIF/2006/", type = JAXBElement.class),
        @XmlElementRef(name = "Categorical", namespace = "http://rs.tdwg.org/UBIF/2006/", type = JAXBElement.class),
        @XmlElementRef(name = "TextChar", namespace = "http://rs.tdwg.org/UBIF/2006/", type = JAXBElement.class),
        @XmlElementRef(name = "Sequence", namespace = "http://rs.tdwg.org/UBIF/2006/", type = JAXBElement.class),
        @XmlElementRef(name = "Text", namespace = "http://rs.tdwg.org/UBIF/2006/", type = JAXBElement.class)
    })
    protected List<JAXBElement<? extends CharacterRef>> categoricalOrQuantitativeOrTextChar;

    /**
     * Gets the value of the categoricalOrQuantitativeOrTextChar property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the categoricalOrQuantitativeOrTextChar property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategoricalOrQuantitativeOrTextChar().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link FreeFormTextData }{@code >}
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link QuantSampleData }{@code >}
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link MolecularSequenceData }{@code >}
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link CatSampleData }{@code >}
     * {@link javax.xml.bind.JAXBElement }{@code <}{@link FreeFormTextData }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends CharacterRef>> getCategoricalOrQuantitativeOrTextChar() {
        if (categoricalOrQuantitativeOrTextChar == null) {
            categoricalOrQuantitativeOrTextChar = new ArrayList<JAXBElement<? extends CharacterRef>>();
        }
        return this.categoricalOrQuantitativeOrTextChar;
    }

}
