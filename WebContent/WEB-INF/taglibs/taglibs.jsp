<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="oa" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.zml.oa.util.Constants" %>
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
	
	long sysInitTime = Constants.SYSY_INIT_TIME;
    //系统启动时间
    request.setAttribute("sysInitTime",sysInitTime);
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" type="text/css" href="${ctx}/css/themes/<%=easyuiThemeName %>/easyui.css">
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-migrate-1.2.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/easyui/common.css">
<%-- <link rel="stylesheet" type="text/css" href="${ctx}/css/bootstrap.min.css"> --%>
<%-- <link rel="stylesheet" type="text/css" href="${ctx}/css/easyui/templatemo_main.css"> --%>

<link rel="stylesheet" href="${ctx}/css/jquery_validation_engine/validationEngine.jquery.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/jquery_validation_engine/template.css" type="text/css" />

<script type="text/javascript" src="${ctx}/js/util.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui/easyui-lang-zh_CN.js"></script>
<%-- <script type="text/javascript" src="${ctx}/js/bootstrap.min.js"></script> --%>
<script type="text/javascript" src="${ctx}/js/easyui/jqueryUtil.js"></script>

<script type="text/javascript" src="${ctx}/js/jquery_validation_engine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery_validation_engine/jquery.validationEngine-zh_CN.js"></script>

<script type="text/javascript" charset="utf-8">
    var ctx = "${ctx}";
    var ctxStatic = "${ctxStatic}";
</script>

<style type="text/css">
	body {
	    font-family:helvetica,tahoma,verdana,sans-serif;
	    font-size:13px;
	    margin:0px 0px 0px 0px;
	    padding:0px 0px 0px 0px;
	}
</style>

