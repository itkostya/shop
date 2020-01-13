<%@page import="com.eva.web.tools.PageContainer" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>

<head>
    <title>Eva - test task</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:useBean id="now" class="java.util.Date"/>
</head>

<body>
<h2>Test task Eva</h2>
    <div><a href='${pageContext.request.contextPath}${PageContainer.CREATE_CHECK_PAGE}'> Create products and check results</a>
    </div>
</body>
</html>