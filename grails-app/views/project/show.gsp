<%@ page import="au.org.ala.keys.Project" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-project" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                              default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="show-project" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list project">

        <g:if test="${projectInstance?.created}">
            <li class="fieldcontain">
                <span id="created-label" class="property-label"><g:message code="project.created.label"
                                                                           default="Created"/></span>

                <span class="property-value" aria-labelledby="created-label"><g:formatDate
                        date="${projectInstance?.created}"/></span>

            </li>
        </g:if>

        <g:if test="${projectInstance?.description}">
            <li class="fieldcontain">
                <span id="description-label" class="property-label"><g:message code="project.description.label"
                                                                               default="Description"/></span>

                <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${projectInstance}"
                                                                                               field="description"/></span>

            </li>
        </g:if>

        <g:if test="${projectInstance?.name}">
            <li class="fieldcontain">
                <span id="name-label" class="property-label"><g:message code="project.name.label"
                                                                        default="Name"/></span>

                <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${projectInstance}"
                                                                                        field="name"/></span>

            </li>
        </g:if>

    </ol>
    <g:form url="[resource: projectInstance, action: 'delete']" method="DELETE">
        <fieldset class="buttons">
            <g:link class="edit" action="edit" resource="${projectInstance}"><g:message code="default.button.edit.label"
                                                                                        default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
