<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="oa" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String easyuiThemeName="metro";
	Cookie cookies[] =request.getCookies();
	if(cookies!=null&&cookies.length>0){
		for(Cookie cookie : cookies){
			if (cookie.getName().equals("cookiesColor")) {
				easyuiThemeName = cookie.getValue();
				break;
			}
		}
	}
%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" type="text/css" href="${ctx}/css/themes/<%=easyuiThemeName %>/easyui.css">

<link rel="stylesheet" type="text/css" href="${ctx}/css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/easyui/common.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/easyui/templatemo_main.css">

<link rel="stylesheet" href="${ctx}/css/jquery_validation_engine/validationEngine.jquery.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/jquery_validation_engine/template.css" type="text/css" />

<script type="text/javascript" src="${ctx}/js/util.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui/jqueryUtil.js"></script>

<script type="text/javascript" src="${ctx}/js/jquery_validation_engine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery_validation_engine/jquery.validationEngine-zh_CN.js"></script>

<style type="text/css">
	body {
	    font-family:helvetica,tahoma,verdana,sans-serif;
	    font-size:13px;
	    margin:0px 0px 0px 0px;
	    padding:0px 0px 0px 0px;
	}
</style>
