package au.org.ala.keys

/**
 * Created by a on 30/03/15.
 */
class AttributeService {

    /**
     * Creates a new attribute with a given name. If there is another attribute with the same name this is created with
     * a reference to the other attribute.
     *
     * @param label
     * @return unsaved Attribute
     */
    public def create(label) {
//        def attributeList = Attribute.findAllByLabel(label)
//        if (attributeList.size() == 0) {
            Attribute attribute = new Attribute(label: label)
            return attribute
//        } else {
//            def parent = null
//            attributeList.each() { a ->
//                if (a.parent) {
//                    parent = a.parent;
//                }
//            }
//
//            Attribute attribute = new Attribute(label: label, parent: parent)
//            return attribute
//        }
    }
}
