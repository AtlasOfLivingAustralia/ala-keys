<%@ page import="au.org.ala.keys.Project" %>



<div class="fieldcontain ${hasErrors(bean: projectInstance, field: 'created', 'error')} ">
    <label for="created">
        <g:message code="project.created.label" default="Created"/>

    </label>
    <g:datePicker name="created" precision="day" value="${projectInstance?.created}" default="none"
                  noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: projectInstance, field: 'description', 'error')} ">
    <label for="description">
        <g:message code="project.description.label" default="Description"/>

    </label>
    <g:textField name="description" value="${projectInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: projectInstance, field: 'name', 'error')} ">
    <label for="name">
        <g:message code="project.name.label" default="Name"/>

    </label>
    <g:textField name="name" value="${projectInstance?.name}"/>
</div>

