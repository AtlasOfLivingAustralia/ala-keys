<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title>Document List</title>
</head>

<body>
<style>
.treeNode {
    margin-left: 20px;
    cursor: pointer;
}
</style>

<div class="nav" role="navigation">
    <g:link controller="uploadItem" class="create"
            action="create">Upload Delta, Lucid, SDD file or a ZIP file containing these</g:link>
</div>

<div class="content scaffold-list" role="main">
    <g:if test="${flash.message}"><div class="message" role="status">${flash.message}</div></g:if>
    <h1>Data Summary</h1>
    <table>
        <tbody>
        <tr class="odd">
            <td>Projects</td>
            <td><g:link controller="project">${projectCount}</g:link></td>
        </tr>
        <tr class="even">
            <td>Data Sources</td>
            <td><g:link controller="key">${keyCount}</g:link></td>
        </tr>
        <tr class="odd">
            <td>Taxon</td>
            <td><g:link controller="taxon">${taxonCount}</g:link></td>
        </tr>
        <tr class="even">
            <td>Attributes</td>
            <td><g:link controller="attribute">${attributeCount}</g:link></td>
        </tr>
        <tr class="odd">
            <td>Values</td>
            <td><g:link controller="value">${valueCount}</g:link></td>
        </tr>
        </tbody>
    </table>
</div>

<div class="section">
    <h1>Webservices</h1>
    <li>
        <h3>Searching</h3>
        <ul>
            <p>Find records by type. Types are project, key, attribute, value, taxon</p>

            <p><b>usage:</b> GET /{type}</p>
            <h4>parameters</h4>
            <table class="table table-bordered">
                <tr>
                    <td>q</td><td>optional</td><td>
                    Searches text fields that contain 'q'
                    <ul>
                        <li>project: name, description, geographic scope</li>
                        <li>key: name, description, geographic scope, filename, status</li>
                        <li>attribute: label, notes, units</li>
                        <li>value: label, notes, units</li>
                        <li>taxon: scientific name, lsid</li></ul>
                </td>
                </tr>
                <tr>
                    <td>projects</td><td>optional</td><td>Restrict search to one or more projects using a comma separated list of project ids.</td>
                </tr>
                <tr>
                    <td>keys</td><td>optional</td><td>Restrict search to one or more keys using a comma separated list of key ids.</td>
                </tr>
                <tr>
                    <td>attribute</td><td>optional</td><td>Restrict search to one or more attributes using a comma separated list of attribute ids.</td>
                </tr>
                <tr>
                    <td>value</td><td>optional</td><td>Restrict search to one or more values using a comma separated list of value ids.</td>
                </tr>
                <tr>
                    <td>taxon</td><td>optional</td><td>Restrict search to one or more taxon using a comma separated list of taxon ids.</td>
                </tr>
                <tr>
                    <td>max</td><td>optional</td><td>Max number of records to return. Default is 10.</td>
                </tr>
                <tr>
                    <td>offset</td><td>optional</td><td>Paging</td>
                </tr>
            </table>
            <h4>examples</h4>
            <ul>
                <li>List all projects <a href="project">/project</a></li>
                <li>List all keys in a project <a href="key?project=3">/key?projects=3</a></li>
                <li>List all attributes in a key <a href="attribute?keys=162703">/attribute?keys=162703</a></li>
                <li>List all values for two taxon <a href="value?taxons=2,6">/value?taxons=2,6</a></li>
                <li>List all taxon containing the text 'Magnoliidae' <a
                        href="taxon?q=Magnoliidae">/taxon?q=Magnoliidae</a></li>
            </ul>
        </ul>
    </li>
    <li>
        <h3>Show a record</h3>
        <ul>
            <p>Show one record by type. Types are project, key, attribute, value, taxon</p>

            <p><b>usage:</b> GET /{type}/show/{id}</p>
            <h4>examples</h4>
            <ul>
                <li>A project <a href="project/show/1">/project/show/1</a></li>
                <li>A key <a href="key/show/1">/key/show/1</a></li>
                <li>An attribute <a href="attribute/show/1">/attribute/show/1</a></li>
                <li>A value <a href="value/show/1">/value/show/1</a></li>
                <li>A taxon <a href="taxon/show/1">/taxon/show/1</a></li>
            </ul>
        </ul>
    </li>

    <li>
        <h3>Update a record</h3>
        <ul>
            <p>Update a single record by type and id. Types are project, key, attribute, value, taxon</p>

            <p><b>usage:</b> PUT JSON to /{type}/update</p>
            <table class="table table-bordered">
                <tr>
                    <th>{type}</th>
                    <th>JSON attributes</th>
                </tr>
                <tr><td>project</td>
                    <td>
                        <ul>
                            <li>id (mandatory): integer</li>
                            <li>name (optional): string max length 255</li>
                            <li>description (optional): string</li>
                            <li>geographicScope (optional): string max length 255</li>
                            <li>imageUrl (optional): string max length 255</li>
                            <li>scopeTaxons (optional): JSON array e.g. ["Acacia"]</li>
                        </ul>
                    </td></tr>
                <tr><td>key</td>
                    <td>
                        <ul>
                            <li>id (mandatory): integer</li>
                            <li>name (optional): string max length 255</li>
                            <li>description (optional): string</li>
                            <li>geographicScope (optional): string max length 255</li>
                            <li>scopeTaxons (optional): JSON array e.g. ["Acacia"]</li>
                        </ul>
                    </td></tr>
                <tr><td>attribute</td>
                    <td>
                        <ul>
                            <li>id (mandatory): integer</li>
                            <li>label (optional): string max length 255</li>
                            <li>notes (optional): string</li>
                            <li>units (optional): string max length 255</li>
                        </ul>
                    </td></tr>
                <tr><td>value</td>
                    <td>
                        <ul>
                            <li>id (mandatory): integer</li>
                            <li>text (optional): string max length 255</li>
                            <li>min (optional): number</li>
                            <li>max (optional): number</li>
                        </ul>
                    </td></tr>
            </table>
            <h4>examples</h4>
            <ul>
                <li>Update project 279; PUT <i>{ "id": 279, "scopeTaxons": ["Acacia"], "description": "test update" }</i> to /project/update
                </li>
            </ul>
        </ul>
    </li>

    <li>
        <h3>Delete a record</h3>
        <ul>
            <p>Delete a record by type. Types are project, key, attribute, value, taxon</p>

            <p><b>usage:</b> DELETE /{type}/delete/{id}</p>
            <h4>examples</h4>
            <ul>
                <li>Delete a project; DELETE /project/delete/1</li>
                <li>Delete a key; DELETE /key/delete/1</li>
                <li>Delete an attribute; DELETE /attribute/delete/1</li>
                <li>Delete a value; DELETE /value/delete/1</li>
                <li>Delete a taxon; DELETE /taxon/delete/1</li>
            </ul>
        </ul>
    </li>

    <li>
        <h3>LSID refresh</h3>
        <ul>
            <p>Re-searches all taxon scientific names to update LSIDs and hierarchy from the local namesearch index.</p>

            <p><b>usage:</b> GET /taxon/updateLsids</p>
        </ul>
    </li>

    <li>
        <h3>Set the LSID for a scientific name</h3>
        <ul>
            <p>Update a list of or a single taxon with an LSID by scientificName. The hierarchy is updated from the
            local namesearch index using the new LSID when the new LSID is found in the namesearch index.</p>

            <p><b>usage:</b> PUT JSON array to /taxon/setLsid</p>
            <table class="table table-bordered">
                <tr>
                    <th>JSON array item attributes</th>
                </tr>
                <tr>
                    <td>
                        <ul>
                            <li>scientificName (mandatory): string</li>
                            <li>lsid (mandatory): string</li>
                        </ul>
                    </td></tr>
            </table>
            <h4>examples</h4>
            <ul>
                <li>Update LSID for the scientific name Artabotrys;
                PUT <i>[ { "scientificName" : "Artabotrys", "lsid" : "urn:lsid:biodiversity.org.au:apni.taxon:314376" } ]</i>
                    to /taxon/setLsid</li>
            </ul>
        </ul>
    </li>


    <h1>Other webservices (may not work)</h1>
    <li>
        <h3>Search Keys</h3>
        <ul>
            <p>Find data sources. ws/search/key</p>
        <h4>parameters</h4>
        <table>
            <tr>
                <td>q</td><td>optional</td><td>Search term. Searches Key fields; filename, alaUserId, status (status is one of; loading, loaded, failed, adhoc)</td>
            </tr>
            <tr>
                <td>max</td><td>optional</td><td>Max number of records to return.</td>
            </tr>
            <tr>
                <td>offset</td><td>optional</td><td>Paging</td>
            </tr>
            <tr>
                <td>sort</td><td>optional</td><td>one of; id, name, filename, alaId. Default is id.</td>
            </tr>
            <tr>
                <td>order</td><td>optional</td><td>asc or desc. Default is asc.</td>
            </tr>
        </table>
        <h4>examples</h4>
            <a href="ws/search/key?q=loaded">ws/search/key?q=loaded</a>
        </ul>
    </li>

    <li>
        <h3>Search Attributes</h3>
        <ul>
            <p>Find attributes. /ws/search/attribute</p>
            <h4>parameters</h4>
            <table>
                <tr>
                    <td>q</td><td>optional</td><td>Search term. Searches Attribute fields; label, units</td>
                </tr>
                <tr>
                    <td>primaryOnly</td><td>optional</td><td>Include primary attributes only (no duplicates). Default is true.</td>
                </tr>
                <tr>
                    <td>max</td><td>optional</td><td>Max number of records to return.</td>
                </tr>
                <tr>
                    <td>offset</td><td>optional</td><td>Paging</td>
                </tr>
                <tr>
                    <td>sort</td><td>optional</td><td>one of; id, label, units</td>
                </tr>
                <tr>
                    <td>order</td><td>optional</td><td>asc or desc. Default is asc.</td>
                </tr>
            </table>
            <h4>examples</h4>
            <a href="ws/search/attribute?q=mm">ws/search/attribute?q=mm</a>
        </ul>
    </li>

    <li>
        <h3>Search Values</h3>
        <ul>
            <p>Find matching values. /ws/search/value</p>
            <h4>parameters</h4>
            <table>
                <tr>
                    <td>q</td><td>optional</td><td>Search term. Searches Value as text or overlapping numeric range (e.g. 1 or 1-2)</td>
                </tr>
                <tr>
                    <td>attributeId</td><td>optional</td><td>Restrict results to values belonging to a single Attribute Id</td>
                </tr>
                <tr>
                    <td>exactMatch</td><td>optional</td><td>Restrict to exact match results of wild card search. Default is false.</td>
                </tr>
                <tr>
                    <td>includeTaxon</td><td>optional</td><td>Include taxon lists in the result. Default is true.</td>
                </tr>
                <tr>
                    <td>max</td><td>optional</td><td>Max number of records to return.</td>
                </tr>
                <tr>
                    <td>offset</td><td>optional</td><td>Paging</td>
                </tr>
                <tr>
                    <td>sort</td><td>optional</td><td>one of; id, min, max, text. Default is id.</td>
                </tr>
                <tr>
                    <td>order</td><td>optional</td><td>asc or desc. Default is asc.</td>
                </tr>
            </table>
            <h4>examples</h4>
            <p>
                List values and assigned taxon with a numerical range or value overlapping '1'. <a href="ws/search/value?q=1">ws/search/value?q=1</a>
            </p>
            <p>
                List unique values assigned to attributeId=1.
            <a href="ws/search/value?attributeId=1&includeTaxon=false">ws/search/value?attributeId=1&includeTaxon=false</a>
            </p>
        </ul>
    </li>

    <li>
        <h3>Create Record</h3>
        <ul>
            <p>Create a new taxon-attribute-value record. uploadItem/upload </p>
            <h4>parameters</h4>
            <table>
                <tr>
                    <td>POST body</td><td>mandatory</td><td>
                    <p>JSON containing mandatory apiKey, alaUserId and list of data to create.
                        <br>
                        Data to create is:
                        <ul>
                        <li>lsid, mandatory</li>
                        <li>attributeLabel, optional (default: "description")</li>
                        <li>attributeUnits, optional (default: null)</li>
                        <li>value: mandatory (numerical, numerical range or text)</li>
                        </ul>

                    </p>
                    </td>
                </tr>
            </table>
            <h4>examples</h4>
            <p>Create a new value.
            URL: <a href="uploadItem/upload">uploadItem/upload</a>
            POST body:
            {
                "apiKey": "123",
                "alaUserId": "321",
                "data": [
                            {
                                "lsid": "urn:lsid:biodiversity.org.au:afd.taxon:31a9b8b8-4e8f-4343-a15f-2ed24e0bf1ae",
                                "attributeLabel": "test number attribute",
                                "attributeUnits": "mm",
                                "value": "5"
                            },
                            {
                                "lsid": "urn:lsid:biodiversity.org.au:afd.taxon:31a9b8b8-4e8f-4343-a15f-2ed24e0bf1ae",
                                "attributeLabel": "test text attribute",
                                "value": "some Text"
                            }
                        ]
            }
        </ul>
    </li>

</div>

</body>

</html>