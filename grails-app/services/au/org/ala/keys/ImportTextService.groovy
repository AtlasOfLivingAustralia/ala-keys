package au.org.ala.keys

import au.com.bytecode.opencsv.CSVReader

class ImportTextService {
    //static transactional = false

    def importStatusService
    def attributeService
    def keyService
    def taxonService

    def importText(Key key) {
        // 3 columns; couplet number, value, couplet number OR taxon name

        char delimiter = '\t'
        List<String[]> csv = new CSVReader(new FileReader(new File(keyService.getFilePath(key))), delimiter).readAll();
        if (csv.get(0).length < 3) {
            csv = new CSVReader(new FileReader(new File(keyService.getFilePath(key)))).readAll();
        }

        def taxonNames = [:]
        def couplets = [:]
        def reverseCouplets = [:]

        for (int i = 0; i < csv.size(); i++) {
            String k = csv.get(i)[0]
            if (!couplets.containsKey(k)) {
                List list = [csv.get(i)]
                couplets.put(k, list);
            } else {
                couplets.get(k).add(csv.get(i))
            }

            char c = csv.get(i)[2].charAt(0)
            if (c < '0' || c > '9') {
                taxonNames.put(csv.get(i)[2], csv.get(i))
            } else {
                String keyReverse = csv.get(i)[2]
                if (!reverseCouplets.containsKey(keyReverse)) {
                    List list = [csv.get(i)]
                    reverseCouplets.put(keyReverse, list);
                } else {
                    reverseCouplets.get(keyReverse).add(csv.get(i))
                }
            }
        }

        //Construct one Attribute for each couplet
        def attributeParents = [:]
        couplets.each() { k, list ->
            def attribute = attributeService.create('description')
            attribute.key = key

            if (!attribute.save()) {
                attribute.errors.each {
                    log.error("error saving attribute: " + it)
                }
            }
            attributeParents.put(key, attribute)
        }
        int i = 0
        taxonNames.each() { taxon, line ->
            i = i + 1

            importStatusService.put(key.id,
                    [("loading: for taxon " + i + " of " + taxonNames.size()), (i / taxonNames.size() * 75)])

            def t = Taxon.findOrCreate(taxon)
            makeValueManyAttributes(key, line, t, attributeParents, reverseCouplets)
        }
        return true
    }

    def makeValueManyAttributes(key, line, taxon, attributeParents, reverseCouplets, depth = 0) {
        if (depth > 100) {
            log.error("depth > 100")
            return
        }

        def attribute = attributeParents.get(line[0])
        Value value = new Value(key: key, taxon: taxon, attribute: attribute, text: line[1])
        if (!value.save()) {
            value.errors.each {
                log.error("error saving value: " + it)
            }
        }

        def parents = reverseCouplets.get(line[0])
        if (parents != null) {
            parents.each() { parentline ->
                makeValueManyAttributes(key, parentline, taxon, attributeParents, reverseCouplets, depth + 1)
            }
        }
    }
}
