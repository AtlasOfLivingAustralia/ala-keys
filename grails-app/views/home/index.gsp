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
<script>
    function expand(div) {
        if (event != null && event.target.tagName != 'DIV') return

        var url = "search/tree?branch=" + encodeURIComponent((div.id == "tree_root" ? '' : div.id.split('|')[0])) + "&rank=" + (div.id == "tree_root" ? '' : div.id.split('|')[1])
        $.get(url, function (data) {
            for (i = 0; i < data.nodes.length; i++) {
                var newId = data.nodes[i].lsid + "|" + data.rank
                var bieLink = "<a href='http://bie.ala.org.au/species/" + data.nodes[i].lsid + "' target='_blank'>view in BIE</a>"
                if (data.nodes[i].lsid == null) {
                    bieLink = ''
                }
                var labelHtml = "<div onclick='expand(this.parentNode)' >+ " + data.nodes[i].name + " (" + data.nodes[i].count + ") " + bieLink + "</div>"
                if (data.nodes[i].taxonId != undefined) {
                    labelHtml = "<div><a href='taxon/show/" + (data.nodes[i].taxonId) + "'>" + data.nodes[i].name + " (" + data.nodes[i].rank + ") " + bieLink + "</a></div>"
                }
                var insert = "<div id='" + newId + "' class='treeNode'>" + labelHtml + "</div>"
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