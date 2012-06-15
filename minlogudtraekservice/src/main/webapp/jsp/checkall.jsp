<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="dk.nsi.minlog.web.IsAlive" %>
<%  
	response.setContentType("text/plain"); 

	WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);

	IsAlive isAlive = (IsAlive)context.getBean("isAlive");
	isAlive.checkAll(out);
%>