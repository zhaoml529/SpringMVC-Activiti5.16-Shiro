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
<c:set var="sysInitTime" value="${sysInitTime}" />

<link rel="stylesheet" type="text/css" href="${ctx}/css/themes/<%=easyuiThemeName %>/easyui.css">
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-migrate-1.2.1.min.js"></script>
<%-- <link rel="stylesheet" type="text/css" href="${ctx}/css/themes/mobile.css"> --%>
<link rel="stylesheet" type="text/css" href="${ctx}/css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/easyui/common.css">
<link rel="stylesheet" type="text/css" href="${ctx }/css/tipsy.css">

<script type="text/javascript" src="${ctx}/js/moment.min.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui/jquery.easyui.min.js"></script>
<%-- <script type="text/javascript" src="${ctx}/js/easyui/jquery.easyui.mobile.js"></script> --%>
<script type="text/javascript" src="${ctx}/js/easyui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/js/easyui/jqueryUtil.js"></script>
<script type="text/javascript" src="${ctx }/js/easyui/login/jquery.tipsy.js"></script>

<script type="text/javascript" charset="utf-8">
    var ctx = "${ctx}";
    var ctxStatic = "${ctxStatic}";
    //alert(jqueryUtil.getRandTime());
    //var t = new Date().getTime();  
    //alert("sysInitTime: "+"${sysInitTime }"+"script_t: "+t);
</script>

<style type="text/css">
	body {
	    font-family:helvetica,tahoma,verdana,sans-serif;
	    font-size:13px;
	    margin:0px 0px 0px 0px;
	    padding:0px 0px 0px 0px;
	}
</style>

