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
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="isNumeric"
                              title="${message(code: 'attribute.isNumeric.label', default: 'Is Numeric')}"/>

            <g:sortableColumn property="isText"
                              title="${message(code: 'attribute.isText.label', default: 'Is Text')}"/>

            <g:sortableColumn property="created"
                              title="${message(code: 'attribute.created.label', default: 'Created')}"/>

            <g:sortableColumn property="label" title="${message(code: 'attribute.label.label', default: 'Label')}"/>

            <g:sortableColumn property="notes" title="${message(code: 'attribute.notes.label', default: 'Notes')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${attributeInstanceList}" status="i" var="attributeInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${attributeInstance.id}">${fieldValue(bean: attributeInstance, field: "isNumeric")}</g:link></td>

                <td><g:formatBoolean boolean="${attributeInstance.isText}"/></td>

                <td><g:formatDate date="${attributeInstance.created}"/></td>

                <td>${fieldValue(bean: attributeInstance, field: "label")}</td>

                <td>${fieldValue(bean: attributeInstance, field: "notes")}</td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${attributeInstanceCount ?: 0}"/>
    </div>
</div>
</body>
</html>
