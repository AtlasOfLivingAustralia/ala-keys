//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.13 at 10:31:49 AM EST 
//


package org.lucidcentral._2007.ds;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FeatureSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FeatureSet">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.lucidcentral.org/2007/DS/}Set">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="MultistateFeature" type="{http://www.lucidcentral.org/2007/DS/}MultistateFeature"/>
 *         &lt;element name="NumericFeature" type="{http://www.lucidcentral.org/2007/DS/}NumericFeature"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FeatureSet", propOrder = {
    "multistateFeatureOrNumericFeature"
})
public class FeatureSet
    extends Set
{

    @XmlElements({
        @XmlElement(name = "MultistateFeature", type = MultistateFeature.class),
        @XmlElement(name = "NumericFeature", type = NumericFeature.class)
    })
    protected List<Feature> multistateFeatureOrNumericFeature;

    /**
     * Gets the value of the multistateFeatureOrNumericFeature property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the multistateFeatureOrNumericFeature property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMultistateFeatureOrNumericFeature().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MultistateFeature }
     * {@link NumericFeature }
     * 
     * 
     */
    public List<Feature> getMultistateFeatureOrNumericFeature() {
        if (multistateFeatureOrNumericFeature == null) {
            multistateFeatureOrNumericFeature = new ArrayList<Feature>();
        }
        return this.multistateFeatureOrNumericFeature;
    }

}