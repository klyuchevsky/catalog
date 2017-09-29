<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Categories</title>
    <link rel="stylesheet" type="text/css" href="/main.css"/>
</head>
<body>
<a href="<c:url value='/'/>">Продукты</a>
<a href="<c:url value='/categories'/>">Категории</a>
<a href="<c:url value='/file/upload'/>">Изображения</a>
<hr>
<br>
<form:form action="/category/add" modelAttribute="form">
    <table>
        <c:if test="${!empty form.id}">
            <tr>
                <td>
                    <form:label path="id">
                        <spring:message text="Ид"/>
                    </form:label>
                </td>
                <td>
                    <form:input path="id" readonly="true" size="8" disabled="true"/>
                    <form:hidden path="id"/>
                </td>
            </tr>
        </c:if>
        <tr>
            <td>
                <form:label path="name">
                    <spring:message text="Наименование"/>
                </form:label>
            </td>
            <td>
                <form:input path="name" required="required"/>
            </td>
        </tr>
        <tr>
            <td>
                <form:label path="description">
                    <spring:message text="Описание"/>
                </form:label>
            </td>
            <td>
                <form:input path="description"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit"
                       value="<spring:message text="Ok"/>"/>
            </td>
        </tr>
    </table>
</form:form>

<br>
<h3>Категории</h3>

<c:if test="${!empty categories}">
    <table class="tg">
        <tr>
            <th width="80">Ид</th>
            <th width="120">Наименование</th>
            <th width="120">Описание</th>
            <th width="60">Редактировать</th>
            <th width="60">Удалить</th>
        </tr>
        <c:forEach items="${categories}" var="category">
            <tr>
                <td>${category.id}</td>
                <td>${category.name}</td>
                <td>${category.description}</td>
                <td><a href="<c:url value='category/edit/${category.id}' />">Редактировать</a></td>
                <td><a href="<c:url value='category/remove/${category.id}' />">Удалить</a></td>
            </tr>
        </c:forEach>
    </table>
</c:if>
</body>
</html>
