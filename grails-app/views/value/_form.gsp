<%@ page import="au.org.ala.keys.Value" %>

<div class="fieldcontain ${hasErrors(bean: valueInstance, field: 'attribute', 'error')} ">
    <label for="attribute">
        <g:message code="value.attribute.label" default="Attribute"/>

    </label>
    <g:select id="attribute" name="attribute.id" from="${au.org.ala.keys.Attribute.list()}" optionKey="id"
              value="${valueInstance?.attribute?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: valueInstance, field: 'created', 'error')} ">
    <label for="created">
        <g:message code="value.created.label" default="Created"/>

    </label>
    <g:datePicker name="created" precision="day" value="${valueInstance?.created}" default="none"
                  noSelection="['': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: valueInstance, field: 'createdBy', 'error')} ">
    <label for="createdBy">
        <g:message code="value.createdBy.label" default="Created By"/>

    </label>
    <g:select id="createdBy" name="createdBy.id" from="${au.org.ala.keys.Key.list()}" optionKey="id"
              value="${valueInstance?.createdBy?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: valueInstance, field: 'max', 'error')} ">
    <label for="max">
        <g:message code="value.max.label" default="Max"/>

    </label>
    <g:field name="max" type="number" value="${valueInstance.max}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: valueInstance, field: 'min', 'error')} ">
    <label for="min">
        <g:message code="value.min.label" default="Min"/>

    </label>
    <g:field name="min" type="number" value="${valueInstance.min}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: valueInstance, field: 'taxon', 'error')} ">
    <label for="taxon">
        <g:message code="value.taxon.label" default="Taxon"/>

    </label>
    <g:select id="taxon" name="taxon.id" from="${au.org.ala.keys.Taxon.list()}" optionKey="id"
              value="${valueInstance?.taxon?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: valueInstance, field: 'text', 'error')} ">
    <label for="text">
        <g:message code="value.text.label" default="Text"/>

    </label>
    <g:textField name="text" value="${valueInstance?.text}"/>

</div>

