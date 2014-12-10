<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Grails Runtime Exception</title>
    <meta name="layout" content="main">

</head>

<body>
<header id="page-header">
    <div class="inner row-fluid">
        <hgroup>
            <h1>Error</h1>
        </hgroup>
    </div>
</header>

<div class="inner">
    <div>${message}</div>
    %{--<div>${exception}</div>--}%
    %{--<!-- class: ${exception.metaClass?.getTheClass()} -->--}%
    <g:renderException exception="${exception}"/>
</div>
</body>
</html>