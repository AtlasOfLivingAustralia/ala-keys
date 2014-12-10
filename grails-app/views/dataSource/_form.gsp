<%@ page import="au.org.ala.keys.DataSource" %>



<div class="fieldcontain ${hasErrors(bean: dataSourceInstance, field: 'alaUserId', 'error')} ">
    <label for="alaUserId">
        <g:message code="dataSource.alaUserId.label" default="Ala User Id"/>

    </label>
    <g:textField name="alaUserId" value="${dataSourceInstance?.alaUserId}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: dataSourceInstance, field: 'attributes', 'error')} ">
    <label for="attributes">
        <g:message code="dataSource.attributes.label" default="Attributes"/>

    </label>

    <ul class="one-to-many">
        <g:each in="${dataSourceInstance?.attributes ?}" var="a">
            <li><g:link controller="attribute" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
            <g:link controller="attribute" action="create"
                    params="['dataSource.id': dataSourceInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'attribute.label', default: 'Attribute')])}</g:link>
        </li>
    </ul>

</div>

<div class="fieldcontain ${hasErrors(bean: dataSourceInstance, field: 'created', 'error')} ">
    <label for="created">
        <g:message code="dataSource.created.label" default="Created"/>

    </label>
    <g:datePicker name="created" precision="day" value="${dataSourceInstance?.created}" default="none"
                  noSelection="['': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: dataSourceInstance, field: 'description', 'error')} ">
    <label for="description">
        <g:message code="dataSource.description.label" default="Description"/>

    </label>
    <g:textField name="description" value="${dataSourceInstance?.description}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: dataSourceInstance, field: 'filename', 'error')} ">
    <label for="filename">
        <g:message code="dataSource.filename.label" default="Filename"/>

    </label>
    <g:textField name="filename" value="${dataSourceInstance?.filename}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: dataSourceInstance, field: 'taxons', 'error')} ">
    <label for="taxons">
        <g:message code="dataSource.taxons.label" default="Taxons"/>

    </label>

    <ul class="one-to-many">
        <g:each in="${dataSourceInstance?.taxons ?}" var="t">
            <li><g:link controller="taxon" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
            <g:link controller="taxon" action="create"
                    params="['dataSource.id': dataSourceInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'taxon.label', default: 'Taxon')])}</g:link>
        </li>
    </ul>

</div>

<div class="fieldcontain ${hasErrors(bean: dataSourceInstance, field: 'values', 'error')} ">
    <label for="values">
        <g:message code="dataSource.values.label" default="Values"/>

    </label>

    <ul class="one-to-many">
        <g:each in="${dataSourceInstance?.values ?}" var="v">
            <li><g:link controller="value" action="show" id="${v.id}">${v?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
            <g:link controller="value" action="create"
                    params="['dataSource.id': dataSourceInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'value.label', default: 'Value')])}</g:link>
        </li>
    </ul>

</div>

