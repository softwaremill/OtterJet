<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>MVC 1.0</title>
</head>
<body>
<h1>Comments</h1>
<c:if test="${bindingResult != null}">
    <ul>
        <c:forEach var="message" items="${bindingResult.allMessages}">
            <li><span style="color: red;">${mvc.encoders.html(message)}</span></li>
        </c:forEach>
    </ul>
</c:if>
<form action="${mvc.basePath}/comments" method="post">
    <input name="comment" placeholder="Comment"><input type="submit" value="Send">
    <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}">
</form>
<ul>
    <c:forEach var="comment" items="${comments.comments}">
        <li>${mvc.encoders.html(comment)}</li>
    </c:forEach>
</ul>
</body>
</html>
