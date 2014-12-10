<%@ page import="au.org.ala.keys.Attribute" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'attribute.label', default: 'Attribute')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-attribute" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="show-attribute" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list attribute">

        <g:if test="${attributeInstance?.characterTypeNumeric}">
            <li class="fieldcontain">
                <span id="characterTypeNumeric-label" class="property-label"><g:message
                        code="attribute.characterTypeNumeric.label" default="Character Type Numeric"/></span>

                <span class="property-value" aria-labelledby="characterTypeNumeric-label"><g:formatBoolean
                        boolean="${attributeInstance?.characterTypeNumeric}"/></span>

            </li>
        </g:if>

        <g:if test="${attributeInstance?.characterTypeText}">
            <li class="fieldcontain">
                <span id="characterTypeText-label" class="property-label"><g:message
                        code="attribute.characterTypeText.label" default="Character Type Text"/></span>

                <span class="property-value" aria-labelledby="characterTypeText-label"><g:formatBoolean
                        boolean="${attributeInstance?.characterTypeText}"/></span>

            </li>
        </g:if>

        <g:if test="${attributeInstance?.created}">
            <li class="fieldcontain">
                <span id="created-label" class="property-label"><g:message code="attribute.created.label"
                                                                           default="Created"/></span>

                <span class="property-value" aria-labelledby="created-label"><g:formatDate
                        date="${attributeInstance?.created}"/></span>

            </li>
        </g:if>

        <g:if test="${attributeInstance?.createdBy}">
            <li class="fieldcontain">
                <span id="createdBy-label" class="property-label"><g:message code="attribute.createdBy.label"
                                                                             default="Created By"/></span>

                <span class="property-value" aria-labelledby="createdBy-label"><g:link controller="dataSource"
                                                                                       action="show"
                                                                                       id="${attributeInstance?.createdBy?.id}">${attributeInstance?.createdBy?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

        <g:if test="${attributeInstance?.label}">
            <li class="fieldcontain">
                <span id="label-label" class="property-label"><g:message code="attribute.label.label"
                                                                         default="Label"/></span>

                <span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${attributeInstance}"
                                                                                         field="label"/></span>

            </li>
        </g:if>

        <g:if test="${attributeInstance?.notes}">
            <li class="fieldcontain">
                <span id="notes-label" class="property-label"><g:message code="attribute.notes.label"
                                                                         default="Notes"/></span>

                <span class="property-value" aria-labelledby="notes-label"><g:fieldValue bean="${attributeInstance}"
                                                                                         field="notes"/></span>

            </li>
        </g:if>

        <g:if test="${attributeInstance?.textValues}">
            <li class="fieldcontain">
                <span id="textValues-label" class="property-label"><g:message code="attribute.textValues.label"
                                                                              default="Text Values"/></span>

                <span class="property-value" aria-labelledby="textValues-label"><g:fieldValue
                        bean="${attributeInstance}" field="textValues"/></span>

            </li>
        </g:if>


        <g:if test="${attributeInstance?.units}">
            <li class="fieldcontain">
                <span id="units-label" class="property-label"><g:message code="attribute.units.label"
                                                                         default="Units"/></span>

                <span class="property-value" aria-labelledby="units-label"><g:fieldValue bean="${attributeInstance}"
                                                                                         field="units"/></span>

            </li>
        </g:if>

    </ol>
    <g:form url="[resource: attributeInstance, action: 'delete']" method="DELETE">
        <fieldset class="buttons">
            <g:link class="edit" action="edit" resource="${attributeInstance}"><g:message
                    code="default.button.edit.label" default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
