<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Catalog</title>
    <link rel="stylesheet" type="text/css" href="/main.css"/>
</head>
<body>
<a href="">Продукты</a>
<a href="<c:url value='/categories'/>">Категории</a>
<a href="<c:url value='/file/upload'/>">Изображения</a>
<hr>
<br>
<form:form action="/product/add" modelAttribute="form">
    <table>
        <c:if test="${!empty form.id}">
            <tr>
                <td>
                    <form:label path="id">
                        <spring:message text="Ид"/>
                    </form:label>
                </td>
                <td>
                    <form:input path="id" readonly="true" disabled="true"/>
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
            <td>
                <form:label path="manufacturer">
                    <spring:message text="Производитель"/>
                </form:label>
            </td>
            <td>
                <form:input path="manufacturer"/>
            </td>
        </tr>
        <tr>
            <td>
                <form:label path="price">
                    <spring:message text="Цена"/>
                </form:label>
            </td>
            <td>
                <form:input path="price" required="required" pattern="([0-9]*[.])?[0-9]+"/>
            </td>
        </tr>
        <tr>
            <td>
                <form:label path="category">
                    <spring:message text="Категория"/>
                </form:label>
            </td>
            <td>
                <form:select path="category">
                    <c:forEach var="item" items="${categories}">
                        <c:choose>
                            <c:when test="${item.name eq selectedCategory}">
                                <option selected>${item.name}</option>
                            </c:when>
                            <c:otherwise>
                                <option>${item.name}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </form:select>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="<spring:message text="Ok"/>"/>
            </td>
        </tr>
    </table>
</form:form>
<br>
<h2>Продукты</h2>
<form:form action="/product/category/" modelAttribute="filterForm" method="GET">
    <table>
        <tr>
            <td>
                <form:label path="name">
                    <spring:message text="Выберите категорию:"/>
                </form:label>
            </td>
            <td>
                <form:select path="name" id="filter">
                    <c:forEach var="item" items="${categories}">
                        <c:choose>
                            <c:when test="${item.name eq filterBy}">
                                <option selected>${item.name}</option>
                            </c:when>
                            <c:otherwise>
                                <option>${item.name}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </form:select>
            </td>
            <td colspan="2">
                <input type="submit" value="<spring:message text="Ok"/>"/>
            </td>
        </tr>
    </table>
</form:form>
<c:if test="${!empty products}">
    <table class="tg">
        <tr>
            <th width="80">Ид</th>
            <th width="120">Наименование</th>
            <th width="120">Описание</th>
            <th width="120">Производитель</th>
            <th width="120">Цена</th>
            <th width="120">Категория</th>
            <th width="130">Дата создания</th>
            <th width="60">Редактировать</th>
            <th width="60">Удалить</th>
        </tr>
        <c:forEach items="${products}" var="product">
            <tr>
                <td>${product.id}</td>
                <td>${product.name}</td>
                <td>${product.description}</td>
                <td>${product.manufacturer}</td>
                <td>${product.price}</td>
                <td>${product.category}</td>
                <td><fmt:formatDate value="${product.created}" pattern="dd.MM.yyyy hh:mm:ss"/></td>
                <td><a href="<c:url value='/product/edit/${product.id}' />">Редактировать</a></td>
                <td><a href="<c:url value='/product/remove/${product.id}' />">Удалить</a></td>
            </tr>
        </c:forEach>
    </table>
</c:if>
</body>
</html>
