<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<script type="text/javascript" src="${ctx}/js/app/delegate_user.js?_=${sysInitTime}"></script>    
<title>选取委派人员</title>

</head>
<body>
	<input type="hidden" id="groupId" value="${groupId }"/>
	 
    <table id="user_datagrid" title="委派人列表"></table>
</body>

</html>
