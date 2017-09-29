<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/main.css"/>
</head>
<body>
<a href="<c:url value='/'/>">Вернуться к просмотру продуктов</a>
<hr>
Ошибка редактирования продукта:
<br>
${message}
<br>
<br>
Сообщите о проблеме системного администратору. Для решения проблемы смотрите системный журнал приложения.
</body>
</html>
