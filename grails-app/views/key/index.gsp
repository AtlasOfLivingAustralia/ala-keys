<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'key.label', default: 'Key')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

<a href="#list-key" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                 default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" controller="uploadItem" action="create"><g:message code="default.new.label"
                                                                                      args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="list-key" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <p>
        Keys are uploaded files, individual files (.dlt, .xml, .zip) in uploaded ZIPs, users with adhoc edits.
    </p>

    <form class="keysSearchForm">
        <div class="input-append" id="searchKeys">
            <input class="span4" id="appendedInputButton" name="q" type="text" value="${params.q}"
                   placeholder="Search alaUserId, filename, description, status (adhoc, loaded, loading or failed)">
            <button class="btn" type="submit">Search</button>
        </div>
    </form>

    <form class="keysSearchForm">
        <g:if test="${params.q}">
            <button class="btn btn-primary" type="submit">Clear search</button>
        </g:if>
    </form>
    <br/>
    <table>
        <thead>
        <tr>

            <g:sortableColumn params="${params}" property="created"
                              title="${message(code: 'key.created.label', default: 'Created')}"/>

            <g:sortableColumn params="${params}" property="description"
                              title="${message(code: 'key.description.label', default: 'Description')}"/>

            <g:sortableColumn params="${params}" property="filename"
                              title="${message(code: 'key.filename.label', default: 'Filename')}"/>

            <g:sortableColumn params="${params}" property="project"
                              title="${message(code: 'key.project.label', default: 'Project')}"/>


            <g:sortableColumn params="${params}" property="status"
                              title="${message(code: 'key.status.label', default: 'Status')}"/>

            <th></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${k}" status="i" var="keyInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show" id="${keyInstance.id}"><g:formatDate
                        date="${keyInstance.created}"/></g:link></td>

                <td>${fieldValue(bean: keyInstance, field: "description")}</td>

                <td>${fieldValue(bean: keyInstance, field: "filename")}</td>

                <td>${fieldValue(bean: keyInstance, field: "project.name")}</td>

                <td>
                    <g:if test="${keyInstance?.status == 'loading'}">
                        <div id="uploadFeedback_${keyInstance.id}" style="clear:right;">
                        </div>

                        <script>
                            setTimeout("updateStatusPolling(${keyInstance.id})", 100);
                        </script>

                    </g:if>
                    <g:if test="${keyInstance?.status != 'loading'}">
                        <div style="clear:right;">
                            ${keyInstance?.status}
                        </div>
                    </g:if>
                </td>

                <td>
                    <g:if test="${keyInstance?.filename}">
                        <g:link action="download" id="${keyInstance.id}">download original</g:link>
                    </g:if>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${keyInstanceCount ?: 0}" params="${params}"/>
    </div>
</div>

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

    function updateStatusPolling(id) {

        $.get("../uploadItem/status/" + id + "?random=" + randomString(10), function (data) {
            console.log("Retrieving status...." + data.status + ", percentage: " + data.percentage);
            if (data.status == "loaded") {
                $("#uploadFeedback_" + id).html('loaded');
            } else if (data.status == "failed") {
                $("#uploadFeedback_" + id).html('failed');
            } else {
                $("#uploadFeedback_" + id).html('' + (Math.round(data.percentage * 100) / 100) + '%');
                setTimeout("updateStatusPolling(" + id + ")", 5000);
            }
        });
    }
</script>
</body>
</html>
