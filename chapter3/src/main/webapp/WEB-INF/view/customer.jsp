<%--
  Created by IntelliJ IDEA.
  User: a010
  Date: 2018/1/11
  Time: 21:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>客户管理-客户列表</title>
</head>
<body>
    <h1>客户列表</h1>

    <table>
        <tr>
            <th>客户名称</th>
            <th>联系人</th>
            <th>电话号码</th>
            <th>邮箱</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${customers}" var="customer">
            <tr>
                <td>${customer.name}</td>
                <td>${customer.contact}</td>
                <td>${customer.telephone}</td>
                <td>${customer.email}</td>
                <td>
                    <a href="">编辑</a>
                    <a href="">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
