<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <title>Еда</title>
</head>
<body>
<section>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>Дата/Время:</dt>
            <dd><input type="datetime-local" name="dateTime" value="${meal.dateTime}"></dd>
        </dl>
        <dl>
            <dt>Описание:</dt>
            <dd><input type="text" name="description" size=40 value="${meal.description}"></dd>
        </dl>
        <dl>
            <dt>Калории:</dt>
            <dd><input type="text" name="calories" size=40 value="${meal.calories}"></dd>
        </dl>
        <button type="submit" style="height:30px; width:100px">Сохранить</button>
        <button type="reset" onclick="window.history.back()" style="height:30px; width:100px">Отменить</button>
    </form>
</section>
</body>
</html>