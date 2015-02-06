<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>top</title>

<link rel="stylesheet" href="${ctx}/css/style.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/top.css" type="text/css" />

</head>

<body>

	<div id="header">
        
        <div class="logo"></div>
        <div class="admin_nav">
        	<ul>
            </ul>
        </div>
        <div class="welcome"><shiro:principal/>&nbsp;&nbsp;你好，欢迎登录！&nbsp;&nbsp;[&nbsp;<a href="${ctx}/logout" target="_parent" class="white underline">退出</a>&nbsp;]</div>

	</div>
    
</body>


</html>
