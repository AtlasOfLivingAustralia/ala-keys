//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.13 at 10:31:49 AM EST 
//


package org.lucidcentral._2007.ds;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LinkRelEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LinkRelEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NCName">
 *     &lt;enumeration value="latest_version"/>
 *     &lt;enumeration value="prior_version"/>
 *     &lt;enumeration value="subclass_of"/>
 *     &lt;enumeration value="superclass_of"/>
 *     &lt;enumeration value="based_on"/>
 *     &lt;enumeration value="same_as"/>
 *     &lt;enumeration value="similar_to"/>
 *     &lt;enumeration value="part_of"/>
 *     &lt;enumeration value="has_part"/>
 *     &lt;enumeration value="metadata"/>
 *     &lt;enumeration value="unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LinkRelEnum")
@XmlEnum
public enum LinkRelEnum {


    /**
     * [latest] -- latest version -- Link resolving to a representation of the latest (i.e. newest) version of the object at the time of resolution. This explicitly assumes that the result of resolution changes over time (non-persistent resource).
     * 
     */
    @XmlEnumValue("latest_version")
    LATEST_VERSION("latest_version"),

    /**
     * [prev.] -- prev. version -- Link resolving to the previous version of the current object. [Not related to xhtml prev]
     * 
     */
    @XmlEnumValue("prior_version")
    PRIOR_VERSION("prior_version"),

    /**
     * INTERNAL NOTE: synonymous names considered: KindOf.
     * 
     */
    @XmlEnumValue("subclass_of")
    SUBCLASS_OF("subclass_of"),

    /**
     * [Superclass] -- Superclass Of -- Inverse of subclass of; generalization relationship pointing to the less general class.
     * 
     */
    @XmlEnumValue("superclass_of")
    SUPERCLASS_OF("superclass_of"),

    /**
     * [BasedOn] -- based on -- The current resource is based on information from another resource, in a way that is different from verbatim copying and different from the extension semantics associated with sub/superclass relations. Specifically, this includes concepts like "source", "origin", or "derivation". "BasedOn" is transitive, not symmetric, not reflexive. Persistence of the other resource is assumed. The current resource may have a similar or entirely different format than the one it is based on. Examples: 1) A record based on a physical specimen (which has a urn). 2) A reference record based on a publication that has a DOI identifier. 3) A character definition based on a standard character definition.
     * 
     */
    @XmlEnumValue("based_on")
    BASED_ON("based_on"),

    /**
     * [SameAs] -- same as -- Two resources have same semantics, same creator, same presentation format, but are not necessarily bitwise identical (e. g., sequence of elements in a set changed). SameAs is transitive, symmetric, reflexive. Persistence of the other resource is assumed. Note that multiple URIs of the current object may be given in "current", but this designates these as equal alternatives. Citing only one URI in "current" and others in "SameAs" may inform about older, outdated URIs.
     * 
     */
    @XmlEnumValue("same_as")
    SAME_AS("same_as"),

    /**
     * [SimilarTo] -- similar to -- Both resources (indepently created or of unknown derivation) are analyzed to be similar. "SimilarTo" is symmetric, reflexive, not transitive (problem of chaining). Whether a data consumer desires to make inferences on the weak "SimilarTo" relation or not is its own decision. In the case of character-based identifications, the built-in error tolerance mechansisms of the identification method will often allow doing so. Persistence of the other resource is assumed. Examples: 1) A character state may be (more or less roughly) equivalent to another in another dataset, although neither definition is based on the other. 2) Multiple images may be taken from the same object.
     * 
     */
    @XmlEnumValue("similar_to")
    SIMILAR_TO("similar_to"),

    /**
     * [Part of] -- Part of -- Indicating composition. Data may be part of other data (a), a physical object may be part of another physical object (b), or an abstract concept may be a part of another concept (c). Examples: plant = root/stem/leaf, leaf = base/stipules/petiole/lamina, etc.; medium preparation and inoculation methods are part of microbial growth rate measurement method.
     * PartOf is transitive, anti-symmetric, and reflexive (though is of theoretical use only). Detailed information on use case (a): A dataset may be a subset of a larger dataset; if a dataset is dynamically generated in response to a query it may either have a URI encoding the query or no URI at all (i. e. uri-attribute on object itself does not exist), but may want to publish the URI or its base-dataset; one purpose of this is that a publisher may want to inform indexers (crawlers) about a resolvable URI to the larger dataset, another to establish the status of archival data. - On use case b): A microscopic slide may be part of a specimen in a collection. On use case c): A method may be a part (step) of a composite method, an article part of a publication, a petal is a part of the plant flower).
     * 
     */
    @XmlEnumValue("part_of")
    PART_OF("part_of"),

    /**
     * [has part] -- has part -- Inverse of the part-of relation, indicating the parts that something consists of. A book may have resources for chapters that are independently retrievable, a specimen unit may have subunits, a descriptive term like "ovate to elliptic" has the parts "ovate" and "elliptic" (the kind of composition being not defined here!).
     * 
     */
    @XmlEnumValue("has_part")
    HAS_PART("has_part"),

    /**
     * [meta] -- meta -- Link referring to a resource offering meta data or help (more information about the current resource, including meta data like copyright or licensing, links to other sources information, etc.). 
     * 
     */
    @XmlEnumValue("metadata")
    METADATA("metadata"),
    @XmlEnumValue("unknown")
    UNKNOWN("unknown");
    private final String value;

    LinkRelEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LinkRelEnum fromValue(String v) {
        for (LinkRelEnum c: LinkRelEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
