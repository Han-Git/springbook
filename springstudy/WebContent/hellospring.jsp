<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="springbook.temp.HelloSpring" %>
<!DOCTYPE html>
<html>
<head>
	<title>Insert title here</title>
</head>
<body>
	<%
	ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
	HelloSpring helloSpring = context.getBean(HelloSpring.class);
	out.println(helloSpring.sayHello("Root Context"));
	%>
</body>
</html>