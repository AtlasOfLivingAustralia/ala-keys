<%@ page import="au.org.ala.keys.Taxon" %>


<div class="fieldcontain ${hasErrors(bean: taxonInstance, field: 'lsid', 'error')} ">
    <label for="lsid">
        <g:message code="taxon.lsid.label" default="Lsid"/>

    </label>
    <g:textField name="lsid" value="${taxonInstance?.lsid}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: taxonInstance, field: 'rank', 'error')} ">
    <label for="rank">
        <g:message code="taxon.rank.label" default="Rank"/>

    </label>
    <g:textField name="rank" value="${taxonInstance?.rank}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: taxonInstance, field: 'scientificName', 'error')} ">
    <label for="scientificName">
        <g:message code="taxon.scientificName.label" default="Scientific Name"/>

    </label>
    <g:textField name="scientificName" value="${taxonInstance?.scientificName}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: taxonInstance, field: 'values', 'error')} ">
    <label for="values">
        <g:message code="taxon.values.label" default="Values"/>

    </label>

    <ul class="one-to-many">
        <g:each in="${taxonInstance?.values ?}" var="v">
            <li><g:link controller="value" action="show" id="${v.id}">${v?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
            <g:link controller="value" action="create"
                    params="['taxon.id': taxonInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'value.label', default: 'Value')])}</g:link>
        </li>
    </ul>

</div>

