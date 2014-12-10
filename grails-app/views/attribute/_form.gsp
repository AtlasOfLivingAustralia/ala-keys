<%@ page import="au.org.ala.keys.Attribute" %>



<div class="fieldcontain ${hasErrors(bean: attributeInstance, field: 'characterTypeNumeric', 'error')} ">
    <label for="characterTypeNumeric">
        <g:message code="attribute.characterTypeNumeric.label" default="Character Type Numeric"/>

    </label>
    <g:checkBox name="characterTypeNumeric" value="${attributeInstance?.characterTypeNumeric}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: attributeInstance, field: 'characterTypeText', 'error')} ">
    <label for="characterTypeText">
        <g:message code="attribute.characterTypeText.label" default="Character Type Text"/>

    </label>
    <g:checkBox name="characterTypeText" value="${attributeInstance?.characterTypeText}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: attributeInstance, field: 'created', 'error')} ">
    <label for="created">
        <g:message code="attribute.created.label" default="Created"/>

    </label>
    <g:datePicker name="created" precision="day" value="${attributeInstance?.created}" default="none"
                  noSelection="['': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: attributeInstance, field: 'createdBy', 'error')} ">
    <label for="createdBy">
        <g:message code="attribute.createdBy.label" default="Created By"/>

    </label>
    <g:select id="createdBy" name="createdBy.id" from="${au.org.ala.keys.DataSource.list()}" optionKey="id"
              value="${attributeInstance?.createdBy?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: attributeInstance, field: 'label', 'error')} ">
    <label for="label">
        <g:message code="attribute.label.label" default="Label"/>

    </label>
    <g:textField name="label" value="${attributeInstance?.label}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: attributeInstance, field: 'notes', 'error')} ">
    <label for="notes">
        <g:message code="attribute.notes.label" default="Notes"/>

    </label>
    <g:textField name="notes" value="${attributeInstance?.notes}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: attributeInstance, field: 'textValues', 'error')} ">
    <label for="textValues">
        <g:message code="attribute.textValues.label" default="Text Values"/>

    </label>
    <g:textField name="textValues" value="${attributeInstance?.textValues}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: attributeInstance, field: 'units', 'error')} ">
    <label for="units">
        <g:message code="attribute.units.label" default="Units"/>

    </label>
    <g:textField name="units" value="${attributeInstance?.units}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: attributeInstance, field: 'values', 'error')} ">
    <label for="values">
        <g:message code="attribute.values.label" default="Values"/>

    </label>

    <ul class="one-to-many">
        <g:each in="${attributeInstance?.values ?}" var="v">
            <li><g:link controller="value" action="show" id="${v.id}">${v?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
            <g:link controller="value" action="create"
                    params="['attribute.id': attributeInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'value.label', default: 'Value')])}</g:link>
        </li>
    </ul>

</div>

