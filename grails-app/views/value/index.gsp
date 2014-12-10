<%@ page import="au.org.ala.keys.Value" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'value.label', default: 'Value')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-value" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                            default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="list-value" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>
            <th><g:message code="value.taxon.label" default="Taxon"/></th>

            <th><g:message code="value.attribute.label" default="Attribute"/></th>

            <th><g:message code="value.value.label" default="Value"/>

            <g:sortableColumn property="created" title="${message(code: 'value.created.label', default: 'Created')}"/>

            <th><g:message code="value.createdBy.label" default="Created By"/></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${valueInstanceList}" status="i" var="valueInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show" controller="taxon"
                            id="${valueInstance.taxon.id}">${valueInstance.taxon.scientificName.encodeAsHTML()}</g:link></td>

                <td><g:link action="show" controller="attribute"
                            id="${valueInstance.attribute.id}">${valueInstance.attribute.label.encodeAsHTML()}</g:link></td>

                <td><g:link action="show"
                            id="${valueInstance.id}">${valueInstance.asText().encodeAsHTML()}</g:link></td>

                <td><g:formatDate date="${valueInstance.created}"/></td>

                <td></td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${valueInstanceCount ?: 0}"/>
    </div>
</div>
</body>
</html>
