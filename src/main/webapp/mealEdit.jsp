<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Meal edit
  User: Sergey Filippov (serdg1984@yandex.ru).
  Version: 1.0
  Since: 2019-02-17 
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal Edit</title>
    <style>
        dl {
        background: none repeat scroll 0 0 #FAFAFA;
            margin: 8px 0;
            padding: 0;
        }
        dt {
            display: inline-block;
            width: 170px;
        }
        dd {
            display: inline-block;
            margin-left: 8px;
            vertical-align: top;
        }

    </style>
</head>
<body>
<section>
    <h2><a href="index.html">Home</a></h2>
    <h3>EditMeal</h3>
    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>DataTime:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime"></dd>
        </dl>
        <dl>
            <dt>Description:</dt>
            <dd><input type="text" value="${meal.description}" size="40" name="description"></dd>
        </dl>
        <dl>
            <dt>Calories:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories"></dd>
        </dl>
        <button type="submit">Save</button>
        <button onclick="window.history.back()">Cancel</button>
    </form>
</section>
</body>
</html>
