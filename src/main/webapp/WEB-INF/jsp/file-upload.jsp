<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Catalog</title>
    <link rel="stylesheet" type="text/css" href="/main.css"/>
</head>
<body>
<a href="<c:url value='/'/>">Продукты</a>
<a href="<c:url value='/categories'/>">Категории</a>
<a href="<c:url value='/file/upload'/>">Изображения</a>
<hr>
<br>
<form action="/upload" method="post" enctype="multipart/form-data" id="upload">
    Выберите продукт:
    <select title="product" name="product" required>
        <option></option>
        <c:forEach var="item" items="${products}">
            <option>${item.name}</option>
        </c:forEach>
    </select>
    <input type="file" name="file" required="required"/>
    <input type="submit" />
</form>
<br>
<c:if test="${!empty productsWithImage}">
    <table class="tg">
        <tr>
            <th width="80">Ид</th>
            <th width="120">Наименование</th>
            <th width="120">Описание</th>
            <th width="300">Изображение</th>
        </tr>
        <c:forEach items="${productsWithImage}" var="product">
            <tr>
                <td>${product.id}</td>
                <td>${product.name}</td>
                <td>${product.description}</td>
                <td><img src="<c:url value='/download/${product.image}?ext=${fn:substringAfter(product.image, ".")}' />" style="width:304px;height:auto;"></td>
            </tr>
        </c:forEach>
    </table>
</c:if>
</body>
</html>
