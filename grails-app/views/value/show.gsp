<%@ page import="au.org.ala.keys.Value" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'value.label', default: 'Value')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-value" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                            default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="show-value" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list value">

        <g:if test="${valueInstance?.attribute}">
            <li class="fieldcontain">
                <span id="attribute-label" class="property-label"><g:message code="value.attribute.label"
                                                                             default="Attribute"/></span>

                <span class="property-value" aria-labelledby="attribute-label"><g:link controller="attribute"
                                                                                       action="show"
                                                                                       id="${valueInstance?.attribute?.id}">${valueInstance?.attribute?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

        <g:if test="${valueInstance?.created}">
            <li class="fieldcontain">
                <span id="created-label" class="property-label"><g:message code="value.created.label"
                                                                           default="Created"/></span>

                <span class="property-value" aria-labelledby="created-label"><g:formatDate
                        date="${valueInstance?.created}"/></span>

            </li>
        </g:if>

        <g:if test="${valueInstance?.createdBy}">
            <li class="fieldcontain">
                <span id="createdBy-label" class="property-label"><g:message code="value.createdBy.label"
                                                                             default="Created By"/></span>

                <span class="property-value" aria-labelledby="createdBy-label"><g:link controller="dataSource"
                                                                                       action="show"
                                                                                       id="${valueInstance?.createdBy?.id}">${valueInstance?.createdBy?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

        <g:if test="${valueInstance?.max}">
            <li class="fieldcontain">
                <span id="max-label" class="property-label"><g:message code="value.max.label" default="Max"/></span>

                <span class="property-value" aria-labelledby="max-label"><g:fieldValue bean="${valueInstance}"
                                                                                       field="max"/></span>

            </li>
        </g:if>

        <g:if test="${valueInstance?.min}">
            <li class="fieldcontain">
                <span id="min-label" class="property-label"><g:message code="value.min.label" default="Min"/></span>

                <span class="property-value" aria-labelledby="min-label"><g:fieldValue bean="${valueInstance}"
                                                                                       field="min"/></span>

            </li>
        </g:if>

        <g:if test="${valueInstance?.taxon}">
            <li class="fieldcontain">
                <span id="taxon-label" class="property-label"><g:message code="value.taxon.label"
                                                                         default="Taxon"/></span>

                <span class="property-value" aria-labelledby="taxon-label"><g:link controller="taxon" action="show"
                                                                                   id="${valueInstance?.taxon?.id}">${valueInstance?.taxon?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

        <g:if test="${valueInstance?.text}">
            <li class="fieldcontain">
                <span id="text-label" class="property-label"><g:message code="value.text.label" default="Text"/></span>

                <span class="property-value" aria-labelledby="text-label"><g:fieldValue bean="${valueInstance}"
                                                                                        field="text"/></span>

            </li>
        </g:if>

    </ol>
    <g:form url="[resource: valueInstance, action: 'delete']" method="DELETE">
        <fieldset class="buttons">
            <g:link class="edit" action="edit" resource="${valueInstance}"><g:message code="default.button.edit.label"
                                                                                      default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
