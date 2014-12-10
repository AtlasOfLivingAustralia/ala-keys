<%@ page import="au.org.ala.keys.DataSource" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'dataSource.label', default: 'DataSource')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <r:require modules="progressbar"/>
</head>

<body>
<a href="#show-dataSource" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                 default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="show-dataSource" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list dataSource">

        <g:if test="${dataSource?.alaUserId}">
            <li class="fieldcontain">
                <span id="alaUserId-label" class="property-label"><g:message code="dataSource.alaUserId.label"
                                                                             default="Ala User Id"/></span>

                <span class="property-value" aria-labelledby="alaUserId-label">${dataSource.alaUserId}</span>

            </li>
        </g:if>

        <g:if test="${dataSource?.created}">
            <li class="fieldcontain">
                <span id="created-label" class="property-label"><g:message code="dataSource.created.label"
                                                                           default="Created"/></span>

                <span class="property-value" aria-labelledby="created-label"><g:formatDate
                        date="${dataSource?.created}"/></span>

            </li>
        </g:if>

        <g:if test="${dataSource?.description}">
            <li class="fieldcontain">
                <span id="description-label" class="property-label"><g:message code="dataSource.description.label"
                                                                               default="Description"/></span>

                <span class="property-value" aria-labelledby="description-label">${dataSource.description}/></span>

            </li>
        </g:if>

        <g:if test="${dataSource?.filename}">
            <li class="fieldcontain">
                <span id="filename-label" class="property-label"><g:message code="dataSource.filename.label"
                                                                            default="Filename"/></span>

                <span class="property-value" aria-labelledby="filename-label">${dataSource.filename}</span>

            </li>
        </g:if>

        <g:if test="${dataSource?.status}">
            <li class="fieldcontain">
                <span id="status-label" class="property-label"><g:message code="dataSource.status.label"
                                                                          default="Status"/></span>

                <g:if test="${dataSource?.status == 'loading'}">
                    <span class="property-value" aria-labelledby="status-label" id="uploadFeedback">
                    </span>

                    <script>
                        function randomString(length) {
                            var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz'.split('');

                            if (!length) {
                                length = Math.floor(Math.random() * chars.length);
                            }

                            var str = '';
                            for (var i = 0; i < length; i++) {
                                str += chars[Math.floor(Math.random() * chars.length)];
                            }
                            return str;
                        }

                        function updateStatusPolling() {

                            $.get("../../uploadItem/status/${dataSource.id}" + "?random=" + randomString(10), function (data) {
                                console.log("Retrieving status...." + data.status + ", percentage: " + data.percentage);
                                if (data.status == "loaded") {
                                    $('.progress .bar').attr('data-percentage', '100');
                                    $('#uploadFeedback').html('loaded');
                                    $('.progress .bar').progressbar();
                                    //reload page
                                    location.reload()
                                } else if (data.status == "failed") {
                                    $("#uploadFeedback").html('failed');
                                } else {
                                    $('.progress .bar').attr('data-percentage', data.percentage);
                                    $('.progress .bar').progressbar();
                                    $("#uploadFeedback").html('Percentage completed: ' + (Math.round(data.percentage * 100) / 100) + '%. ' + data.status + '');
                                    setTimeout("updateStatusPolling()", 1000);
                                }
                            });
                        }
                        setTimeout("updateStatusPolling()", 1000);
                    </script>
                </g:if>
                <g:if test="${dataSource?.status != 'loading'}">
                    <span class="property-value" aria-labelledby="status-label">${dataSource.status}</span>
                </g:if>

            </li>
            <g:if test="${dataSource?.status == 'loading'}">
                <div id="progressBar" class="progress progress-info" style="margin-top:20px;">
                    <div class="bar" data-percentage="0"></div>
                </div>
            </g:if>
        </g:if>

        <g:if test="${dataSource?.attributesCount}">
            <li class="fieldcontain">
                <span id="attributes-label" class="property-label"><g:message code="dataSource.attributes.label"
                                                                              default="Attributes"/></span>
                <span class="property-value" aria-labelledby="attributes-label"><g:link controller="attribute"
                                                                                        action="dataSource"
                                                                                        id="${dataSource.id}">${dataSource.attributesCount}</g:link></span>

            </li>
        </g:if>

        <g:if test="${dataSource?.valuesCount}">
            <li class="fieldcontain">
                <span id="values-label" class="property-label"><g:message code="dataSource.values.label"
                                                                          default="Values"/></span>
                <span class="property-value" aria-labelledby="values-label"><g:link controller="value"
                                                                                    action="dataSource"
                                                                                    id="${dataSource.id}">${dataSource.valuesCount}</g:link></span>

            </li>
        </g:if>

    </ol>
    <g:form url="[id: dataSource.id, action: 'delete', controller: 'dataSource']" method="DELETE">
        <fieldset class="buttons">
            <g:link class="edit" action="edit" resource="${dataSource.id}"><g:message code="default.button.edit.label"
                                                                                      default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
