<%@ page import="static ru.javawebinar.topjava.web.MealServlet.DATE_TIME_FORMATTER" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<section>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Дата/Время</th>
            <th>Описание</th>
            <th>Калории</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${meals}" var="mealTo">
            <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr style="color:${mealTo.excess ? 'red' : 'green'}">
                <td><%=DATE_TIME_FORMATTER.format(mealTo.getDateTime())%>
                </td>
                <td>${mealTo.description}</td>
                <td>${mealTo.calories}</td>
                <td><a href="meals?id=${mealTo.id}&action=edit"><img src="img/pencil.png"></a></td>
                <td><a href="meals?id=${mealTo.id}&action=delete"><img src="img/delete.png"></a></td>
            </tr>
        </c:forEach>
    </table>
    <br/>
    <a href="meals?action=edit"><button type="submit" style="height:30px; width:200px;">Добавить</button></a>
</section>
</body>
</html>
