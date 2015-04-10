<%@ page import="au.org.ala.keys.Key" %>


<div class="fieldcontain ${hasErrors(bean: keyInstance, field: 'attributes', 'error')} ">
    <label for="attributes">
        <g:message code="key.attributes.label" default="Attributes"/>

    </label>

    <ul class="one-to-many">
        <g:each in="${keyInstance?.attributes ?}" var="a">
            <li><g:link controller="attribute" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
            <g:link controller="attribute" action="create"
                    params="['key.id': keyInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'attribute.label', default: 'Attribute')])}</g:link>
        </li>
    </ul>

</div>

<div class="fieldcontain ${hasErrors(bean: keyInstance, field: 'created', 'error')} ">
    <label for="created">
        <g:message code="key.created.label" default="Created"/>

    </label>
    <g:datePicker name="created" precision="day" value="${keyInstance?.created}" default="none"
                  noSelection="['': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: keyInstance, field: 'description', 'error')} ">
    <label for="description">
        <g:message code="key.description.label" default="Description"/>

    </label>
    <g:textField name="description" value="${keyInstance?.description}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: keyInstance, field: 'filename', 'error')} ">
    <label for="filename">
        <g:message code="key.filename.label" default="Filename"/>

    </label>
    <g:textField name="filename" value="${keyInstance?.filename}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: keyInstance, field: 'taxons', 'error')} ">
    <label for="taxons">
        <g:message code="key.taxons.label" default="Taxons"/>

    </label>

    <ul class="one-to-many">
        <g:each in="${keyInstance?.taxons ?}" var="t">
            <li><g:link controller="taxon" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
            <g:link controller="taxon" action="create"
                    params="['key.id': keyInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'taxon.label', default: 'Taxon')])}</g:link>
        </li>
    </ul>

</div>

<div class="fieldcontain ${hasErrors(bean: keyInstance, field: 'values', 'error')} ">
    <label for="values">
        <g:message code="key.values.label" default="Values"/>

    </label>

    <ul class="one-to-many">
        <g:each in="${keyInstance?.values ?}" var="v">
            <li><g:link controller="value" action="show" id="${v.id}">${v?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
            <g:link controller="value" action="create"
                    params="['key.id': keyInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'value.label', default: 'Value')])}</g:link>
        </li>
    </ul>

</div>

