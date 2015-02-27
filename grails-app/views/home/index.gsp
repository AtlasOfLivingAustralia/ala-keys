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
            <td><g:link controller="dataSource">${dataSourceCount}</g:link></td>
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
    <h1>Taxon Tree</h1>

    <div id="tree_root" class="treeNode">
        <div></div>
    </div>
</div>

<div class="section">
    <h1>Webservices</h1>

    <li>
        <h3>Search DataSources</h3>
        <ul>
        <p>Find data sources. ws/search/dataSource</p>
        <h4>parameters</h4>
        <table>
            <tr>
                <td>q</td><td>optional</td><td>Search term. Searches DataSource fields; filename, alaUserId, status (status is one of; loading, loaded, failed, adhoc)</td>
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
            <a href="ws/search/dataSource?q=loaded">ws/search/dataSource?q=loaded</a>
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

<script>
    function expand(div) {
        if (event != null && event.target.tagName != 'DIV') return

        var url = "search/tree?left=" + (div.id == "tree_root" ? '' : div.id.split('|')[2]) + "&right=" + (div.id == "tree_root" ? '' : div.id.split('|')[3]) + "&rank=" + (div.id == "tree_root" ? '' : div.id.split('|')[1])
        $.get(url, function (data) {
            console.log(data)
            for (i = 0; i < data.nodes.length; i++) {
                var newId = data.nodes[i].lsid + "|" + data.rank + "|" + data.nodes[i].left + "|" + data.nodes[i].right
                var bieLink = "<a href='http://bie.ala.org.au/species/" + data.nodes[i].lsid + "' target='_blank'>view in BIE</a>"
                if (data.nodes[i].lsid == null) {
                    bieLink = ''
                }
                var labelHtml = "<div onclick='expand(this.parentNode)' >+ " + data.nodes[i].name + " (" + data.nodes[i].count + ") " + bieLink + "</div>"
                if (data.nodes[i].taxonId != undefined) {
                    labelHtml = "<div><a href='taxon/show/" + (data.nodes[i].taxonId) + "'>" + data.nodes[i].name + " (" + data.nodes[i].rank + ") " + bieLink + "</a></div>"
                }
                var insert = "<div id='" + newId + "' class='treeNode'>" + labelHtml + "</div>"
                console.log(newId)
                div.innerHTML += insert
            }
            div.firstChild.onclick = function () {
                collapse(div)
            }
            if (div.firstChild.firstChild != null) div.firstChild.firstChild.textContent = "-" + div.firstChild.firstChild.textContent.substring(1)
        });
    }

    function collapse(div) {
        while (div.firstChild != div.lastChild) {
            div.removeChild(div.lastChild)
        }
        div.firstChild.onclick = function () {
            expand(div)
        }
        div.firstChild.firstChild.textContent = "+" + div.firstChild.firstChild.textContent.substring(1)
    }

    expand($('#tree_root')[0]);
</script>
</body>

</html>