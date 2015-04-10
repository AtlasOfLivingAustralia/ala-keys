<%@ page import="au.org.ala.keys.Attribute" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'attribute.label', default: 'Attribute')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-attribute" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="list-attribute" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label"
                   args="[entityName]"/>&nbsp;for Key ${(key.alaUserId ?: "") + ":" + (key.filename ?: "")}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>
            <g:sortableColumn property="taxon.scientificName"
                              title="${message(code: 'attribute.taxon.label', default: 'Taxon')}"/>

            <g:sortableColumn property="attribute.label"
                              title="${message(code: 'value.attribute.label', default: 'Attribute')}"/>

            <g:sortableColumn property="attribute.units"
                              title="${message(code: 'value.units.label', default: 'Units')}"/>

            <th><g:message code="value.value.label" default="Value"/>

            <g:sortableColumn property="created" title="${message(code: 'value.created.label', default: 'Created')}"/>
        </tr>
        </thead>
        <tbody>
        <g:each in="${valueInstanceList}" status="i" var="valueInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show" controller="taxon"
                            id="${valueInstance.taxon.id}">${valueInstance.taxon.scientificName.encodeAsHTML()}</g:link></td>

                <td><g:link action="show" controller="attribute"
                            id="${valueInstance.attribute.id}">${valueInstance.attribute.label.encodeAsHTML()}</g:link></td>

                <td>${valueInstance.attribute.units?.encodeAsHTML()}</td>

                <td><g:link action="show"
                            id="${valueInstance.id}">${valueInstance.asText([noLabel: true, noUnits: true]).encodeAsHTML()}</g:link></td>

                <td><g:formatDate date="${valueInstance.created}"/></td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate controller="value" action="key" id="${key.id}" total="${valueInstanceCount ?: 0}"/>
    </div>
</div>
</body>
</html>
