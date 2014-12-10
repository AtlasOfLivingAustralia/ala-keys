<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'taxon.label', default: 'Taxon')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-taxon" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                            default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="show-taxon" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list taxon">>

        <g:if test="${taxon?.created}">
            <li class="fieldcontain">
                <span id="created-label" class="property-label"><g:message code="taxon.created.label"
                                                                           default="Created"/></span>

                <span class="property-value" aria-labelledby="created-label"><g:formatDate
                        date="${taxon?.created}"/></span>

            </li>
        </g:if>

        <g:if test="${taxon?.lsid}">
            <li class="fieldcontain">
                <span id="lsid-label" class="property-label"><g:message code="taxon.lsid.label" default="Lsid"/></span>

                <span class="property-value" aria-labelledby="lsid-label">${taxon.lsid}</span>

            </li>
        </g:if>

        <g:if test="${taxon?.rank}">
            <li class="fieldcontain">
                <span id="rank-label" class="property-label"><g:message code="taxon.rank.label" default="Rank"/></span>

                <span class="property-value" aria-labelledby="rank-label">${taxon.rank}</span>

            </li>
        </g:if>

        <g:if test="${taxon?.scientificName}">
            <li class="fieldcontain">
                <span id="scientificName-label" class="property-label"><g:message code="taxon.scientificName.label"
                                                                                  default="Scientific Name"/></span>

                <span class="property-value" aria-labelledby="scientificName-label">${taxon.scientificName}</span>

            </li>
        </g:if>

        <g:if test="${taxon?.values}">
            <li class="fieldcontain">
                <span id="values-label" class="property-label"><g:message code="taxon.values.label"
                                                                          default="Values"/></span>

                <g:each in="${taxon.values}" var="v">
                    <span class="property-value" aria-labelledby="values-label"><g:link controller="value" action="show"
                                                                                        id="${v.id}">
                        ${v.asText().encodeAsHTML()}</g:link></span>
                </g:each>

            </li>
        </g:if>

        <g:if test="${inherited}">
            <li class="fieldcontain">
                <span id="values-label" class="property-label"><g:message code="taxon.valuesInherited.label"
                                                                          default="Values Inherited"/></span>

                <g:each in="${inherited}" var="v">
                    <span class="property-value" aria-labelledby="values-label"><g:link controller="value" action="show"
                                                                                        id="${v.id}">
                        ${v.asText().encodeAsHTML().encodeAsHTML()}</g:link></span>
                </g:each>

            </li>
        </g:if>

    </ol>
    <g:form url="[resource: taxonInstance, action: 'delete']" method="DELETE">
        <fieldset class="buttons">
            <g:link class="edit" action="edit" resource="${taxon}"><g:message code="default.button.edit.label"
                                                                              default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
