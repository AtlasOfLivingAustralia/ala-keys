<%@ page import="au.org.ala.keys.Key" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title>Upload New Document</title>
    <r:require modules="fileupload,progressbar"/>
</head>

<body>
<div class="nav" role="navigation">
    <ul><li><g:link class="home" controller="home" action="index">Home</g:link></li></ul>
</div>

<div class="content scaffold-create" role="main">
    <h1>Upload New Document</h1>
    <g:if test="${flash.message}"><div class="message" role="status">${flash.message}</div></g:if>
    <g:uploadForm action="upload">

        <div class="fieldcontain ">
            <label for="project">
                <g:message code="key.project.label" default="Project"/>

            </label>
            <g:select id="project" name="project.id" from="${au.org.ala.keys.Project.list()}" optionKey="id"
                      optionValue="name"
                      value="" class="many-to-one" noSelection="['null': '']"/>

        </div>

        <fieldset class="form">
            <input type="file" name="file"/>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="upload" class="save" value="Upload"/>
        </fieldset>
    </g:uploadForm>
<!--
    <br>
    <h1>Add One Entry</h1>
    <form class="addOneForm" enctype='application/json'>

        <div class="row">
            <div class="span3">
                <h3>Species</h3>
                <p>Search.</p>
                <input type="text" name="lsid" id="searchSpecies" placeholder="Search species" />
            </div>

            <div class="span3">
                <h3>Attribute (optional)</h3>
                <p>Search or enter your own.</p>
                <input type="text" name="attributeId" id="searchAttributes" placeholder="Search attributes (optional)" />
                <input type="text" name="attributeName" id="attributeName" placeholder="or enter your own (optional)" />
                <input type="text" name="attributeUnits" id="attributeUnits" placeholder="attribute units (optional)" />
            </div>

            <div class="span3">
                <h3>Value</h3>
                <p>Enter a value as one of; free text description, number (e.g. "7"), range (e.g. "3-7")</p>
                <input type="text" name="value" id="value" placeholder="Enter a value" />
            </div>
        </div>

        <div class="input-append" id="searchKeys">
            <button class="btn" type="submit">Search</button>
        </div>
    </form>
    <script>

        // initialise plugins
        jQuery(function(){
            // autocomplete on navbar search input
            jQuery("#searchSpecies").autocomplete('//bie.ala.org.au/search/auto.jsonp', {
                extraParams: {limit: 100},
                dataType: 'jsonp',
                parse: function(data) {
                    console.log(data)
                    var rows = new Array();
                    data = data.autoCompleteList;
                    for(var i=0; i<data.length; i++) {
                        rows[i] = {
                            data:data[i],
                            value: data[i].guid,
                            result: data[i].matchedNames[0]
                        };
                    }
                    return rows;
                },
                matchSubset: false,
                formatItem: function(row, i, n) {
                    return row.matchedNames[0] + " <i>(" + row.rankString + ")</i><p>" + row.guid + "</p>";
                },
                minChars: 3,
                select: function(event, ui) {
                    alert('select')
                }
            });
        });


    </script>
</div> -->
</body>
</html>