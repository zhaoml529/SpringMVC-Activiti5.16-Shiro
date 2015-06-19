<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
<html>
  <head>
    <title>在线用户列表</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/online_user.js?_=${sysInitTime}"></script>
  </head>

<body>

	<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
		<span class="badge">提示</span>
		<p>
			在此您可以查看在线<span class="label-info"><strong>用户</strong></span>，可以随时<span class="label-info"><strong>踢出</strong></span>此用户！
		</p>
	</div>
	<table id="user_datagrid" title="在线用户管理"></table>
  </body>
</body>
</html>
