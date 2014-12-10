<%@ page import="au.org.ala.keys.Taxon" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'taxon.label', default: 'Taxon')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-taxon" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                            default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="list-taxon" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="created" title="${message(code: 'taxon.created.label', default: 'Created')}"/>

            <g:sortableColumn property="lsid" title="${message(code: 'taxon.lsid.label', default: 'Lsid')}"/>

            <g:sortableColumn property="rank" title="${message(code: 'taxon.rank.label', default: 'Rank')}"/>

            <g:sortableColumn property="scientificName"
                              title="${message(code: 'taxon.scientificName.label', default: 'Scientific Name')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${taxonInstanceList}" status="i" var="taxonInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${taxonInstance.id}">${fieldValue(bean: taxonInstance, field: "created")}</g:link></td>

                <td>${fieldValue(bean: taxonInstance, field: "lsid")}</td>

                <td>${fieldValue(bean: taxonInstance, field: "rank")}</td>

                <td>${fieldValue(bean: taxonInstance, field: "scientificName")}</td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${taxonInstanceCount ?: 0}"/>
    </div>
</div>
</body>
</html>
